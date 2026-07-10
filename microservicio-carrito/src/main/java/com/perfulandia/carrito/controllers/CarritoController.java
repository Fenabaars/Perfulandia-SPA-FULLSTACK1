package com.perfulandia.carrito.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.perfulandia.carrito.models.dtos.CarritoItemDTO;
import com.perfulandia.carrito.models.entities.CarritoItem;
import com.perfulandia.carrito.services.CarritoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    private static final Logger log = LoggerFactory.getLogger(CarritoController.class);

    private final CarritoService service;

    public CarritoController(CarritoService service) {
        this.service = service;
    }

    // H16: Ver todos los ítems del carrito de un usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CarritoItem>> obtenerCarrito(@PathVariable Long usuarioId) {
        log.info("Petición REST recibida en CarritoController");
        return ResponseEntity.ok(service.obtenerCarritoPorUsuario(usuarioId));
    }

    // H16: Agregar un producto al carrito
    @PostMapping
    public ResponseEntity<CarritoItem> agregarItem(@Valid @RequestBody CarritoItemDTO dto) {
        log.info("Petición REST recibida en CarritoController");
        return new ResponseEntity<>(service.agregarItem(dto), HttpStatus.CREATED);
    }

    // H16: Actualizar la cantidad de un ítem del carrito
    @PutMapping("/{id}")
    public ResponseEntity<CarritoItem> actualizarCantidad(
            @PathVariable Long id,
            @RequestParam Integer cantidad) {
        log.info("Petición REST recibida en CarritoController");
        return ResponseEntity.ok(service.actualizarCantidad(id, cantidad));
    }

    // H16: Quitar un producto del carrito
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarItem(@PathVariable Long id) {
        log.info("Petición REST recibida en CarritoController");
        service.eliminarItem(id);
        return ResponseEntity.noContent().build();
    }

    // H16: Vaciar todo el carrito (se llama al confirmar pedido)
    @DeleteMapping("/usuario/{usuarioId}/vaciar")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long usuarioId) {
        log.info("Petición REST recibida en CarritoController");
        service.vaciarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
