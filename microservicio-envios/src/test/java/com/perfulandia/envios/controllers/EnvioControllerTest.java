package com.perfulandia.envios.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.envios.dto.EnvioDTO;
import com.perfulandia.envios.models.entities.Envio;
import com.perfulandia.envios.services.EnvioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnvioController.class)
class EnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EnvioService service;

    private ObjectMapper objectMapper;

    private Envio envio;
    private EnvioDTO dto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        envio = new Envio();
        envio.setId(1L);
        envio.setPedidoId(10L);
        envio.setEmpresaTransporte("Starken");
        envio.setNumeroSeguimiento("12345");
        envio.setEstado("EN_PREPARACION");
        envio.setFechaActualizacion(LocalDateTime.now());

        dto = new EnvioDTO();
        dto.setPedidoId(10L);
        dto.setEmpresaTransporte("Starken");
        dto.setNumeroSeguimiento("12345");
        dto.setDireccionDestino("Calle 1");
        dto.setEstado("EN_PREPARACION");
    }

    @Test
    void testObtenerTodos() throws Exception {
        when(service.obtenerTodos()).thenReturn(Arrays.asList(envio));
        mockMvc.perform(get("/api/envios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].empresaTransporte").value("Starken"));
    }

    @Test
    void testObtenerPorId() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(envio);
        mockMvc.perform(get("/api/envios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empresaTransporte").value("Starken"));
    }

    @Test
    void testObtenerPorPedidoId() throws Exception {
        when(service.obtenerPorPedidoId(10L)).thenReturn(envio);
        mockMvc.perform(get("/api/envios/pedido/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empresaTransporte").value("Starken"));
    }

    @Test
    void testRegistrar() throws Exception {
        when(service.registrarEnvio(any(Envio.class))).thenReturn(envio);
        mockMvc.perform(post("/api/envios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.empresaTransporte").value("Starken"));
    }

    @Test
    void testActualizarEstado() throws Exception {
        when(service.actualizarEstado(eq(1L), eq("ENVIADO"))).thenReturn(envio);
        mockMvc.perform(patch("/api/envios/1/estado")
                .param("estado", "ENVIADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empresaTransporte").value("Starken"));
    }
}
