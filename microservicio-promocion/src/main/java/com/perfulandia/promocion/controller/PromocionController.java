package com.perfulandia.promocion.controller;

import com.perfulandia.promocion.entity.Promocion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import com.perfulandia.promocion.dto.PromocionDTO;

@Slf4j
@RestController
@RequestMapping("/api/promotions")
@SuppressWarnings("null")
public class PromocionController {

    private final com.perfulandia.promocion.service.PromocionService promocionService;

    public PromocionController(com.perfulandia.promocion.service.PromocionService promocionService) {
        this.promocionService = promocionService;
    }

    @GetMapping
    public ResponseEntity<List<Promocion>> getAllPromotions() {
        return ResponseEntity.ok(promocionService.getAllPromotions());
    }

    @PostMapping
    public ResponseEntity<Promocion> createPromotion(@Valid @RequestBody PromocionDTO promocionDTO) {
        log.info("Creando promocion: {}", promocionDTO.getCodigo());
        Promocion promocion = new Promocion();
        BeanUtils.copyProperties(promocionDTO, promocion);
        Promocion savedPromocion = promocionService.createPromotion(promocion);
        return new ResponseEntity<>(savedPromocion, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Promocion> updatePromotion(@PathVariable Long id, @Valid @RequestBody PromocionDTO promocionDTO) {
        log.info("Actualizando promocion id: {}", id);
        Promocion promocionDetails = new Promocion();
        BeanUtils.copyProperties(promocionDTO, promocionDetails);
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
