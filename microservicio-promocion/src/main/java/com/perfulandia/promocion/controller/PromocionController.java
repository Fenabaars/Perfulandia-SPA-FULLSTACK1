package com.perfulandia.promocion.controller;

import com.perfulandia.promocion.entity.Promocion;
import com.perfulandia.promocion.repository.PromocionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/promotions")
public class PromocionController {

    @Autowired
    private com.perfulandia.promocion.service.PromocionService promocionService;

    @GetMapping
    public ResponseEntity<List<Promocion>> getAllPromotions() {
        return ResponseEntity.ok(promocionService.getAllPromotions());
    }

    @PostMapping
    public ResponseEntity<Promocion> createPromotion(@RequestBody Promocion promocion) {
        Promocion savedPromocion = promocionService.createPromotion(promocion);
        return new ResponseEntity<>(savedPromocion, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Promocion> updatePromotion(@PathVariable Long id, @RequestBody Promocion promocionDetails) {
        return promocionService.updatePromotion(id, promocionDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        if (promocionService.deletePromotion(id)) {
            return ResponseEntity.ok().<Void>build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/validate/{codigo}")
    public ResponseEntity<?> validatePromotion(@PathVariable String codigo) {
        Object validacion = promocionService.validatePromotion(codigo);
        if (validacion instanceof String) {
            String msj = (String) validacion;
            if (msj.contains("no existe")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msj);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msj);
        }
        return ResponseEntity.ok(validacion);
    }
}
