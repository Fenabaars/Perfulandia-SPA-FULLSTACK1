package com.perfulandia.promocion.service;

import com.perfulandia.promocion.entity.Promocion;
import com.perfulandia.promocion.repository.PromocionRepository;
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
class PromocionServiceTest {

    @Mock
    private PromocionRepository repository;

    @InjectMocks
    private PromocionService service;

    private Promocion promocionMock;

    @BeforeEach
    void setUp() {
        promocionMock = new Promocion();
        promocionMock.setId(1L);
        promocionMock.setCodigo("DESC10");
        promocionMock.setActivo(true);
        promocionMock.setFechaInicio(LocalDate.now().minusDays(1));
        promocionMock.setFechaFin(LocalDate.now().plusDays(10));
    }

    @Test
    void givenPromocionValida_whenValidatePromotion_thenRetornaPromocion() {
        // Given
        when(repository.findByCodigo("DESC10")).thenReturn(Optional.of(promocionMock));

        // When
        Object resultado = service.validatePromotion("DESC10");

        // Then
        assertTrue(resultado instanceof Promocion);
        assertEquals("DESC10", ((Promocion) resultado).getCodigo());
        verify(repository, times(1)).findByCodigo("DESC10");
    }

    @Test
    void givenPromocionInactiva_whenValidatePromotion_thenRetornaMensajeError() {
        // Given
        promocionMock.setActivo(false);
        when(repository.findByCodigo("DESC10")).thenReturn(Optional.of(promocionMock));

        // When
        Object resultado = service.validatePromotion("DESC10");

        // Then
        assertTrue(resultado instanceof String);
        assertEquals("El código promocional está inactivo.", resultado);
    }
}
