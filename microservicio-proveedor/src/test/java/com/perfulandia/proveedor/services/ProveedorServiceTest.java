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

    private Proveedor proveedorMock;
    private ProveedorDTO dtoMock;

    @BeforeEach
    void setUp() {
        proveedorMock = new Proveedor("12345678-9", "Perfumes SA", "Juan Perez", "juan@test.com", "123456789", "Calle 123");
        proveedorMock.setId(1L);

        dtoMock = new ProveedorDTO();
        dtoMock.setRut("12345678-9");
        dtoMock.setRazonSocial("Perfumes SA");
        dtoMock.setNombreContacto("Juan Perez");
    }

    @Test
    void givenProveedorValido_whenRegistrarProveedor_thenRetornaProveedor() {
        // Given
        when(repository.existsByRut("12345678-9")).thenReturn(false);
        when(repository.save(any(Proveedor.class))).thenReturn(proveedorMock);

        // When
        Proveedor resultado = service.registrarProveedor(dtoMock);

        // Then
        assertNotNull(resultado);
        assertEquals("12345678-9", resultado.getRut());
        verify(repository, times(1)).save(any(Proveedor.class));
    }

    @Test
    void givenRutExistente_whenRegistrarProveedor_thenLanzaExcepcion() {
        // Given
        when(repository.existsByRut("12345678-9")).thenReturn(true);

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.registrarProveedor(dtoMock);
        });

        assertEquals("Ya existe un proveedor registrado con ese RUT", exception.getMessage());
        verify(repository, never()).save(any(Proveedor.class));
    }
}
