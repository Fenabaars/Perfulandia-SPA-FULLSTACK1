package com.perfulandia.facturacion.services;

import com.perfulandia.facturacion.models.dtos.BoletaDTO;
import com.perfulandia.facturacion.models.dtos.DetalleBoletaDTO;
import com.perfulandia.facturacion.models.entities.Boleta;
import com.perfulandia.facturacion.models.entities.DetalleBoleta;
import com.perfulandia.facturacion.repositories.BoletaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class BoletaService {

    private static final Logger logger = LoggerFactory.getLogger(BoletaService.class);
    private static final double IVA_PORCENTAJE = 0.19;

    private final BoletaRepository repository;

    public BoletaService(BoletaRepository repository) {
        this.repository = repository;
    }

    // ==================== CONSULTAS ====================

    public List<Boleta> obtenerTodas() {
        logger.info("Obteniendo todas las boletas/facturas");
        return repository.findAll();
    }

    public Boleta obtenerPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> {
            logger.error("Boleta no encontrada con ID: {}", id);
            return new RuntimeException("Boleta no encontrada");
        });
    }

    // H21: Listar boletas del cliente para descarga desde su perfil
    public List<Boleta> obtenerPorCliente(Long clienteId) {
        logger.info("Obteniendo boletas del cliente ID: {}", clienteId);
        return repository.findByClienteIdOrderByFechaEmisionDesc(clienteId);
    }

    public Boleta obtenerPorPedido(Long pedidoId) {
        return repository.findByPedidoId(pedidoId).orElseThrow(() -> {
            logger.error("Boleta no encontrada para pedido ID: {}", pedidoId);
            return new RuntimeException("Boleta no encontrada para el pedido especificado");
        });
    }

    public Boleta obtenerPorVenta(Long ventaId) {
        return repository.findByVentaId(ventaId).orElseThrow(() -> {
            logger.error("Boleta no encontrada para venta ID: {}", ventaId);
            return new RuntimeException("Boleta no encontrada para la venta especificada");
        });
    }

    // ==================== H20: GENERACIÓN DE BOLETA/FACTURA ====================

    @Transactional
    public Boleta generarBoleta(BoletaDTO dto) {
        logger.info("Generando {} para cliente: {} (ID: {})",
                dto.getTipo(), dto.getClienteNombre(), dto.getClienteId());

        // Validar que se proporcione pedidoId o ventaId
        if (dto.getPedidoId() == null && dto.getVentaId() == null) {
            throw new RuntimeException("Debe especificar un pedidoId o ventaId asociado");
        }

        // Validar que para FACTURA se requiere RUT
        if ("FACTURA".equalsIgnoreCase(dto.getTipo()) &&
                (dto.getRutCliente() == null || dto.getRutCliente().isBlank())) {
            throw new RuntimeException("El RUT del cliente es obligatorio para emitir una FACTURA");
        }

        Boleta boleta = new Boleta();
        boleta.setNumeroBoleta(generarNumeroBoleta(dto.getTipo()));
        boleta.setTipo(dto.getTipo().toUpperCase());
        boleta.setPedidoId(dto.getPedidoId());
        boleta.setVentaId(dto.getVentaId());
        boleta.setClienteId(dto.getClienteId());
        boleta.setClienteNombre(dto.getClienteNombre());
        boleta.setClienteEmail(dto.getClienteEmail());
        boleta.setRutCliente(dto.getRutCliente());
        boleta.setFechaEmision(LocalDateTime.now());
        boleta.setEstado("EMITIDA");

        // Calcular detalles y totales
        List<DetalleBoleta> detalles = new ArrayList<>();
        double subtotalNeto = 0.0;

        for (DetalleBoletaDTO detalleDTO : dto.getDetalles()) {
            DetalleBoleta detalle = new DetalleBoleta();
            detalle.setBoleta(boleta);
            detalle.setPerfumeId(detalleDTO.getPerfumeId());
            detalle.setDescripcion(detalleDTO.getDescripcion());
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
            double lineaSubtotal = detalleDTO.getPrecioUnitario() * detalleDTO.getCantidad();
            detalle.setSubtotal(lineaSubtotal);
            subtotalNeto += lineaSubtotal;
            detalles.add(detalle);
        }

        boleta.setDetalles(detalles);
        boleta.setSubtotal(subtotalNeto);

        // Calcular IVA (19%)
        double impuesto = Math.round(subtotalNeto * IVA_PORCENTAJE * 100.0) / 100.0;
        boleta.setImpuesto(impuesto);
        boleta.setTotal(subtotalNeto + impuesto);

        Boleta guardada = repository.save(boleta);
        logger.info("{} generada exitosamente - Número: {} | Subtotal: ${} | IVA: ${} | Total: ${}",
                guardada.getTipo(), guardada.getNumeroBoleta(),
                guardada.getSubtotal(), guardada.getImpuesto(), guardada.getTotal());

        return guardada;
    }

    // ==================== H21: ENVÍO DE DOCUMENTO TRIBUTARIO ====================

    @Transactional
    public Boleta enviarBoleta(Long boletaId) {
        Boleta boleta = obtenerPorId(boletaId);

        if ("ANULADA".equals(boleta.getEstado())) {
            throw new RuntimeException("No se puede enviar una boleta anulada");
        }

        // Simular envío por correo electrónico
        logger.info("=== SIMULACIÓN DE ENVÍO DE CORREO ===");
        logger.info("Destinatario: {}", boleta.getClienteEmail());
        logger.info("Asunto: Su {} N° {} - Perfulandia", boleta.getTipo(), boleta.getNumeroBoleta());
        logger.info("Contenido: Estimado/a {}, adjuntamos su {} por un total de ${}",
                boleta.getClienteNombre(), boleta.getTipo().toLowerCase(), boleta.getTotal());
        logger.info("=== FIN SIMULACIÓN ===");

        boleta.setEstado("ENVIADA");
        Boleta actualizada = repository.save(boleta);
        logger.info("{} N° {} enviada exitosamente al correo: {}",
                actualizada.getTipo(), actualizada.getNumeroBoleta(), actualizada.getClienteEmail());

        return actualizada;
    }

    // ==================== ELIMINACIÓN ====================

    @Transactional
    public void anularBoleta(Long id) {
        Boleta boleta = obtenerPorId(id);
        boleta.setEstado("ANULADA");
        repository.save(boleta);
        logger.info("{} N° {} anulada del sistema", boleta.getTipo(), boleta.getNumeroBoleta());
    }

    public void eliminarBoleta(Long id) {
        Boleta boleta = obtenerPorId(id);
        repository.delete(boleta);
        logger.info("Boleta ID {} eliminada del sistema", id);
    }

    // ==================== UTILIDADES ====================

    /**
     * Genera un número de boleta único con formato: BOL-YYYYMMDD-XXXX o FAC-YYYYMMDD-XXXX
     */
    private String generarNumeroBoleta(String tipo) {
        String prefijo = "FACTURA".equalsIgnoreCase(tipo) ? "FAC" : "BOL";
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = repository.count() + 1;
        return String.format("%s-%s-%04d", prefijo, fecha, count);
    }
}
