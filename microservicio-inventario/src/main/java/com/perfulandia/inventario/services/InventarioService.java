package com.perfulandia.inventario.services;

import com.perfulandia.inventario.clients.PerfumeClient;
import com.perfulandia.inventario.clients.SucursalClient;
import com.perfulandia.inventario.models.dtos.InventarioDTO;
import com.perfulandia.inventario.models.entities.Inventario;
import com.perfulandia.inventario.repositories.InventarioRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {

    private static final Logger logger = LoggerFactory.getLogger(InventarioService.class);
    private final InventarioRepository repository;
    private final PerfumeClient perfumeClient;
    private final SucursalClient sucursalClient;

    public InventarioService(InventarioRepository repository, PerfumeClient perfumeClient, SucursalClient sucursalClient) {
        this.repository = repository;
        this.perfumeClient = perfumeClient;
        this.sucursalClient = sucursalClient;
    }

    public Inventario ingresarStock(InventarioDTO dto) {
        logger.info("Verificando existencia de Perfume ID: {} y Sucursal ID: {}", dto.getPerfumeId(), dto.getSucursalId());
        
        // 1. Comunicación Web: Validar existencia real en los otros microservicios
        try {
            perfumeClient.getPerfumeById(dto.getPerfumeId());
        } catch (FeignException.NotFound e) {
            logger.error("Perfume ID {} no existe en el catálogo", dto.getPerfumeId());
            throw new RuntimeException("El perfume indicado no existe en el sistema.");
        }

        try {
            sucursalClient.getSucursalById(dto.getSucursalId());
        } catch (FeignException.NotFound e) {
            logger.error("Sucursal ID {} no existe", dto.getSucursalId());
            throw new RuntimeException("La sucursal indicada no existe en el sistema.");
        }

        // 2. Lógica de Negocio: Sumar stock si ya existe, o crear registro nuevo
        Optional<Inventario> inventarioExistente = repository.findByPerfumeIdAndSucursalId(dto.getPerfumeId(), dto.getSucursalId());

        if (inventarioExistente.isPresent()) {
            Inventario inv = inventarioExistente.get();
            inv.setCantidad(inv.getCantidad() + dto.getCantidad());
            logger.info("Stock actualizado para Perfume ID: {}. Nueva cantidad: {}", dto.getPerfumeId(), inv.getCantidad());
            return repository.save(inv);
        } else {
            logger.info("Creando nuevo registro de stock para Perfume ID: {}", dto.getPerfumeId());
            Inventario nuevoInventario = new Inventario(dto.getPerfumeId(), dto.getSucursalId(), dto.getCantidad());
            return repository.save(nuevoInventario);
        }
    }

    public Inventario consultarStock(Long perfumeId, Long sucursalId) {
        return repository.findByPerfumeIdAndSucursalId(perfumeId, sucursalId)
                .orElseThrow(() -> new RuntimeException("No hay stock registrado para este perfume en esta sucursal."));
    }

    public List<Inventario> listarPorSucursal(Long sucursalId) {
        return repository.findBySucursalId(sucursalId);
    }

    public Inventario descontarStock(Long perfumeId, Long sucursalId, Integer cantidadADescontar) {
        Inventario inv = consultarStock(perfumeId, sucursalId);
        
        if (inv.getCantidad() < cantidadADescontar) {
            logger.error("Quiebre de stock: Solicitado {}, Disponible {}", cantidadADescontar, inv.getCantidad());
            throw new RuntimeException("Stock insuficiente para realizar la reserva o venta.");
        }
        
        inv.setCantidad(inv.getCantidad() - cantidadADescontar);
        logger.info("Stock descontado exitosamente. Restante: {}", inv.getCantidad());
        return repository.save(inv);
    }
}