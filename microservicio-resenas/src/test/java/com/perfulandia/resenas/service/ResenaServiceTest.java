package com.perfulandia.resenas.service;

import com.perfulandia.resenas.entity.Resena;
import com.perfulandia.resenas.repository.ResenaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResenaServiceTest {

    @Mock
    private ResenaRepository repository;

    @InjectMocks
    private ResenaService service;

    private Resena resenaMock;

    @BeforeEach
    void setUp() {
        resenaMock = new Resena();
        resenaMock.setId(1L);
        resenaMock.setProductoId(100L);
        resenaMock.setCalificacion(5);
        resenaMock.setComentario("Excelente perfume!");
    }

    @Test
    void givenResenaValida_whenCreateResena_thenRetornaResenaCreada() {
        // Given
        when(repository.save(any(Resena.class))).thenReturn(resenaMock);

        // When
        Object resultado = service.createResena(resenaMock);

        // Then
        assertTrue(resultado instanceof Resena);
        assertEquals(5, ((Resena) resultado).getCalificacion());
        verify(repository, times(1)).save(any(Resena.class));
    }

    @Test
    void givenResenaInvalida_whenCreateResena_thenRetornaError() {
        // Given
        resenaMock.setCalificacion(6); // Inválida, máximo 5

        // When
        Object resultado = service.createResena(resenaMock);

        // Then
        assertTrue(resultado instanceof String);
        assertEquals("La calificación debe estar entre 1 y 5.", resultado);
        verify(repository, never()).save(any(Resena.class));
    }
}
