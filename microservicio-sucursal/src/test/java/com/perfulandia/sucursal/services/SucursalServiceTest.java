package com.perfulandia.sucursal.services;

import com.perfulandia.sucursal.models.dtos.SucursalDTO;
import com.perfulandia.sucursal.models.entities.Sucursal;
import com.perfulandia.sucursal.repositories.SucursalRepository;
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
class SucursalServiceTest {

    @Mock
    private SucursalRepository repository;

    @InjectMocks
    private SucursalService service;

    private Sucursal sucursal;
    private SucursalDTO dto;

    @BeforeEach
    void setUp() {
        sucursal = new Sucursal("Sucursal Central", "Calle 1", "Centro", "123456", "9-18");
        sucursal.setId(1L);

        dto = new SucursalDTO();
        dto.setNombre("Sucursal Central");
        dto.setDireccion("Calle 1");
        dto.setComuna("Centro");
        dto.setTelefono("123456");
        dto.setHorarioAtencion("9-18");
    }

    @Test
    void testObtenerTodas() {
        when(repository.findAll()).thenReturn(Arrays.asList(sucursal));
        List<Sucursal> result = service.obtenerTodas();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testObtenerPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(sucursal));
        Sucursal result = service.obtenerPorId(1L);
        assertNotNull(result);
        assertEquals("Sucursal Central", result.getNombre());
    }

    @Test
    void testObtenerPorId_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.obtenerPorId(1L));
    }

    @Test
    void testBuscarPorComuna() {
        when(repository.findByComunaContainingIgnoreCase("Centro")).thenReturn(Arrays.asList(sucursal));
        List<Sucursal> result = service.buscarPorComuna("Centro");
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testRegistrarSucursal() {
        when(repository.save(any(Sucursal.class))).thenReturn(sucursal);

        Sucursal result = service.registrarSucursal(dto);
        assertNotNull(result);
        verify(repository, times(1)).save(any(Sucursal.class));
    }

    @Test
    void testActualizarSucursal() {
        when(repository.findById(1L)).thenReturn(Optional.of(sucursal));
        when(repository.save(any(Sucursal.class))).thenReturn(sucursal);

        Sucursal result = service.actualizarSucursal(1L, dto);
        assertNotNull(result);
        assertEquals("Sucursal Central", result.getNombre());
    }

    @Test
    void testEliminarSucursal() {
        when(repository.findById(1L)).thenReturn(Optional.of(sucursal));
        doNothing().when(repository).delete(any(Sucursal.class));

        service.eliminarSucursal(1L);
        verify(repository, times(1)).delete(sucursal);
    }
}
