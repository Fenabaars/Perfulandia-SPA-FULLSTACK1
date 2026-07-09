package com.perfulandia.resenas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.perfulandia.resenas.dto.ResenaDTO;
import com.perfulandia.resenas.entity.Resena;
import com.perfulandia.resenas.service.ResenaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResenaController.class)
class ResenaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResenaService resenaService;

    private ObjectMapper objectMapper;
    private Resena resena;
    private ResenaDTO resenaDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        resena = new Resena();
        resena.setId(1L);
        resena.setUsuarioId(10L);
        resena.setProductoId(100L);
        resena.setCalificacion(5);
        resena.setComentario("Excelente");

        resenaDTO = new ResenaDTO();
        resenaDTO.setUsuarioId(10L);
        resenaDTO.setProductoId(100L);
        resenaDTO.setCalificacion(5);
        resenaDTO.setComentario("Excelente");
    }

    @Test
    void testCreateResenaSuccess() throws Exception {
        when(resenaService.createResena(any(Resena.class))).thenReturn(resena);

        mockMvc.perform(post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resenaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testCreateResenaBadRequest() throws Exception {
        when(resenaService.createResena(any(Resena.class))).thenReturn("La calificación debe estar entre 1 y 5.");

        mockMvc.perform(post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resenaDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("La calificación debe estar entre 1 y 5."));
    }

    @Test
    void testGetResenasByProductoId() throws Exception {
        when(resenaService.getResenasByProductoId(100L)).thenReturn(Arrays.asList(resena));

        mockMvc.perform(get("/api/reviews/product/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testDeleteResenaSuccess() throws Exception {
        when(resenaService.deleteResena(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/reviews/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteResenaNotFound() throws Exception {
        when(resenaService.deleteResena(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/reviews/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllResenas() throws Exception {
        when(resenaService.getAllResenas()).thenReturn(Arrays.asList(resena));

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }
}
