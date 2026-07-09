package com.perfulandia.carrito.repositories;

import com.perfulandia.carrito.models.entities.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findBySucursalIdOrderByFechaVentaDesc(Long sucursalId);
    List<Venta> findByEmpleadoId(Long empleadoId);
}
