package com.perfulandia.usuario.services;

import com.perfulandia.usuario.models.dtos.LoginDTO;
import com.perfulandia.usuario.models.dtos.UsuarioDTO;
import com.perfulandia.usuario.models.entities.Usuario;
import com.perfulandia.usuario.repositories.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public List<Usuario> obtenerTodos() {
        logger.info("Obteniendo todos los usuarios");
        return repository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> {
            logger.error("Usuario no encontrado con ID: {}", id);
            return new RuntimeException("Usuario no encontrado");
        });
    }

    public Usuario registrarUsuario(UsuarioDTO dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            logger.error("Intento de registro fallido: El email {} ya existe", dto.getEmail());
            throw new RuntimeException("El correo electrónico ya está registrado");
        }
        
        String rolAsignado = (dto.getRol() != null && !dto.getRol().isEmpty()) ? dto.getRol() : "CLIENTE";
        logger.info("Registrando nuevo usuario: {} con rol: {}", dto.getEmail(), rolAsignado);
        
        Usuario usuario = new Usuario(dto.getNombre(), dto.getEmail(), dto.getPassword(), dto.getDireccion(), rolAsignado);
        return repository.save(usuario);
    }

    public Usuario autenticar(LoginDTO dto) {
        logger.info("Intento de login para email: {}", dto.getEmail());
        Usuario usuario = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!usuario.getPassword().equals(dto.getPassword())) {
            logger.error("Contraseña incorrecta para el usuario: {}", dto.getEmail());
            throw new RuntimeException("Credenciales inválidas");
        }
        
        logger.info("Login exitoso para: {}", dto.getEmail());
        return usuario;
    }

    public Usuario actualizarPerfil(Long id, UsuarioDTO dto) {
        Usuario existente = obtenerPorId(id);
        logger.info("Actualizando perfil del usuario ID: {}", id);
        
        existente.setNombre(dto.getNombre());
        existente.setDireccion(dto.getDireccion());
        // En un caso real, el cambio de email requeriría validaciones extra, aquí lo permitiremos si no está en uso
        if (!existente.getEmail().equals(dto.getEmail()) && repository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El nuevo correo ya está en uso");
        }
        existente.setEmail(dto.getEmail());
        
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            existente.setPassword(dto.getPassword());
        }

        return repository.save(existente);
    }

    public Usuario asignarRol(Long id, String nuevoRol) {
        Usuario existente = obtenerPorId(id);
        logger.info("Asignando nuevo rol {} al usuario ID: {}", nuevoRol, id);
        existente.setRol(nuevoRol);
        return repository.save(existente);
    }
    
    public void eliminarUsuario(Long id) {
        Usuario existente = obtenerPorId(id);
        repository.delete(existente);
        logger.info("Usuario eliminado con ID: {}", id);
    }
}