package com.perfulandia.microservicio_catalogo.service;

import com.perfulandia.microservicio_catalogo.model.Perfume;
import com.perfulandia.microservicio_catalogo.repository.PerfumeRepository;
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
class PerfumeServiceTest {

    @Mock
    private PerfumeRepository repository;

    @InjectMocks
    private PerfumeService service;

    private Perfume perfumeMock;

    @BeforeEach
    void setUp() {
        perfumeMock = new Perfume();
        perfumeMock.setId(1L);
        perfumeMock.setNombre("Chanel N5");
        perfumeMock.setMarca("Chanel");
        perfumeMock.setPrecio(150.0);
    }

    @Test
    void givenPerfumeValido_whenCreatePerfume_thenRetornaPerfumeCreado() {
        // Given
        when(repository.save(any(Perfume.class))).thenReturn(perfumeMock);

        // When
        Perfume resultado = service.createPerfume(perfumeMock);

        // Then
        assertNotNull(resultado);
        assertEquals("Chanel N5", resultado.getNombre());
        verify(repository, times(1)).save(any(Perfume.class));
    }

    @Test
    void givenPerfumeExistente_whenUpdatePerfume_thenRetornaPerfumeActualizado() {
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(perfumeMock));
        when(repository.save(any(Perfume.class))).thenReturn(perfumeMock);

        Perfume detalles = new Perfume();
        detalles.setNombre("Chanel N5 Nuevo");

        // When
        Optional<Perfume> resultado = service.updatePerfume(1L, detalles);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Chanel N5 Nuevo", resultado.get().getNombre());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Perfume.class));
    }
}
