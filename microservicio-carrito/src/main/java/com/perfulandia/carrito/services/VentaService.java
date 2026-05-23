package com.perfulandia.carrito.services;

import com.perfulandia.carrito.models.dtos.DetalleVentaDTO;
import com.perfulandia.carrito.models.dtos.VentaDTO;
import com.perfulandia.carrito.models.entities.DetalleVenta;
import com.perfulandia.carrito.models.entities.Venta;
import com.perfulandia.carrito.repositories.VentaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VentaService {

    private static final Logger logger = LoggerFactory.getLogger(VentaService.class);
    private final VentaRepository repository;

    public VentaService(VentaRepository repository) {
        this.repository = repository;
    }

    public List<Venta> obtenerTodas() {
        logger.info("Obteniendo todas las ventas físicas");
        return repository.findAll();
    }

    public Venta obtenerPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> {
            logger.error("Venta no encontrada con ID: {}", id);
            return new RuntimeException("Venta no encontrada");
        });
    }

    public List<Venta> obtenerPorSucursal(Long sucursalId) {
        logger.info("Obteniendo ventas de la sucursal ID: {}", sucursalId);
        return repository.findBySucursalIdOrderByFechaVentaDesc(sucursalId);
    }

    // H18: Registrar nueva venta física en sucursal
    @Transactional
    public Venta registrarVenta(VentaDTO dto) {
        logger.info("Registrando venta física en sucursal ID: {} por empleado ID: {}",
                dto.getSucursalId(), dto.getEmpleadoId());

        Venta venta = new Venta();
        venta.setSucursalId(dto.getSucursalId());
        venta.setEmpleadoId(dto.getEmpleadoId());
        venta.setFechaVenta(LocalDateTime.now());
        venta.setMetodoPago(dto.getMetodoPago());

        List<DetalleVenta> detalles = new ArrayList<>();
        double total = 0.0;

        for (DetalleVentaDTO detalleDTO : dto.getDetalles()) {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setPerfumeId(detalleDTO.getPerfumeId());
            detalle.setNombrePerfume(detalleDTO.getNombrePerfume());
            detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
            detalle.setCantidad(detalleDTO.getCantidad());
            double subtotal = detalleDTO.getPrecioUnitario() * detalleDTO.getCantidad();
            detalle.setSubtotal(subtotal);
            total += subtotal;
            detalles.add(detalle);
        }

        venta.setDetalles(detalles);
        venta.setTotal(total);

        Venta guardada = repository.save(venta);
        logger.info("Venta física registrada con ID: {}. Total: ${}", guardada.getId(), total);
        return guardada;
    }

    public void eliminarVenta(Long id) {
        Venta venta = obtenerPorId(id);
        repository.delete(venta);
        logger.info("Venta ID {} eliminada del sistema", id);
    }
}
