package com.perfulandia.microservicio_catalogo.service;

import com.perfulandia.microservicio_catalogo.model.Perfume;
import com.perfulandia.microservicio_catalogo.repository.PerfumeRepository;
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
@SuppressWarnings("null")
class PerfumeServiceTest {

    @Mock
    private PerfumeRepository perfumeRepository;

    @InjectMocks
    private PerfumeService perfumeService;

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
        when(perfumeRepository.findAll()).thenReturn(Arrays.asList(perfume));

        // When
        List<Perfume> perfumes = perfumeService.getAllPerfumes();

        // Then
        assertFalse(perfumes.isEmpty());
        assertEquals(1, perfumes.size());
        assertEquals("Test Perfume", perfumes.get(0).getNombre());
        verify(perfumeRepository, times(1)).findAll();
    }

    @Test
    void testGetPerfumeById() {
        // Given
        when(perfumeRepository.findById(1L)).thenReturn(Optional.of(perfume));

        // When
        Optional<Perfume> foundPerfume = perfumeService.getPerfumeById(1L);

        // Then
        assertTrue(foundPerfume.isPresent());
        assertEquals("Test Marca", foundPerfume.get().getMarca());
        verify(perfumeRepository, times(1)).findById(1L);
    }

    @Test
    void testCreatePerfume() {
        // Given
        when(perfumeRepository.save(any(Perfume.class))).thenReturn(perfume);

        // When
        Perfume createdPerfume = perfumeService.createPerfume(new Perfume());

        // Then
        assertNotNull(createdPerfume);
        assertEquals("Test Perfume", createdPerfume.getNombre());
        verify(perfumeRepository, times(1)).save(any(Perfume.class));
    }

    @Test
    void testUpdatePerfume() {
        // Given
        Perfume detalles = new Perfume();
        detalles.setNombre("Updated Perfume");
        detalles.setMarca("Updated Marca");
        
        when(perfumeRepository.findById(1L)).thenReturn(Optional.of(perfume));
        when(perfumeRepository.save(any(Perfume.class))).thenReturn(perfume);

        // When
        Optional<Perfume> updatedPerfume = perfumeService.updatePerfume(1L, detalles);

        // Then
        assertTrue(updatedPerfume.isPresent());
        assertEquals("Updated Perfume", updatedPerfume.get().getNombre());
        assertEquals("Updated Marca", updatedPerfume.get().getMarca());
        verify(perfumeRepository, times(1)).findById(1L);
        verify(perfumeRepository, times(1)).save(any(Perfume.class));
    }

    @Test
    void testUpdatePerfumeNotFound() {
        // Given
        when(perfumeRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<Perfume> updatedPerfume = perfumeService.updatePerfume(1L, new Perfume());

        // Then
        assertFalse(updatedPerfume.isPresent());
        verify(perfumeRepository, times(1)).findById(1L);
        verify(perfumeRepository, never()).save(any(Perfume.class));
    }

    @Test
    void testDeletePerfumeSuccess() {
        // Given
        when(perfumeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(perfumeRepository).deleteById(1L);

        // When
        boolean isDeleted = perfumeService.deletePerfume(1L);

        // Then
        assertTrue(isDeleted);
        verify(perfumeRepository, times(1)).existsById(1L);
        verify(perfumeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePerfumeNotFound() {
        // Given
        when(perfumeRepository.existsById(1L)).thenReturn(false);

        // When
        boolean isDeleted = perfumeService.deletePerfume(1L);

        // Then
        assertFalse(isDeleted);
        verify(perfumeRepository, times(1)).existsById(1L);
        verify(perfumeRepository, never()).deleteById(anyLong());
    }
}
