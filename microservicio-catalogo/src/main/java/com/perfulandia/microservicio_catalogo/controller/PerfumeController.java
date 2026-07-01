package com.perfulandia.microservicio_catalogo.controller;

import com.perfulandia.microservicio_catalogo.model.Perfume;
import com.perfulandia.microservicio_catalogo.repository.PerfumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/catalog")
public class PerfumeController {

    @Autowired
    private com.perfulandia.microservicio_catalogo.service.PerfumeService perfumeService;

    // 1. Obtener todos los perfumes (GET)
    @GetMapping
    public List<Perfume> getAllPerfumes() {
        return perfumeService.getAllPerfumes();
    }

    // 2. Obtener un perfume específico por su ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Perfume> getPerfumeById(@PathVariable Long id) {
        Optional<Perfume> perfume = perfumeService.getPerfumeById(id);
        return perfume.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 3. Crear un nuevo perfume (POST)
    @PostMapping
    public ResponseEntity<Perfume> createPerfume(@RequestBody Perfume perfume) {
        Perfume nuevoPerfume = perfumeService.createPerfume(perfume);
        return new ResponseEntity<>(nuevoPerfume, HttpStatus.CREATED);
    }

    // 4. Actualizar un perfume existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Perfume> updatePerfume(@PathVariable Long id, @RequestBody Perfume perfumeDetalles) {
        Optional<Perfume> perfumeActualizado = perfumeService.updatePerfume(id, perfumeDetalles);
        return perfumeActualizado.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 5. Eliminar un perfume (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerfume(@PathVariable Long id) {
        if (perfumeService.deletePerfume(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
