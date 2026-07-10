package com.perfulandia.resenas.service;

import com.perfulandia.resenas.entity.Resena;
import com.perfulandia.resenas.repository.ResenaRepository;
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
class ResenaServiceTest {

    @Mock
    private ResenaRepository resenaRepository;

    @InjectMocks
    private ResenaService resenaService;

    private Resena resena;

    @BeforeEach
    void setUp() {
        resena = new Resena();
        resena.setId(1L);
        resena.setUsuarioId(10L);
        resena.setProductoId(100L);
        resena.setCalificacion(5);
        resena.setComentario("Muy bueno");
    }

    @Test
    void testCreateResenaSuccess() {
        when(resenaRepository.save(any(Resena.class))).thenReturn(resena);
        
        Object resultado = resenaService.createResena(resena);
        
        assertTrue(resultado instanceof Resena);
        assertNotNull(((Resena) resultado).getFechaCreacion());
        assertEquals(5, ((Resena) resultado).getCalificacion());
        verify(resenaRepository, times(1)).save(any(Resena.class));
    }

    @Test
    void testCreateResenaCalificacionNula() {
        resena.setCalificacion(null);
        Object resultado = resenaService.createResena(resena);
        assertEquals("La calificación debe estar entre 1 y 5.", resultado);
        verify(resenaRepository, never()).save(any(Resena.class));
    }

    @Test
    void testCreateResenaCalificacionMenorA1() {
        resena.setCalificacion(0);
        Object resultado = resenaService.createResena(resena);
        assertEquals("La calificación debe estar entre 1 y 5.", resultado);
        verify(resenaRepository, never()).save(any(Resena.class));
    }

    @Test
    void testCreateResenaCalificacionMayorA5() {
        resena.setCalificacion(6);
        Object resultado = resenaService.createResena(resena);
        assertEquals("La calificación debe estar entre 1 y 5.", resultado);
        verify(resenaRepository, never()).save(any(Resena.class));
    }

    @Test
    void testGetResenasByProductoId() {
        when(resenaRepository.findByProductoId(100L)).thenReturn(Arrays.asList(resena));
        
        List<Resena> resenas = resenaService.getResenasByProductoId(100L);
        
        assertFalse(resenas.isEmpty());
        assertEquals(1, resenas.size());
        verify(resenaRepository, times(1)).findByProductoId(100L);
    }

    @Test
    void testDeleteResenaSuccess() {
        when(resenaRepository.findById(1L)).thenReturn(Optional.of(resena));
        doNothing().when(resenaRepository).deleteById(1L);
        
        boolean resultado = resenaService.deleteResena(1L);
        
        assertTrue(resultado);
        verify(resenaRepository, times(1)).findById(1L);
        verify(resenaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteResenaNotFound() {
        when(resenaRepository.findById(1L)).thenReturn(Optional.empty());
        
        boolean resultado = resenaService.deleteResena(1L);
        
        assertFalse(resultado);
        verify(resenaRepository, times(1)).findById(1L);
        verify(resenaRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetAllResenas() {
        when(resenaRepository.findAll()).thenReturn(Arrays.asList(resena));
        
        List<Resena> resenas = resenaService.getAllResenas();
        
        assertFalse(resenas.isEmpty());
        assertEquals(1, resenas.size());
        verify(resenaRepository, times(1)).findAll();
    }
}
