package com.perfulandia.microservicio_catalogo.controller;

import com.perfulandia.microservicio_catalogo.model.Perfume;
import com.perfulandia.microservicio_catalogo.service.PerfumeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.perfulandia.microservicio_catalogo.dto.PerfumeDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerfumeControllerTest {

    @Mock
    private PerfumeService perfumeService;

    @InjectMocks
    private PerfumeController perfumeController;

    private Perfume perfume;

    @BeforeEach
    void setUp() {
        perfume = new Perfume();
        perfume.setId(1L);
        perfume.setNombre("Test Perfume");
        perfume.setMarca("Test Marca");
        perfume.setCategoria("Hombre");
        perfume.setDescripcion("Test Desc");
        perfume.setPrecio(15000.0);
        perfume.setNotasOlfativas("Madera");
    }

    @Test
    void testGetAllPerfumes() {
        // Given
        when(perfumeService.getAllPerfumes()).thenReturn(Arrays.asList(perfume));

        // When
        List<Perfume> perfumes = perfumeController.getAllPerfumes();

        // Then
        assertFalse(perfumes.isEmpty());
        assertEquals(1, perfumes.size());
        assertEquals("Test Perfume", perfumes.get(0).getNombre());
        verify(perfumeService, times(1)).getAllPerfumes();
    }

    @Test
    void testGetPerfumeById() {
        // Given
        when(perfumeService.getPerfumeById(1L)).thenReturn(Optional.of(perfume));

        // When
        ResponseEntity<Perfume> response = perfumeController.getPerfumeById(1L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Perfume", response.getBody().getNombre());
        verify(perfumeService, times(1)).getPerfumeById(1L);
    }

    @Test
    void testGetPerfumeByIdNotFound() {
        // Given
        when(perfumeService.getPerfumeById(1L)).thenReturn(Optional.empty());

        // When
        ResponseEntity<Perfume> response = perfumeController.getPerfumeById(1L);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(perfumeService, times(1)).getPerfumeById(1L);
    }

    @Test
    void testCreatePerfume() {
        // Given
        when(perfumeService.createPerfume(any(Perfume.class))).thenReturn(perfume);

        // When
        ResponseEntity<Perfume> response = perfumeController.createPerfume(new PerfumeDTO());

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Perfume", response.getBody().getNombre());
        verify(perfumeService, times(1)).createPerfume(any(Perfume.class));
    }

    @Test
    void testUpdatePerfume() {
        // Given
        when(perfumeService.updatePerfume(eq(1L), any(Perfume.class))).thenReturn(Optional.of(perfume));

        // When
        ResponseEntity<Perfume> response = perfumeController.updatePerfume(1L, new PerfumeDTO());

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Perfume", response.getBody().getNombre());
        verify(perfumeService, times(1)).updatePerfume(eq(1L), any(Perfume.class));
    }

    @Test
    void testUpdatePerfumeNotFound() {
        // Given
        when(perfumeService.updatePerfume(eq(1L), any(Perfume.class))).thenReturn(Optional.empty());

        // When
        ResponseEntity<Perfume> response = perfumeController.updatePerfume(1L, new PerfumeDTO());

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(perfumeService, times(1)).updatePerfume(eq(1L), any(Perfume.class));
    }

    @Test
    void testDeletePerfumeSuccess() {
        // Given
        when(perfumeService.deletePerfume(1L)).thenReturn(true);

        // When
        ResponseEntity<Void> response = perfumeController.deletePerfume(1L);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(perfumeService, times(1)).deletePerfume(1L);
    }

    @Test
    void testDeletePerfumeNotFound() {
        // Given
        when(perfumeService.deletePerfume(1L)).thenReturn(false);

        // When
        ResponseEntity<Void> response = perfumeController.deletePerfume(1L);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(perfumeService, times(1)).deletePerfume(1L);
    }
}
