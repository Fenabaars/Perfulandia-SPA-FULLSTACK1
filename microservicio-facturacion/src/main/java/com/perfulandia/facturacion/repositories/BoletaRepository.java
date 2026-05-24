package com.perfulandia.facturacion.repositories;

import com.perfulandia.facturacion.models.entities.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Long> {

    // H21: Listar boletas del cliente para descarga desde perfil
    List<Boleta> findByClienteIdOrderByFechaEmisionDesc(Long clienteId);

    // Buscar boleta asociada a un pedido web
    Optional<Boleta> findByPedidoId(Long pedidoId);

    // Buscar boleta asociada a una venta física
    Optional<Boleta> findByVentaId(Long ventaId);

    // Buscar por número de boleta
    Optional<Boleta> findByNumeroBoleta(String numeroBoleta);
}
