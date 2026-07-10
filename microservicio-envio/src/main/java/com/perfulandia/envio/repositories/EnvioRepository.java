package com.perfulandia.envio.repositories;

import com.perfulandia.envio.models.entities.Envio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnvioRepository extends JpaRepository<Envio, Long> {

    // H22: Listar envíos por estado (ej: PREPARACION para ver pedidos en bodega)
    List<Envio> findByEstadoOrderByFechaCreacionAsc(String estado);

    // H23: Listar envíos del cliente para rastreo
    List<Envio> findByClienteIdOrderByFechaCreacionDesc(Long clienteId);

    // H23: Buscar por código de seguimiento
    Optional<Envio> findByCodigoSeguimiento(String codigoSeguimiento);

    // Buscar envío de un pedido
    Optional<Envio> findByPedidoId(Long pedidoId);

    // H22: Filtrar por sucursal/bodega
    List<Envio> findBySucursalIdOrderByFechaCreacionAsc(Long sucursalId);
}
