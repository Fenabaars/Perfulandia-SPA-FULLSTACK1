package com.perfulandia.proveedor.services;

import com.perfulandia.proveedor.models.dtos.ProveedorDTO;
import com.perfulandia.proveedor.models.entities.Proveedor;
import com.perfulandia.proveedor.repositories.ProveedorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedorService {

    private static final Logger logger = LoggerFactory.getLogger(ProveedorService.class);
    private final ProveedorRepository repository;

    public ProveedorService(ProveedorRepository repository) {
        this.repository = repository;
    }

    public List<Proveedor> obtenerTodos() {
        logger.info("Obteniendo la lista completa de proveedores");
        return repository.findAll();
    }

    public Proveedor obtenerPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> {
            logger.error("Proveedor no encontrado con ID: {}", id);
            return new RuntimeException("Proveedor no encontrado");
        });
    }

    public Proveedor obtenerPorRut(String rut) {
        logger.info("Buscando proveedor por RUT: {}", rut);
        return repository.findByRut(rut).orElseThrow(() -> {
            logger.error("Proveedor no encontrado con RUT: {}", rut);
            return new RuntimeException("Proveedor no encontrado");
        });
    }

    public Proveedor registrarProveedor(ProveedorDTO dto) {
        if (repository.existsByRut(dto.getRut())) {
            logger.error("Error al registrar: El proveedor con RUT {} ya existe", dto.getRut());
            throw new RuntimeException("Ya existe un proveedor registrado con ese RUT");
        }
        
        logger.info("Registrando nuevo proveedor: {}", dto.getRazonSocial());
        Proveedor proveedor = new Proveedor(dto.getRut(), dto.getRazonSocial(), dto.getNombreContacto(), 
                                            dto.getEmail(), dto.getTelefono(), dto.getDireccion());
        return repository.save(proveedor);
    }

    public Proveedor actualizarProveedor(Long id, ProveedorDTO dto) {
        Proveedor existente = obtenerPorId(id);
        
        // Validar si están intentando cambiar el RUT a uno que ya pertenece a otro proveedor
        if (!existente.getRut().equals(dto.getRut()) && repository.existsByRut(dto.getRut())) {
            throw new RuntimeException("El nuevo RUT ya está asignado a otro proveedor");
        }

        logger.info("Actualizando información del proveedor ID: {}", id);
        existente.setRut(dto.getRut());
        existente.setRazonSocial(dto.getRazonSocial());
        existente.setNombreContacto(dto.getNombreContacto());
        existente.setEmail(dto.getEmail());
        existente.setTelefono(dto.getTelefono());
        existente.setDireccion(dto.getDireccion());
        
        return repository.save(existente);
    }

    public void eliminarProveedor(Long id) {
        Proveedor existente = obtenerPorId(id);
        repository.delete(existente);
        logger.info("Proveedor eliminado con ID: {}", id);
    }
}