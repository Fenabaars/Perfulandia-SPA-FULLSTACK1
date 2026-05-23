package com.perfulandia.sucursal.repositories;

import com.perfulandia.sucursal.models.entities.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
    List<Sucursal> findByComunaContainingIgnoreCase(String comuna);
}