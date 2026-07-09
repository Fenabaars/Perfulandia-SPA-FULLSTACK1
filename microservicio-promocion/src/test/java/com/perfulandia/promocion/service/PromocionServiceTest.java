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
import java.util.Arrays;
import java.util.List;
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
        promocionMock.setTipoDescuento("PORCENTAJE");
        promocionMock.setValorDescuento(10.0);
        promocionMock.setActivo(true);
        promocionMock.setFechaInicio(LocalDate.now().minusDays(1));
        promocionMock.setFechaFin(LocalDate.now().plusDays(10));
    }

    @Test
    void testGetAllPromotions() {
        when(repository.findAll()).thenReturn(Arrays.asList(promocionMock));
        List<Promocion> res = service.getAllPromotions();
        assertFalse(res.isEmpty());
        assertEquals(1, res.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testCreatePromotion() {
        when(repository.save(any(Promocion.class))).thenReturn(promocionMock);
        Promocion res = service.createPromotion(new Promocion());
        assertNotNull(res);
        assertEquals("DESC10", res.getCodigo());
        verify(repository, times(1)).save(any(Promocion.class));
    }

    @Test
    void testUpdatePromotionSuccess() {
        when(repository.findById(1L)).thenReturn(Optional.of(promocionMock));
        when(repository.save(any(Promocion.class))).thenReturn(promocionMock);
        
        Promocion detalles = new Promocion();
        detalles.setCodigo("DESC20");
        detalles.setValorDescuento(20.0);
        
        Optional<Promocion> res = service.updatePromotion(1L, detalles);
        assertTrue(res.isPresent());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Promocion.class));
    }

    @Test
    void testUpdatePromotionNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        Optional<Promocion> res = service.updatePromotion(1L, new Promocion());
        assertFalse(res.isPresent());
        verify(repository, times(1)).findById(1L);
        verify(repository, never()).save(any(Promocion.class));
    }

    @Test
    void testDeletePromotionSuccess() {
        when(repository.findById(1L)).thenReturn(Optional.of(promocionMock));
        doNothing().when(repository).delete(any(Promocion.class));
        
        boolean res = service.deletePromotion(1L);
        assertTrue(res);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).delete(any(Promocion.class));
    }

    @Test
    void testDeletePromotionNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        boolean res = service.deletePromotion(1L);
        assertFalse(res);
        verify(repository, times(1)).findById(1L);
        verify(repository, never()).delete(any(Promocion.class));
    }

    @Test
    void givenPromocionValida_whenValidatePromotion_thenRetornaPromocion() {
        when(repository.findByCodigo("DESC10")).thenReturn(Optional.of(promocionMock));
        Object resultado = service.validatePromotion("DESC10");
        assertTrue(resultado instanceof Promocion);
        assertEquals("DESC10", ((Promocion) resultado).getCodigo());
    }

    @Test
    void givenPromocionNoExiste_whenValidatePromotion_thenRetornaMensajeError() {
        when(repository.findByCodigo("NOTFOUND")).thenReturn(Optional.empty());
        Object resultado = service.validatePromotion("NOTFOUND");
        assertEquals("El código promocional no existe.", resultado);
    }

    @Test
    void givenPromocionInactiva_whenValidatePromotion_thenRetornaMensajeError() {
        promocionMock.setActivo(false);
        when(repository.findByCodigo("DESC10")).thenReturn(Optional.of(promocionMock));
        Object resultado = service.validatePromotion("DESC10");
        assertEquals("El código promocional está inactivo.", resultado);
    }

    @Test
    void givenPromocionVencida_whenValidatePromotion_thenRetornaMensajeError() {
        promocionMock.setFechaInicio(LocalDate.now().minusDays(10));
        promocionMock.setFechaFin(LocalDate.now().minusDays(1));
        when(repository.findByCodigo("DESC10")).thenReturn(Optional.of(promocionMock));
        Object resultado = service.validatePromotion("DESC10");
        assertEquals("El código promocional está vencido o aún no es válido.", resultado);
    }

    @Test
    void givenPromocionFutura_whenValidatePromotion_thenRetornaMensajeError() {
        promocionMock.setFechaInicio(LocalDate.now().plusDays(1));
        promocionMock.setFechaFin(LocalDate.now().plusDays(10));
        when(repository.findByCodigo("DESC10")).thenReturn(Optional.of(promocionMock));
        Object resultado = service.validatePromotion("DESC10");
        assertEquals("El código promocional está vencido o aún no es válido.", resultado);
    }
}
