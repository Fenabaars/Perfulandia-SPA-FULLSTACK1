package com.perfulandia.envios.services;

import com.perfulandia.envios.models.entities.Envio;
import com.perfulandia.envios.repositories.EnvioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvioServiceTest {

    @Mock
    private EnvioRepository repository;

    @InjectMocks
    private EnvioService service;

    private Envio envio;

    @BeforeEach
    void setUp() {
        envio = new Envio();
        envio.setId(1L);
        envio.setPedidoId(10L);
        envio.setEmpresaTransporte("Starken");
        envio.setNumeroSeguimiento("12345");
        envio.setEstado("EN_PREPARACION");
        envio.setFechaActualizacion(LocalDateTime.now());
    }

    @Test
    void testObtenerTodos() {
        when(repository.findAll()).thenReturn(Arrays.asList(envio));
        List<Envio> result = service.obtenerTodos();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testObtenerPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(envio));
        Envio result = service.obtenerPorId(1L);
        assertNotNull(result);
        assertEquals("Starken", result.getEmpresaTransporte());
    }

    @Test
    void testObtenerPorId_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.obtenerPorId(1L));
    }

    @Test
    void testObtenerPorPedidoId() {
        when(repository.findByPedidoId(10L)).thenReturn(Optional.of(envio));
        Envio result = service.obtenerPorPedidoId(10L);
        assertNotNull(result);
        assertEquals(10L, result.getPedidoId());
    }

    @Test
    void testObtenerPorPedidoId_NotFound() {
        when(repository.findByPedidoId(10L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.obtenerPorPedidoId(10L));
    }

    @Test
    void testRegistrarEnvio() {
        when(repository.save(any(Envio.class))).thenReturn(envio);
        Envio result = service.registrarEnvio(envio);
        assertNotNull(result);
        verify(repository, times(1)).save(any(Envio.class));
    }

    @Test
    void testActualizarEstado() {
        when(repository.findById(1L)).thenReturn(Optional.of(envio));
        when(repository.save(any(Envio.class))).thenReturn(envio);

        Envio result = service.actualizarEstado(1L, "ENVIADO");
        assertNotNull(result);
        assertEquals("ENVIADO", result.getEstado());
    }
}
