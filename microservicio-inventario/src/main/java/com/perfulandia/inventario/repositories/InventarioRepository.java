package com.perfulandia.inventario.repositories;

import com.perfulandia.inventario.models.entities.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    Optional<Inventario> findByPerfumeIdAndSucursalId(Long perfumeId, Long sucursalId);
    List<Inventario> findBySucursalId(Long sucursalId);
}