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
    private PromocionRepository promocionRepository;

    @GetMapping
    public ResponseEntity<List<Promocion>> getAllPromotions() {
        return ResponseEntity.ok(promocionRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Promocion> createPromotion(@RequestBody Promocion promocion) {
        Promocion savedPromocion = promocionRepository.save(promocion);
        return new ResponseEntity<>(savedPromocion, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Promocion> updatePromotion(@PathVariable Long id, @RequestBody Promocion promocionDetails) {
        return promocionRepository.findById(id)
                .map(promocion -> {
                    promocion.setCodigo(promocionDetails.getCodigo());
                    promocion.setTipoDescuento(promocionDetails.getTipoDescuento());
                    promocion.setValorDescuento(promocionDetails.getValorDescuento());
                    promocion.setFechaInicio(promocionDetails.getFechaInicio());
                    promocion.setFechaFin(promocionDetails.getFechaFin());
                    promocion.setActivo(promocionDetails.getActivo());
                    Promocion updatedPromocion = promocionRepository.save(promocion);
                    return ResponseEntity.ok(updatedPromocion);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        return promocionRepository.findById(id)
                .map(promocion -> {
                    promocionRepository.delete(promocion);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/validate/{codigo}")
    public ResponseEntity<?> validatePromotion(@PathVariable String codigo) {
        Optional<Promocion> promocionOpt = promocionRepository.findByCodigo(codigo);
        
        if (promocionOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El código promocional no existe.");
        }
        
        Promocion promocion = promocionOpt.get();
        
        if (!Boolean.TRUE.equals(promocion.getActivo())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El código promocional está inactivo.");
        }
        
        LocalDate hoy = LocalDate.now();
        if (hoy.isBefore(promocion.getFechaInicio()) || hoy.isAfter(promocion.getFechaFin())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El código promocional está vencido o aún no es válido.");
        }
        
        return ResponseEntity.ok(promocion);
    }
}
