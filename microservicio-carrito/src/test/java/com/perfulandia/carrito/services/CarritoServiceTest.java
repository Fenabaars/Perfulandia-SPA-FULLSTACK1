package com.perfulandia.carrito.services;

import com.perfulandia.carrito.models.dtos.CarritoItemDTO;
import com.perfulandia.carrito.models.entities.CarritoItem;
import com.perfulandia.carrito.repositories.CarritoItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {

    @Mock
    private CarritoItemRepository repository;

    @InjectMocks
    private CarritoService service;

    private CarritoItem item;
    private CarritoItemDTO dto;

    @BeforeEach
    void setUp() {
        item = new CarritoItem();
        item.setId(1L);
        item.setUsuarioId(10L);
        item.setPerfumeId(100L);
        item.setNombrePerfume("Chanel");
        item.setPrecio(150.0);
        item.setCantidad(2);

        dto = new CarritoItemDTO();
        dto.setUsuarioId(10L);
        dto.setPerfumeId(100L);
        dto.setNombrePerfume("Chanel");
        dto.setPrecio(150.0);
        dto.setCantidad(1);
    }

    @Test
    void testObtenerCarritoPorUsuario() {
        when(repository.findByUsuarioId(10L)).thenReturn(Arrays.asList(item));
        List<CarritoItem> res = service.obtenerCarritoPorUsuario(10L);
        assertFalse(res.isEmpty());
        assertEquals(1, res.size());
    }

    @Test
    void testAgregarItemNuevo() {
        when(repository.findByUsuarioId(10L)).thenReturn(new ArrayList<>());
        when(repository.save(any(CarritoItem.class))).thenReturn(item);

        CarritoItem res = service.agregarItem(dto);
        assertNotNull(res);
        verify(repository, times(1)).save(any(CarritoItem.class));
    }

    @Test
    void testAgregarItemExistente() {
        when(repository.findByUsuarioId(10L)).thenReturn(Arrays.asList(item));
        when(repository.save(any(CarritoItem.class))).thenReturn(item);

        CarritoItem res = service.agregarItem(dto);
        assertNotNull(res);
        assertEquals(3, item.getCantidad()); // 2 + 1
        verify(repository, times(1)).save(any(CarritoItem.class));
    }

    @Test
    void testActualizarCantidadSuccess() {
        when(repository.findById(1L)).thenReturn(Optional.of(item));
        when(repository.save(any(CarritoItem.class))).thenReturn(item);

        CarritoItem res = service.actualizarCantidad(1L, 5);
        assertEquals(5, res.getCantidad());
    }

    @Test
    void testActualizarCantidadMenorA1() {
        when(repository.findById(1L)).thenReturn(Optional.of(item));
        assertThrows(RuntimeException.class, () -> service.actualizarCantidad(1L, 0));
    }

    @Test
    void testActualizarCantidadNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.actualizarCantidad(1L, 5));
    }

    @Test
    void testEliminarItemSuccess() {
        when(repository.findById(1L)).thenReturn(Optional.of(item));
        doNothing().when(repository).delete(item);

        assertDoesNotThrow(() -> service.eliminarItem(1L));
        verify(repository, times(1)).delete(item);
    }

    @Test
    void testEliminarItemNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.eliminarItem(1L));
    }

    @Test
    void testVaciarCarrito() {
        doNothing().when(repository).deleteByUsuarioId(10L);
        assertDoesNotThrow(() -> service.vaciarCarrito(10L));
        verify(repository, times(1)).deleteByUsuarioId(10L);
    }
}
