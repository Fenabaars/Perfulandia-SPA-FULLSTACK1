package com.perfulandia.carrito.repositories;

import com.perfulandia.carrito.models.entities.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findBySucursalIdOrderByFechaVentaDesc(Long sucursalId);
    List<Venta> findByEmpleadoId(Long empleadoId);
}
