package com.perfulandia.envios.controllers;

import com.perfulandia.envios.models.entities.Envio;
import com.perfulandia.envios.services.EnvioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;

@RestController
@RequestMapping("/api/envios")
@Tag(name = "Envíos", description = "API para la gestión logística de los despachos")
public class EnvioController {

    private final EnvioService service;

    public EnvioController(EnvioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Envio>> obtenerTodos() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Envio> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }
    
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<Envio> obtenerPorPedidoId(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(service.obtenerPorPedidoId(pedidoId));
    }

    @PostMapping
    public ResponseEntity<Envio> registrarEnvio(@RequestBody Envio envio) {
        return new ResponseEntity<>(service.registrarEnvio(envio), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Envio> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        return ResponseEntity.ok(service.actualizarEstado(id, estado));
    }
}
