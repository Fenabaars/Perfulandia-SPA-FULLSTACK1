package com.perfulandia.carrito.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.perfulandia.carrito.models.dtos.PedidoDTO;
import com.perfulandia.carrito.models.entities.Pedido;
import com.perfulandia.carrito.services.PedidoService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PedidoService pedidoService;

    private ObjectMapper objectMapper;

    private Pedido pedido;
    private PedidoDTO dto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setUsuarioId(10L);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado("PENDIENTE");
        pedido.setTotal(100.0);

        dto = new PedidoDTO();
        dto.setUsuarioId(10L);
        dto.setDireccionEnvio("Calle 123");
        dto.setMetodoPago("TARJETA");
        dto.setMetodoEnvio("DOMICILIO");
        
        com.perfulandia.carrito.models.dtos.DetallePedidoDTO detalle = new com.perfulandia.carrito.models.dtos.DetallePedidoDTO();
        detalle.setPerfumeId(100L);
        detalle.setNombrePerfume("Chanel");
        detalle.setPrecioUnitario(50.0);
        detalle.setCantidad(2);
        dto.setDetalles(Arrays.asList(detalle));
    }

    @Test
    void testObtenerPorUsuario() throws Exception {
        when(pedidoService.obtenerHistorialPorUsuario(10L)).thenReturn(Arrays.asList(pedido));
        mockMvc.perform(get("/api/pedidos/usuario/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("PENDIENTE"));
    }

    @Test
    void testObtenerPorId() throws Exception {
        when(pedidoService.obtenerPorId(1L)).thenReturn(pedido);
        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void testCrearPedido() throws Exception {
        when(pedidoService.confirmarPedido(any(PedidoDTO.class))).thenReturn(pedido);
        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }
}
