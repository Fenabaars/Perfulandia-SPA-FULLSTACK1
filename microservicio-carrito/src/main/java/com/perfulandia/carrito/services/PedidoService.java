package com.perfulandia.carrito.services;

import com.perfulandia.carrito.models.dtos.DetallePedidoDTO;
import com.perfulandia.carrito.models.dtos.PedidoDTO;
import com.perfulandia.carrito.models.entities.DetallePedido;
import com.perfulandia.carrito.models.entities.Pedido;
import com.perfulandia.carrito.repositories.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);
    private final PedidoRepository repository;

    public PedidoService(PedidoRepository repository) {
        this.repository = repository;
    }

    public List<Pedido> obtenerTodos() {
        logger.info("Obteniendo todos los pedidos");
        return repository.findAll();
    }

    public Pedido obtenerPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> {
            logger.error("Pedido no encontrado con ID: {}", id);
            return new RuntimeException("Pedido no encontrado");
        });
    }

    // H19: Historial de compras del cliente
    public List<Pedido> obtenerHistorialPorUsuario(Long usuarioId) {
        logger.info("Obteniendo historial de compras del usuario ID: {}", usuarioId);
        return repository.findByUsuarioIdOrderByFechaPedidoDesc(usuarioId);
    }

    // H17: Confirmar pedido web con método de pago y envío
    @Transactional
    public Pedido confirmarPedido(PedidoDTO dto) {
        logger.info("Confirmando nuevo pedido para usuario ID: {}", dto.getUsuarioId());

        Pedido pedido = new Pedido();
        pedido.setUsuarioId(dto.getUsuarioId());
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado("PENDIENTE");
        pedido.setMetodoPago(dto.getMetodoPago());
        pedido.setMetodoEnvio(dto.getMetodoEnvio());
        pedido.setDireccionEnvio(dto.getDireccionEnvio());

        List<DetallePedido> detalles = new ArrayList<>();
        double total = 0.0;

        for (DetallePedidoDTO detalleDTO : dto.getDetalles()) {
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setPerfumeId(detalleDTO.getPerfumeId());
            detalle.setNombrePerfume(detalleDTO.getNombrePerfume());
            detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
            detalle.setCantidad(detalleDTO.getCantidad());
            double subtotal = detalleDTO.getPrecioUnitario() * detalleDTO.getCantidad();
            detalle.setSubtotal(subtotal);
            total += subtotal;
            detalles.add(detalle);
        }

        pedido.setDetalles(detalles);
        pedido.setTotal(total);

        Pedido guardado = repository.save(pedido);
        logger.info("Pedido ID {} confirmado. Total: ${}", guardado.getId(), total);
        return guardado;
    }

    public Pedido actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedido = obtenerPorId(id);
        logger.info("Actualizando estado del pedido ID {} a: {}", id, nuevoEstado);
        pedido.setEstado(nuevoEstado);
        return repository.save(pedido);
    }

    public void cancelarPedido(Long id) {
        Pedido pedido = obtenerPorId(id);
        if ("ENTREGADO".equals(pedido.getEstado())) {
            throw new RuntimeException("No se puede cancelar un pedido ya entregado");
        }
        pedido.setEstado("CANCELADO");
        repository.save(pedido);
        logger.info("Pedido ID {} cancelado", id);
    }
}
