package com.perfulandia.proveedor.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.perfulandia.proveedor.models.dtos.ProveedorDTO;
import com.perfulandia.proveedor.models.entities.Proveedor;
import com.perfulandia.proveedor.services.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private static final Logger log = LoggerFactory.getLogger(ProveedorController.class);

    private final ProveedorService service;

    public ProveedorController(ProveedorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Proveedor>> obtenerTodos() {
        log.info("Petición REST recibida en ProveedorController");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> obtenerPorId(@PathVariable Long id) {
        log.info("Petición REST recibida en ProveedorController");
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<Proveedor> obtenerPorRut(@RequestParam String rut) {
        log.info("Petición REST recibida en ProveedorController");
        return ResponseEntity.ok(service.obtenerPorRut(rut));
    }

    @PostMapping
    public ResponseEntity<Proveedor> registrar(@Valid @RequestBody ProveedorDTO dto) {
        log.info("Petición REST recibida en ProveedorController");
        return new ResponseEntity<>(service.registrarProveedor(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizar(@PathVariable Long id, @Valid @RequestBody ProveedorDTO dto) {
        log.info("Petición REST recibida en ProveedorController");
        return ResponseEntity.ok(service.actualizarProveedor(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Petición REST recibida en ProveedorController");
        service.eliminarProveedor(id);
        return ResponseEntity.noContent().build();
    }
}