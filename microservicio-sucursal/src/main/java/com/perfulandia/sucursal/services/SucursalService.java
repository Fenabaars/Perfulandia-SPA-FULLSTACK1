package com.perfulandia.sucursal.services;

import com.perfulandia.sucursal.models.dtos.SucursalDTO;
import com.perfulandia.sucursal.models.entities.Sucursal;
import com.perfulandia.sucursal.repositories.SucursalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalService {

    private static final Logger logger = LoggerFactory.getLogger(SucursalService.class);
    private final SucursalRepository repository;

    public SucursalService(SucursalRepository repository) {
        this.repository = repository;
    }

    public List<Sucursal> obtenerTodas() {
        logger.info("Obteniendo la lista de todas las sucursales");
        return repository.findAll();
    }

    public Sucursal obtenerPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> {
            logger.error("Sucursal no encontrada con ID: {}", id);
            return new RuntimeException("Sucursal no encontrada");
        });
    }

    public List<Sucursal> buscarPorComuna(String comuna) {
        logger.info("Buscando sucursales en la comuna: {}", comuna);
        return repository.findByComunaContainingIgnoreCase(comuna);
    }

    public Sucursal registrarSucursal(SucursalDTO dto) {
        logger.info("Registrando nueva sucursal: {}", dto.getNombre());
        Sucursal sucursal = new Sucursal(dto.getNombre(), dto.getDireccion(), dto.getComuna(), dto.getTelefono(), dto.getHorarioAtencion());
        return repository.save(sucursal);
    }

    public Sucursal actualizarSucursal(Long id, SucursalDTO dto) {
        Sucursal existente = obtenerPorId(id);
        logger.info("Actualizando información de la sucursal ID: {}", id);
        
        existente.setNombre(dto.getNombre());
        existente.setDireccion(dto.getDireccion());
        existente.setComuna(dto.getComuna());
        existente.setTelefono(dto.getTelefono());
        existente.setHorarioAtencion(dto.getHorarioAtencion());
        
        return repository.save(existente);
    }

    public void eliminarSucursal(Long id) {
        Sucursal existente = obtenerPorId(id);
        repository.delete(existente);
        logger.info("Sucursal eliminada con ID: {}", id);
    }
}