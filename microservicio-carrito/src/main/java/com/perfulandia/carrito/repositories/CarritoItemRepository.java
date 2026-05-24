package com.perfulandia.carrito.repositories;

import com.perfulandia.carrito.models.entities.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {
    List<CarritoItem> findByUsuarioId(Long usuarioId);
    void deleteByUsuarioId(Long usuarioId);
}
