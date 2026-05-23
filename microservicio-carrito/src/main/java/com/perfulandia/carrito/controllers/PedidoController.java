package com.perfulandia.carrito.controllers;

import com.perfulandia.carrito.models.dtos.PedidoDTO;
import com.perfulandia.carrito.models.entities.Pedido;
import com.perfulandia.carrito.services.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> obtenerTodos() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // H19: Historial de compras del cliente (pedidos web + estado actual)
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pedido>> obtenerHistorial(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.obtenerHistorialPorUsuario(usuarioId));
    }

    // H17: Confirmar pedido web con método de pago y envío
    @PostMapping
    public ResponseEntity<Pedido> confirmarPedido(@Valid @RequestBody PedidoDTO dto) {
        return new ResponseEntity<>(service.confirmarPedido(dto), HttpStatus.CREATED);
    }

    // Actualizar estado del pedido
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        return ResponseEntity.ok(service.actualizarEstado(id, estado));
    }

    // Cancelar pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarPedido(@PathVariable Long id) {
        service.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
