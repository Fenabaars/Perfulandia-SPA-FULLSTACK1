package com.perfulandia.carrito.controllers;

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

    private final VentaService service;

    public VentaController(VentaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Venta>> obtenerTodas() {
        return ResponseEntity.ok(service.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // Ventas registradas por sucursal
    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<List<Venta>> obtenerPorSucursal(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(service.obtenerPorSucursal(sucursalId));
    }

    // H18: Registrar venta física en la sucursal (descuenta inventario implícitamente)
    @PostMapping
    public ResponseEntity<Venta> registrarVenta(@Valid @RequestBody VentaDTO dto) {
        return new ResponseEntity<>(service.registrarVenta(dto), HttpStatus.CREATED);
    }

    // Eliminar registro de venta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Long id) {
        service.eliminarVenta(id);
        return ResponseEntity.noContent().build();
    }
}
