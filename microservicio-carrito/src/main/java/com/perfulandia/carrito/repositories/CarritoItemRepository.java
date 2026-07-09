package com.perfulandia.carrito.repositories;

import com.perfulandia.carrito.models.entities.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {
    List<CarritoItem> findByUsuarioId(Long usuarioId);
    void deleteByUsuarioId(Long usuarioId);
}
