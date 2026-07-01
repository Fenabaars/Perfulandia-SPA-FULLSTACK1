package com.perfulandia.resenas.controller;

import com.perfulandia.resenas.entity.Resena;
import com.perfulandia.resenas.repository.ResenaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
public class ResenaController {

    @Autowired
    private com.perfulandia.resenas.service.ResenaService resenaService;

    // 1. Como Cliente, quiero dejar una calificación (de 1 a 5 estrellas) y un comentario sobre un perfume que compré.
    @PostMapping
    public ResponseEntity<?> createResena(@RequestBody Resena resena) {
        Object resultado = resenaService.createResena(resena);
        if (resultado instanceof String) {
            return new ResponseEntity<>(resultado, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }

    // 2. Como Usuario Web, quiero ver todas las reseñas asociadas a un producto específico para decidir mi compra.
    @GetMapping("/product/{productoId}")
    public ResponseEntity<List<Resena>> getResenasByProductoId(@PathVariable Long productoId) {
        return new ResponseEntity<>(resenaService.getResenasByProductoId(productoId), HttpStatus.OK);
    }

    // 3. Como Administrador, quiero eliminar una reseña si contiene lenguaje inapropiado.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResena(@PathVariable Long id) {
        if (resenaService.deleteResena(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // GET general (Devuelve todas las reseñas del sistema)
    @GetMapping
    public ResponseEntity<List<Resena>> getAllResenas() {
        return new ResponseEntity<>(resenaService.getAllResenas(), HttpStatus.OK);
    }
}
