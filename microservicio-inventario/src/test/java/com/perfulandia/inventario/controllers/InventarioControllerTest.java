package com.perfulandia.inventario.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.inventario.models.dtos.InventarioDTO;
import com.perfulandia.inventario.models.entities.Inventario;
import com.perfulandia.inventario.services.InventarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventarioController.class)
@SuppressWarnings("null")
class InventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventarioService service;

    private ObjectMapper objectMapper;

    private Inventario inventario;
    private InventarioDTO dto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        inventario = new Inventario(1L, 2L, 50);
        inventario.setId(100L);

        dto = new InventarioDTO();
        dto.setPerfumeId(1L);
        dto.setSucursalId(2L);
        dto.setCantidad(20);
    }

    @Test
    void testIngresarStock() throws Exception {
        when(service.ingresarStock(any(InventarioDTO.class))).thenReturn(inventario);
        mockMvc.perform(post("/api/inventario/ingreso")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cantidad").value(50));
    }

    @Test
    void testConsultarStock() throws Exception {
        when(service.consultarStock(1L, 2L)).thenReturn(inventario);
        mockMvc.perform(get("/api/inventario/consulta")
                .param("perfumeId", "1")
                .param("sucursalId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(50));
    }

    @Test
    void testListarPorSucursal() throws Exception {
        when(service.listarPorSucursal(2L)).thenReturn(Arrays.asList(inventario));
        mockMvc.perform(get("/api/inventario/sucursal/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cantidad").value(50));
    }

    @Test
    void testDescontarStock() throws Exception {
        when(service.descontarStock(1L, 2L, 10)).thenReturn(inventario);
        mockMvc.perform(patch("/api/inventario/descontar")
                .param("perfumeId", "1")
                .param("sucursalId", "2")
                .param("cantidad", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(50));
    }
}
