package com.perfulandia.facturacion.controllers;

import com.perfulandia.facturacion.models.dtos.BoletaDTO;
import com.perfulandia.facturacion.models.entities.Boleta;
import com.perfulandia.facturacion.services.BoletaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boletas")
public class BoletaController {

    private final BoletaService service;

    public BoletaController(BoletaService service) {
        this.service = service;
    }

    // Listar todas las boletas/facturas
    @GetMapping
    public ResponseEntity<List<Boleta>> obtenerTodas() {
        return ResponseEntity.ok(service.obtenerTodas());
    }

    // Obtener boleta por ID (H21: descarga desde perfil)
    @GetMapping("/{id}")
    public ResponseEntity<Boleta> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // H21: Listar boletas del cliente para descarga desde su perfil
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Boleta>> obtenerPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(service.obtenerPorCliente(clienteId));
    }

    // H20: Obtener boleta asociada a un pedido web
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<Boleta> obtenerPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(service.obtenerPorPedido(pedidoId));
    }

    // H20: Obtener boleta asociada a una venta física
    @GetMapping("/venta/{ventaId}")
    public ResponseEntity<Boleta> obtenerPorVenta(@PathVariable Long ventaId) {
        return ResponseEntity.ok(service.obtenerPorVenta(ventaId));
    }

    // H20: Generar nueva boleta/factura al momento del pago
    @PostMapping
    public ResponseEntity<Boleta> generarBoleta(@Valid @RequestBody BoletaDTO dto) {
        return new ResponseEntity<>(service.generarBoleta(dto), HttpStatus.CREATED);
    }

    // H21: Enviar boleta por correo electrónico (simulado)
    @PutMapping("/{id}/enviar")
    public ResponseEntity<Boleta> enviarBoleta(@PathVariable Long id) {
        return ResponseEntity.ok(service.enviarBoleta(id));
    }

    // Anular boleta (cambia estado a ANULADA)
    @PutMapping("/{id}/anular")
    public ResponseEntity<Boleta> anularBoleta(@PathVariable Long id) {
        service.anularBoleta(id);
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // Eliminar boleta del sistema
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarBoleta(@PathVariable Long id) {
        service.eliminarBoleta(id);
        return ResponseEntity.noContent().build();
    }
}
