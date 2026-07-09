package com.perfulandia.carrito.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.carrito.models.dtos.CarritoItemDTO;
import com.perfulandia.carrito.models.entities.CarritoItem;
import com.perfulandia.carrito.services.CarritoService;
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

@WebMvcTest(CarritoController.class)
@SuppressWarnings("null")
class CarritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarritoService carritoService;

    @Autowired
    private ObjectMapper objectMapper;

    private CarritoItem item;
    private CarritoItemDTO dto;

    @BeforeEach
    void setUp() {
        item = new CarritoItem();
        item.setId(1L);
        item.setUsuarioId(10L);
        item.setPerfumeId(100L);
        item.setNombrePerfume("Chanel");
        item.setPrecio(150.0);
        item.setCantidad(2);

        dto = new CarritoItemDTO();
        dto.setUsuarioId(10L);
        dto.setPerfumeId(100L);
        dto.setNombrePerfume("Chanel");
        dto.setPrecio(150.0);
        dto.setCantidad(2);
    }

    @Test
    void testObtenerCarritoPorUsuario() throws Exception {
        when(carritoService.obtenerCarritoPorUsuario(10L)).thenReturn(Arrays.asList(item));
        mockMvc.perform(get("/api/carrito/usuario/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].perfumeId").value(100L));
    }

    @Test
    void testAgregarItem() throws Exception {
        when(carritoService.agregarItem(any(CarritoItemDTO.class))).thenReturn(item);
        mockMvc.perform(post("/api/carrito")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.perfumeId").value(100L));
    }

    @Test
    void testActualizarCantidad() throws Exception {
        item.setCantidad(5);
        when(carritoService.actualizarCantidad(eq(1L), eq(5))).thenReturn(item);
        mockMvc.perform(put("/api/carrito/1")
                .param("cantidad", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(5));
    }

    @Test
    void testEliminarItem() throws Exception {
        doNothing().when(carritoService).eliminarItem(1L);
        mockMvc.perform(delete("/api/carrito/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testVaciarCarrito() throws Exception {
        doNothing().when(carritoService).vaciarCarrito(10L);
        mockMvc.perform(delete("/api/carrito/usuario/10/vaciar"))
                .andExpect(status().isNoContent());
    }
}
