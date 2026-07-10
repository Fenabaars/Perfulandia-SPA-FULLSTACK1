package com.perfulandia.carrito.services;

import com.perfulandia.carrito.models.dtos.DetalleVentaDTO;
import com.perfulandia.carrito.models.dtos.VentaDTO;
import com.perfulandia.carrito.models.entities.Venta;
import com.perfulandia.carrito.repositories.VentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository repository;

    @InjectMocks
    private VentaService service;

    private Venta venta;
    private VentaDTO dto;

    @BeforeEach
    void setUp() {
        venta = new Venta();
        venta.setId(1L);
        venta.setSucursalId(10L);
        venta.setEmpleadoId(5L);

        dto = new VentaDTO();
        dto.setSucursalId(10L);
        dto.setEmpleadoId(5L);
        
        DetalleVentaDTO det = new DetalleVentaDTO();
        det.setPerfumeId(100L);
        det.setCantidad(2);
        det.setPrecioUnitario(50.0);
        dto.setDetalles(Arrays.asList(det));
    }

    @Test
    void testObtenerTodas() {
        when(repository.findAll()).thenReturn(Arrays.asList(venta));
        List<Venta> res = service.obtenerTodas();
        assertFalse(res.isEmpty());
    }

    @Test
    void testObtenerPorIdSuccess() {
        when(repository.findById(1L)).thenReturn(Optional.of(venta));
        Venta res = service.obtenerPorId(1L);
        assertNotNull(res);
    }

    @Test
    void testObtenerPorIdNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.obtenerPorId(1L));
    }

    @Test
    void testObtenerPorSucursal() {
        when(repository.findBySucursalIdOrderByFechaVentaDesc(10L)).thenReturn(Arrays.asList(venta));
        List<Venta> res = service.obtenerPorSucursal(10L);
        assertFalse(res.isEmpty());
    }

    @Test
    void testRegistrarVenta() {
        when(repository.save(any(Venta.class))).thenReturn(venta);
        Venta res = service.registrarVenta(dto);
        assertNotNull(res);
        verify(repository, times(1)).save(any(Venta.class));
    }

    @Test
    void testEliminarVentaSuccess() {
        when(repository.findById(1L)).thenReturn(Optional.of(venta));
        doNothing().when(repository).delete(any(Venta.class));
        assertDoesNotThrow(() -> service.eliminarVenta(1L));
        verify(repository, times(1)).delete(any(Venta.class));
    }
}
