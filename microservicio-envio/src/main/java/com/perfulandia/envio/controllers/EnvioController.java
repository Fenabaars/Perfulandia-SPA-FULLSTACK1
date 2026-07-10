package com.perfulandia.envio.controllers;

import com.perfulandia.envio.models.dtos.ActualizarEstadoDTO;
import com.perfulandia.envio.models.dtos.EnvioDTO;
import com.perfulandia.envio.models.entities.Envio;
import com.perfulandia.envio.models.entities.HistorialEnvio;
import com.perfulandia.envio.services.EnvioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/envios")
public class EnvioController {

    private final EnvioService service;

    public EnvioController(EnvioService service) {
        this.service = service;
    }

    // Listar todos los envíos
    @GetMapping
    public ResponseEntity<List<Envio>> obtenerTodos() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    // Obtener envío por ID
    @GetMapping("/{id}")
    public ResponseEntity<Envio> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // H22: Listar envíos por estado (ej: /api/envios/estado/PREPARACION)
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Envio>> obtenerPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.obtenerPorEstado(estado));
    }

    // H23: Listar envíos del cliente para rastreo
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Envio>> obtenerPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(service.obtenerPorCliente(clienteId));
    }

    // H23: Rastrear envío por código de seguimiento
    @GetMapping("/rastreo/{codigo}")
    public ResponseEntity<Envio> obtenerPorCodigoSeguimiento(@PathVariable String codigo) {
        return ResponseEntity.ok(service.obtenerPorCodigoSeguimiento(codigo));
    }

    // Obtener envío de un pedido
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<Envio> obtenerPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(service.obtenerPorPedido(pedidoId));
    }

    // H22: Listar envíos por sucursal/bodega
    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<List<Envio>> obtenerPorSucursal(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(service.obtenerPorSucursal(sucursalId));
    }

    // H22: Crear nuevo envío para un pedido confirmado
    @PostMapping
    public ResponseEntity<Envio> crearEnvio(@Valid @RequestBody EnvioDTO dto) {
        return new ResponseEntity<>(service.crearEnvio(dto), HttpStatus.CREATED);
    }

    // H23: Actualizar estado del envío (Preparación -> Tránsito -> Entregado)
    @PutMapping("/{id}/estado")
    public ResponseEntity<Envio> actualizarEstado(@PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoDTO dto) {
        return ResponseEntity.ok(service.actualizarEstado(id, dto));
    }

    // H23: Ver historial de rastreo de un envío
    @GetMapping("/{id}/historial")
    public ResponseEntity<List<HistorialEnvio>> obtenerHistorial(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerHistorial(id));
    }

    // Eliminar envío
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEnvio(@PathVariable Long id) {
        service.eliminarEnvio(id);
        return ResponseEntity.noContent().build();
    }
}
