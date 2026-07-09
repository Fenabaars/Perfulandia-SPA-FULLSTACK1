package com.perfulandia.inventario.services;

import com.perfulandia.inventario.clients.PerfumeClient;
import com.perfulandia.inventario.clients.SucursalClient;
import com.perfulandia.inventario.models.dtos.InventarioDTO;
import com.perfulandia.inventario.models.entities.Inventario;
import com.perfulandia.inventario.repositories.InventarioRepository;
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
class InventarioServiceTest {

    @Mock
    private InventarioRepository repository;

    @Mock
    private PerfumeClient perfumeClient;

    @Mock
    private SucursalClient sucursalClient;

    @InjectMocks
    private InventarioService service;

    private Inventario inventarioMock;
    private InventarioDTO dtoMock;

    @BeforeEach
    void setUp() {
        inventarioMock = new Inventario(1L, 2L, 50);
        inventarioMock.setId(100L);

        dtoMock = new InventarioDTO();
        dtoMock.setPerfumeId(1L);
        dtoMock.setSucursalId(2L);
        dtoMock.setCantidad(20);
    }

    @Test
    void givenInventarioExistente_whenIngresarStock_thenSumaCantidad() {
        // Given
        when(perfumeClient.getPerfumeById(1L)).thenReturn(null);
        when(sucursalClient.getSucursalById(2L)).thenReturn(null);
        when(repository.findByPerfumeIdAndSucursalId(1L, 2L)).thenReturn(Optional.of(inventarioMock));
        when(repository.save(any(Inventario.class))).thenReturn(inventarioMock);

        // When
        Inventario resultado = service.ingresarStock(dtoMock);

        // Then
        assertEquals(70, resultado.getCantidad());
        verify(repository, times(1)).save(inventarioMock);
    }

    @Test
    void givenStockSuficiente_whenDescontarStock_thenRestaCantidad() {
        // Given
        when(repository.findByPerfumeIdAndSucursalId(1L, 2L)).thenReturn(Optional.of(inventarioMock));
        when(repository.save(any(Inventario.class))).thenReturn(inventarioMock);

        // When
        Inventario resultado = service.descontarStock(1L, 2L, 10);

        // Then
        assertEquals(40, resultado.getCantidad());
        verify(repository, times(1)).save(inventarioMock);
    }

    @Test
    void givenStockInsuficiente_whenDescontarStock_thenLanzaExcepcion() {
        // Given
        when(repository.findByPerfumeIdAndSucursalId(1L, 2L)).thenReturn(Optional.of(inventarioMock));

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.descontarStock(1L, 2L, 60);
        });

        assertEquals("Stock insuficiente para realizar la reserva o venta.", exception.getMessage());
        verify(repository, never()).save(any(Inventario.class));
    }

    @Test
    void givenInventarioNoExistente_whenIngresarStock_thenCreaNuevo() {
        // Given
        when(perfumeClient.getPerfumeById(1L)).thenReturn(null);
        when(sucursalClient.getSucursalById(2L)).thenReturn(null);
        when(repository.findByPerfumeIdAndSucursalId(1L, 2L)).thenReturn(Optional.empty());
        when(repository.save(any(Inventario.class))).thenAnswer(invocation -> {
            Inventario inv = invocation.getArgument(0);
            inv.setId(101L);
            return inv;
        });

        // When
        Inventario resultado = service.ingresarStock(dtoMock);

        // Then
        assertEquals(101L, resultado.getId());
        assertEquals(20, resultado.getCantidad());
        verify(repository, times(1)).save(any(Inventario.class));
    }

    @Test
    void givenPerfumeNoExiste_whenIngresarStock_thenLanzaExcepcion() {
        // Given
        when(perfumeClient.getPerfumeById(1L)).thenThrow(feign.FeignException.NotFound.class);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.ingresarStock(dtoMock);
        });

        assertEquals("El perfume indicado no existe en el sistema.", exception.getMessage());
        verify(repository, never()).save(any(Inventario.class));
    }

    @Test
    void givenSucursalNoExiste_whenIngresarStock_thenLanzaExcepcion() {
        // Given
        when(perfumeClient.getPerfumeById(1L)).thenReturn(null);
        when(sucursalClient.getSucursalById(2L)).thenThrow(feign.FeignException.NotFound.class);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.ingresarStock(dtoMock);
        });

        assertEquals("La sucursal indicada no existe en el sistema.", exception.getMessage());
        verify(repository, never()).save(any(Inventario.class));
    }

    @Test
    void givenSinStock_whenConsultarStock_thenLanzaExcepcion() {
        // Given
        when(repository.findByPerfumeIdAndSucursalId(1L, 2L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.consultarStock(1L, 2L);
        });

        assertEquals("No hay stock registrado para este perfume en esta sucursal.", exception.getMessage());
    }

    @Test
    void testListarPorSucursal() {
        when(repository.findBySucursalId(2L)).thenReturn(java.util.Collections.singletonList(inventarioMock));
        
        java.util.List<Inventario> resultados = service.listarPorSucursal(2L);
        
        assertEquals(1, resultados.size());
        assertEquals(50, resultados.get(0).getCantidad());
    }
}
