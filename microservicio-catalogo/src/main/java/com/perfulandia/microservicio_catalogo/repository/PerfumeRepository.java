package com.perfulandia.microservicio_catalogo.repository;

import com.perfulandia.microservicio_catalogo.model.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfumeRepository extends JpaRepository<Perfume, Long> {
    // JpaRepository ya incluye por defecto todos los métodos para el CRUD
    // (save, findAll, findById, deleteById, etc.)
}