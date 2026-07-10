package com.perfulandia.proveedor.repositories;

import com.perfulandia.proveedor.models.entities.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    Optional<Proveedor> findByRut(String rut);
    boolean existsByRut(String rut);
}