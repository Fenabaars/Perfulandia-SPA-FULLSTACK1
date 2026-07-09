package com.perfulandia.usuario.services;

import com.perfulandia.usuario.models.dtos.LoginDTO;
import com.perfulandia.usuario.models.dtos.UsuarioDTO;
import com.perfulandia.usuario.models.entities.Usuario;
import com.perfulandia.usuario.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    private Usuario usuario;
    private UsuarioDTO dto;
    private LoginDTO loginDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("Juan", "juan@test.com", "pass", "calle 1", "CLIENTE");
        usuario.setId(1L);

        dto = new UsuarioDTO();
        dto.setNombre("Juan");
        dto.setEmail("juan@test.com");
        dto.setPassword("pass");
        dto.setDireccion("calle 1");
        dto.setRol("CLIENTE");

        loginDTO = new LoginDTO();
        loginDTO.setEmail("juan@test.com");
        loginDTO.setPassword("pass");
    }

    @Test
    void testObtenerTodos() {
        when(repository.findAll()).thenReturn(Arrays.asList(usuario));
        List<Usuario> result = service.obtenerTodos();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testObtenerPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        Usuario result = service.obtenerPorId(1L);
        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
    }

    @Test
    void testObtenerPorId_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.obtenerPorId(1L));
    }

    @Test
    void testRegistrarUsuario() {
        when(repository.existsByEmail("juan@test.com")).thenReturn(false);
        when(repository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = service.registrarUsuario(dto);
        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
    }

    @Test
    void testRegistrarUsuario_EmailExists() {
        when(repository.existsByEmail("juan@test.com")).thenReturn(true);
        assertThrows(RuntimeException.class, () -> service.registrarUsuario(dto));
    }

    @Test
    void testAutenticar() {
        when(repository.findByEmail("juan@test.com")).thenReturn(Optional.of(usuario));
        Usuario result = service.autenticar(loginDTO);
        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
    }

    @Test
    void testAutenticar_WrongPassword() {
        when(repository.findByEmail("juan@test.com")).thenReturn(Optional.of(usuario));
        loginDTO.setPassword("wrong");
        assertThrows(RuntimeException.class, () -> service.autenticar(loginDTO));
    }

    @Test
    void testAutenticar_UserNotFound() {
        when(repository.findByEmail("juan@test.com")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.autenticar(loginDTO));
    }

    @Test
    void testActualizarPerfil() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(repository.save(any(Usuario.class))).thenReturn(usuario);
        
        Usuario result = service.actualizarPerfil(1L, dto);
        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
    }

    @Test
    void testActualizarPerfil_NewEmailExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        dto.setEmail("nuevo@test.com");
        when(repository.existsByEmail("nuevo@test.com")).thenReturn(true);
        
        assertThrows(RuntimeException.class, () -> service.actualizarPerfil(1L, dto));
    }

    @Test
    void testAsignarRol() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(repository.save(any(Usuario.class))).thenReturn(usuario);
        
        Usuario result = service.asignarRol(1L, "ADMIN");
        assertNotNull(result);
    }

    @Test
    void testEliminarUsuario() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(repository).delete(any(Usuario.class));

        service.eliminarUsuario(1L);
        verify(repository, times(1)).delete(usuario);
    }
}
