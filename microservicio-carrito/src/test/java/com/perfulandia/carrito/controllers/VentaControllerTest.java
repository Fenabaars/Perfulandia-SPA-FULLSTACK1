package com.perfulandia.carrito.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.perfulandia.carrito.models.dtos.DetalleVentaDTO;
import com.perfulandia.carrito.models.dtos.VentaDTO;
import com.perfulandia.carrito.models.entities.Venta;
import com.perfulandia.carrito.services.VentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VentaController.class)
class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VentaService ventaService;

    private ObjectMapper objectMapper;

    private Venta venta;
    private VentaDTO dto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        venta = new Venta();
        venta.setId(1L);
        venta.setSucursalId(10L);
        venta.setEmpleadoId(5L);
        venta.setFechaVenta(LocalDateTime.now());
        venta.setMetodoPago("EFECTIVO");
        venta.setTotal(100.0);

        dto = new VentaDTO();
        dto.setSucursalId(10L);
        dto.setEmpleadoId(5L);
        dto.setMetodoPago("EFECTIVO");
        
        DetalleVentaDTO det = new DetalleVentaDTO();
        det.setPerfumeId(100L);
        det.setNombrePerfume("Chanel");
        det.setCantidad(2);
        det.setPrecioUnitario(50.0);
        dto.setDetalles(Arrays.asList(det));
    }

    @Test
    void testObtenerTodas() throws Exception {
        when(ventaService.obtenerTodas()).thenReturn(Arrays.asList(venta));
        mockMvc.perform(get("/api/ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sucursalId").value(10L));
    }

    @Test
    void testObtenerPorId() throws Exception {
        when(ventaService.obtenerPorId(1L)).thenReturn(venta);
        mockMvc.perform(get("/api/ventas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sucursalId").value(10L));
    }

    @Test
    void testObtenerPorSucursal() throws Exception {
        when(ventaService.obtenerPorSucursal(10L)).thenReturn(Arrays.asList(venta));
        mockMvc.perform(get("/api/ventas/sucursal/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sucursalId").value(10L));
    }

    @Test
    void testRegistrarVenta() throws Exception {
        when(ventaService.registrarVenta(any(VentaDTO.class))).thenReturn(venta);
        mockMvc.perform(post("/api/ventas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sucursalId").value(10L));
    }

    @Test
    void testEliminarVenta() throws Exception {
        doNothing().when(ventaService).eliminarVenta(1L);
        mockMvc.perform(delete("/api/ventas/1"))
                .andExpect(status().isNoContent());
    }
}
