package com.perfulandia.carrito.repositories;

import com.perfulandia.carrito.models.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // H19: Historial de compras ordenado por fecha descendente
    List<Pedido> findByUsuarioIdOrderByFechaPedidoDesc(Long usuarioId);
}
