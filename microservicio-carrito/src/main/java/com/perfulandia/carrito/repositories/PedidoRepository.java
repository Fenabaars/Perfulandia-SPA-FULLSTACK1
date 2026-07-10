package com.perfulandia.carrito.repositories;

import com.perfulandia.carrito.models.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // H19: Historial de compras ordenado por fecha descendente
    List<Pedido> findByUsuarioIdOrderByFechaPedidoDesc(Long usuarioId);
}
