package com.perfulandia.proveedor.services;

import com.perfulandia.proveedor.models.dtos.ProveedorDTO;
import com.perfulandia.proveedor.models.entities.Proveedor;
import com.perfulandia.proveedor.repositories.ProveedorRepository;
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
class ProveedorServiceTest {

    @Mock
    private ProveedorRepository repository;

    @InjectMocks
    private ProveedorService service;

    private Proveedor proveedor;
    private ProveedorDTO dto;

    @BeforeEach
    void setUp() {
        proveedor = new Proveedor("12345678-9", "Empresa SA", "Juan", "juan@empresa.com", "123456", "Dir 1");
        proveedor.setId(1L);

        dto = new ProveedorDTO();
        dto.setRut("12345678-9");
        dto.setRazonSocial("Empresa SA");
        dto.setNombreContacto("Juan");
        dto.setEmail("juan@empresa.com");
        dto.setTelefono("123456");
        dto.setDireccion("Dir 1");
    }

    @Test
    void testObtenerTodos() {
        when(repository.findAll()).thenReturn(Arrays.asList(proveedor));
        List<Proveedor> result = service.obtenerTodos();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testObtenerPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(proveedor));
        Proveedor result = service.obtenerPorId(1L);
        assertNotNull(result);
        assertEquals("12345678-9", result.getRut());
    }

    @Test
    void testObtenerPorId_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.obtenerPorId(1L));
    }

    @Test
    void testObtenerPorRut() {
        when(repository.findByRut("12345678-9")).thenReturn(Optional.of(proveedor));
        Proveedor result = service.obtenerPorRut("12345678-9");
        assertNotNull(result);
    }

    @Test
    void testObtenerPorRut_NotFound() {
        when(repository.findByRut("12")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.obtenerPorRut("12"));
    }

    @Test
    void testRegistrarProveedor() {
        when(repository.existsByRut("12345678-9")).thenReturn(false);
        when(repository.save(any(Proveedor.class))).thenReturn(proveedor);

        Proveedor result = service.registrarProveedor(dto);
        assertNotNull(result);
        verify(repository, times(1)).save(any(Proveedor.class));
    }

    @Test
    void testRegistrarProveedor_AlreadyExists() {
        when(repository.existsByRut("12345678-9")).thenReturn(true);
        assertThrows(RuntimeException.class, () -> service.registrarProveedor(dto));
    }

    @Test
    void testActualizarProveedor() {
        when(repository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(repository.save(any(Proveedor.class))).thenReturn(proveedor);

        Proveedor result = service.actualizarProveedor(1L, dto);
        assertNotNull(result);
        assertEquals("12345678-9", result.getRut());
    }

    @Test
    void testActualizarProveedor_RutAlreadyAssigned() {
        Proveedor existente = new Proveedor("11111111-1", "Empresa 2", "Ana", "ana@emp", "123", "Dir 2");
        existente.setId(2L);
        when(repository.findById(2L)).thenReturn(Optional.of(existente));
        when(repository.existsByRut("12345678-9")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> service.actualizarProveedor(2L, dto));
    }

    @Test
    void testEliminarProveedor() {
        when(repository.findById(1L)).thenReturn(Optional.of(proveedor));
        doNothing().when(repository).delete(any(Proveedor.class));

        service.eliminarProveedor(1L);
        verify(repository, times(1)).delete(proveedor);
    }
}
