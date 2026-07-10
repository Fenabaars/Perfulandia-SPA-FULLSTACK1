package com.perfulandia.microservicio_catalogo.controller;


import com.perfulandia.microservicio_catalogo.model.Perfume;
import com.perfulandia.microservicio_catalogo.service.PerfumeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import com.perfulandia.microservicio_catalogo.dto.PerfumeDTO;

@Slf4j
@RestController
@RequestMapping("/api/catalog")
public class PerfumeController {


    private final PerfumeService perfumeService;

    public PerfumeController(PerfumeService perfumeService) {
        this.perfumeService = perfumeService;
    }

    // 1. Obtener todos los perfumes (GET)
    @GetMapping
    public List<Perfume> getAllPerfumes() {
        log.info("Petición REST recibida en PerfumeController");
        return perfumeService.getAllPerfumes();
    }

    // 2. Obtener un perfume específico por su ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Perfume> getPerfumeById(@PathVariable Long id) {
        log.info("Petición REST recibida en PerfumeController");
        Optional<Perfume> perfume = perfumeService.getPerfumeById(id);
        return perfume.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 3. Crear un nuevo perfume (POST)
    @PostMapping
    public ResponseEntity<Perfume> createPerfume(@Valid @RequestBody PerfumeDTO perfumeDTO) {
        log.info("Petición REST recibida en PerfumeController");
        log.info("Creando nuevo perfume: {}", perfumeDTO.getNombre());
        Perfume perfume = new Perfume();
        BeanUtils.copyProperties(perfumeDTO, perfume);
        Perfume nuevoPerfume = perfumeService.createPerfume(perfume);
        return new ResponseEntity<>(nuevoPerfume, HttpStatus.CREATED);
    }

    // 4. Actualizar un perfume existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Perfume> updatePerfume(@PathVariable Long id, @Valid @RequestBody PerfumeDTO perfumeDTO) {
        log.info("Petición REST recibida en PerfumeController");
        log.info("Actualizando perfume con id: {}", id);
        Perfume perfumeDetalles = new Perfume();
        BeanUtils.copyProperties(perfumeDTO, perfumeDetalles);
        Optional<Perfume> perfumeActualizado = perfumeService.updatePerfume(id, perfumeDetalles);
        return perfumeActualizado.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 5. Eliminar un perfume (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerfume(@PathVariable Long id) {
        log.info("Petición REST recibida en PerfumeController");
        if (perfumeService.deletePerfume(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
