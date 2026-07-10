package com.perfulandia.sucursal.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.sucursal.models.dtos.SucursalDTO;
import com.perfulandia.sucursal.models.entities.Sucursal;
import com.perfulandia.sucursal.services.SucursalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SucursalController.class)
class SucursalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SucursalService service;

    private ObjectMapper objectMapper;

    private Sucursal sucursal;
    private SucursalDTO dto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        sucursal = new Sucursal("Sucursal Central", "Calle 1", "Centro", "123456", "9-18");
        sucursal.setId(1L);

        dto = new SucursalDTO();
        dto.setNombre("Sucursal Central");
        dto.setDireccion("Calle 1");
        dto.setComuna("Centro");
        dto.setTelefono("123456");
        dto.setHorarioAtencion("9-18");
    }

    @Test
    void testObtenerTodas() throws Exception {
        when(service.obtenerTodas()).thenReturn(Arrays.asList(sucursal));
        mockMvc.perform(get("/api/sucursales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Sucursal Central"));
    }

    @Test
    void testObtenerPorId() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(sucursal);
        mockMvc.perform(get("/api/sucursales/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Sucursal Central"));
    }

    @Test
    void testBuscarPorComuna() throws Exception {
        when(service.buscarPorComuna("Centro")).thenReturn(Arrays.asList(sucursal));
        mockMvc.perform(get("/api/sucursales/buscar?comuna=Centro"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Sucursal Central"));
    }

    @Test
    void testRegistrar() throws Exception {
        when(service.registrarSucursal(any(SucursalDTO.class))).thenReturn(sucursal);
        mockMvc.perform(post("/api/sucursales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Sucursal Central"));
    }

    @Test
    void testActualizar() throws Exception {
        when(service.actualizarSucursal(eq(1L), any(SucursalDTO.class))).thenReturn(sucursal);
        mockMvc.perform(put("/api/sucursales/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Sucursal Central"));
    }

    @Test
    void testEliminar() throws Exception {
        doNothing().when(service).eliminarSucursal(1L);
        mockMvc.perform(delete("/api/sucursales/1"))
                .andExpect(status().isNoContent());
    }
}
