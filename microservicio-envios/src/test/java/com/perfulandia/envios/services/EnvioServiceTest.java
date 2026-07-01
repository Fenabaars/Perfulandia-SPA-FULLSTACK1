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

    private Envio envioMock;

    @BeforeEach
    void setUp() {
        envioMock = new Envio();
        envioMock.setId(1L);
        envioMock.setPedidoId(100L);
        envioMock.setEstado("PENDIENTE");
        envioMock.setDireccionDestino("Calle Falsa 123");
    }

    @Test
    void givenEnvioValido_whenRegistrarEnvio_thenRetornaEnvioPendiente() {
        // Given
        when(repository.save(any(Envio.class))).thenReturn(envioMock);

        // When
        Envio resultado = service.registrarEnvio(envioMock);

        // Then
        assertNotNull(resultado);
        assertEquals("PENDIENTE", resultado.getEstado());
        verify(repository, times(1)).save(any(Envio.class));
    }

    @Test
    void givenEnvioExistente_whenActualizarEstado_thenRetornaEnvioActualizado() {
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(envioMock));
        
        Envio envioActualizado = new Envio();
        envioActualizado.setId(1L);
        envioActualizado.setEstado("EN_TRANSITO");
        envioActualizado.setFechaActualizacion(LocalDateTime.now());
        
        when(repository.save(any(Envio.class))).thenReturn(envioActualizado);

        // When
        Envio resultado = service.actualizarEstado(1L, "EN_TRANSITO");

        // Then
        assertNotNull(resultado);
        assertEquals("EN_TRANSITO", resultado.getEstado());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Envio.class));
    }

    @Test
    void givenEnvioNoExistente_whenObtenerPorId_thenLanzaExcepcion() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.obtenerPorId(99L);
        });

        assertEquals("Envio no encontrado", exception.getMessage());
        verify(repository, times(1)).findById(99L);
    }
}
