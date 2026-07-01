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

    private Sucursal sucursalMock;
    private SucursalDTO dtoMock;

    @BeforeEach
    void setUp() {
        sucursalMock = new Sucursal("Sucursal Centro", "Calle 1", "Santiago", "12345", "09:00 - 18:00");
        sucursalMock.setId(1L);

        dtoMock = new SucursalDTO();
        dtoMock.setNombre("Sucursal Centro");
        dtoMock.setDireccion("Calle 1");
        dtoMock.setComuna("Santiago");
    }

    @Test
    void givenSucursalValida_whenRegistrarSucursal_thenRetornaSucursalCreada() {
        // Given
        when(repository.save(any(Sucursal.class))).thenReturn(sucursalMock);

        // When
        Sucursal resultado = service.registrarSucursal(dtoMock);

        // Then
        assertNotNull(resultado);
        assertEquals("Santiago", resultado.getComuna());
        verify(repository, times(1)).save(any(Sucursal.class));
    }

    @Test
    void givenSucursalNoExistente_whenObtenerPorId_thenLanzaExcepcion() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.obtenerPorId(99L);
        });

        assertEquals("Sucursal no encontrada", exception.getMessage());
        verify(repository, times(1)).findById(99L);
    }
}
