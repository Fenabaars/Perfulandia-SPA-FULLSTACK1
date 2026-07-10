package com.perfulandia.carrito.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.perfulandia.carrito.models.dtos.VentaDTO;
import com.perfulandia.carrito.models.entities.Venta;
import com.perfulandia.carrito.services.VentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private static final Logger log = LoggerFactory.getLogger(VentaController.class);

    private final VentaService service;

    public VentaController(VentaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Venta>> obtenerTodas() {
        log.info("Petición REST recibida en VentaController");
        return ResponseEntity.ok(service.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerPorId(@PathVariable Long id) {
        log.info("Petición REST recibida en VentaController");
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // Ventas registradas por sucursal
    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<List<Venta>> obtenerPorSucursal(@PathVariable Long sucursalId) {
        log.info("Petición REST recibida en VentaController");
        return ResponseEntity.ok(service.obtenerPorSucursal(sucursalId));
    }

    // H18: Registrar venta física en la sucursal (descuenta inventario implícitamente)
    @PostMapping
    public ResponseEntity<Venta> registrarVenta(@Valid @RequestBody VentaDTO dto) {
        log.info("Petición REST recibida en VentaController");
        return new ResponseEntity<>(service.registrarVenta(dto), HttpStatus.CREATED);
    }

    // Eliminar registro de venta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Long id) {
        log.info("Petición REST recibida en VentaController");
        service.eliminarVenta(id);
        return ResponseEntity.noContent().build();
    }
}
