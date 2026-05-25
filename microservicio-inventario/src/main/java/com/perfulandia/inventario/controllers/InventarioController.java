package com.perfulandia.inventario.controllers;

import com.perfulandia.inventario.models.dtos.InventarioDTO;
import com.perfulandia.inventario.models.entities.Inventario;
import com.perfulandia.inventario.services.InventarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private final InventarioService service;

    public InventarioController(InventarioService service) {
        this.service = service;
    }

    @PostMapping("/ingreso")
    public ResponseEntity<Inventario> ingresarStock(@Valid @RequestBody InventarioDTO dto) {
        return new ResponseEntity<>(service.ingresarStock(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Inventario>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/consulta")
    public ResponseEntity<Inventario> consultarStock(@RequestParam Long perfumeId, @RequestParam Long sucursalId) {
        return ResponseEntity.ok(service.consultarStock(perfumeId, sucursalId));
    }

    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<List<Inventario>> listarPorSucursal(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(service.listarPorSucursal(sucursalId));
    }

    @PatchMapping("/descontar")
    public ResponseEntity<Inventario> descontarStock(@RequestParam Long perfumeId, 
                                                     @RequestParam Long sucursalId, 
                                                     @RequestParam Integer cantidad) {
        return ResponseEntity.ok(service.descontarStock(perfumeId, sucursalId, cantidad));
    }
}