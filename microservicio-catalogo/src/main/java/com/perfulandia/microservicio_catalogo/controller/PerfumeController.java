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
    private PerfumeRepository perfumeRepository;

    // 1. Obtener todos los perfumes (GET)
    @GetMapping
    public List<Perfume> getAllPerfumes() {
        return perfumeRepository.findAll();
    }

    // 2. Obtener un perfume específico por su ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Perfume> getPerfumeById(@PathVariable Long id) {
        Optional<Perfume> perfume = perfumeRepository.findById(id);
        if (perfume.isPresent()) {
            return new ResponseEntity<>(perfume.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 3. Crear un nuevo perfume (POST)
    @PostMapping
    public ResponseEntity<Perfume> createPerfume(@RequestBody Perfume perfume) {
        Perfume nuevoPerfume = perfumeRepository.save(perfume);
        return new ResponseEntity<>(nuevoPerfume, HttpStatus.CREATED);
    }

    // 4. Actualizar un perfume existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Perfume> updatePerfume(@PathVariable Long id, @RequestBody Perfume perfumeDetalles) {
        Optional<Perfume> perfumeOptional = perfumeRepository.findById(id);

        if (perfumeOptional.isPresent()) {
            Perfume perfume = perfumeOptional.get();
            perfume.setNombre(perfumeDetalles.getNombre());
            perfume.setMarca(perfumeDetalles.getMarca());
            perfume.setCategoria(perfumeDetalles.getCategoria());
            perfume.setDescripcion(perfumeDetalles.getDescripcion());
            perfume.setPrecio(perfumeDetalles.getPrecio());
            perfume.setNotasOlfativas(perfumeDetalles.getNotasOlfativas());
            
            Perfume perfumeActualizado = perfumeRepository.save(perfume);
            return new ResponseEntity<>(perfumeActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 5. Eliminar un perfume (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerfume(@PathVariable Long id) {
        if (perfumeRepository.existsById(id)) {
            perfumeRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
