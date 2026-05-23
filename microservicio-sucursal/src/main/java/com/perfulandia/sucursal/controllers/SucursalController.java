package com.perfulandia.sucursal.controllers;

import com.perfulandia.sucursal.models.dtos.SucursalDTO;
import com.perfulandia.sucursal.models.entities.Sucursal;
import com.perfulandia.sucursal.services.SucursalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
public class SucursalController {

    private final SucursalService service;

    public SucursalController(SucursalService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Sucursal>> obtenerTodas() {
        return ResponseEntity.ok(service.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sucursal> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Sucursal>> buscarPorComuna(@RequestParam String comuna) {
        return ResponseEntity.ok(service.buscarPorComuna(comuna));
    }

    @PostMapping
    public ResponseEntity<Sucursal> registrar(@Valid @RequestBody SucursalDTO dto) {
        return new ResponseEntity<>(service.registrarSucursal(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sucursal> actualizar(@PathVariable Long id, @Valid @RequestBody SucursalDTO dto) {
        return ResponseEntity.ok(service.actualizarSucursal(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarSucursal(id);
        return ResponseEntity.noContent().build();
    }
}