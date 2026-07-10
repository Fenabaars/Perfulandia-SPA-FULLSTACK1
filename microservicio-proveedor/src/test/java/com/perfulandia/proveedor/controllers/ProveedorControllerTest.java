package com.perfulandia.proveedor.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.proveedor.models.dtos.ProveedorDTO;
import com.perfulandia.proveedor.models.entities.Proveedor;
import com.perfulandia.proveedor.services.ProveedorService;
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

@WebMvcTest(ProveedorController.class)
class ProveedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProveedorService service;

    private ObjectMapper objectMapper;

    private Proveedor proveedor;
    private ProveedorDTO dto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        proveedor = new Proveedor("12345678-9", "Empresa SA", "Juan", "juan@empresa.com", "123456", "Dir 1");
        proveedor.setId(1L);

        dto = new ProveedorDTO();
        dto.setRut("12345678-9");
        dto.setRazonSocial("Empresa SA");
        dto.setNombreContacto("Juan");
        dto.setEmail("juan@empresa.com");
        dto.setTelefono("123456");
        dto.setDireccion("Dir 1");
    }

    @Test
    void testObtenerTodos() throws Exception {
        when(service.obtenerTodos()).thenReturn(Arrays.asList(proveedor));
        mockMvc.perform(get("/api/proveedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rut").value("12345678-9"));
    }

    @Test
    void testObtenerPorId() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(proveedor);
        mockMvc.perform(get("/api/proveedores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    void testObtenerPorRut() throws Exception {
        when(service.obtenerPorRut("12345678-9")).thenReturn(proveedor);
        mockMvc.perform(get("/api/proveedores/buscar?rut=12345678-9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    void testRegistrar() throws Exception {
        when(service.registrarProveedor(any(ProveedorDTO.class))).thenReturn(proveedor);
        mockMvc.perform(post("/api/proveedores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    void testActualizar() throws Exception {
        when(service.actualizarProveedor(eq(1L), any(ProveedorDTO.class))).thenReturn(proveedor);
        mockMvc.perform(put("/api/proveedores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    void testEliminar() throws Exception {
        doNothing().when(service).eliminarProveedor(1L);
        mockMvc.perform(delete("/api/proveedores/1"))
                .andExpect(status().isNoContent());
    }
}
