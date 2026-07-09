package com.perfulandia.usuario.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.usuario.models.dtos.LoginDTO;
import com.perfulandia.usuario.models.dtos.UsuarioDTO;
import com.perfulandia.usuario.models.entities.Usuario;
import com.perfulandia.usuario.services.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService service;

    private ObjectMapper objectMapper;

    private Usuario usuario;
    private UsuarioDTO dto;
    private LoginDTO loginDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        usuario = new Usuario("Juan", "juan@test.com", "password123", "calle 1", "CLIENTE");
        usuario.setId(1L);

        dto = new UsuarioDTO();
        dto.setNombre("Juan");
        dto.setEmail("juan@test.com");
        dto.setPassword("password123");
        dto.setDireccion("calle 1");
        dto.setRol("CLIENTE");

        loginDTO = new LoginDTO();
        loginDTO.setEmail("juan@test.com");
        loginDTO.setPassword("password123");
    }

    @Test
    void testObtenerTodos() throws Exception {
        when(service.obtenerTodos()).thenReturn(Arrays.asList(usuario));
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }

    @Test
    void testObtenerPorId() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(usuario);
        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void testRegistrar() throws Exception {
        when(service.registrarUsuario(any(UsuarioDTO.class))).thenReturn(usuario);
        mockMvc.perform(post("/api/usuarios/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void testLogin() throws Exception {
        when(service.autenticar(any(LoginDTO.class))).thenReturn(usuario);
        mockMvc.perform(post("/api/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void testActualizarPerfil() throws Exception {
        when(service.actualizarPerfil(eq(1L), any(UsuarioDTO.class))).thenReturn(usuario);
        mockMvc.perform(put("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void testAsignarRol() throws Exception {
        when(service.asignarRol(eq(1L), eq("ADMIN"))).thenReturn(usuario);
        mockMvc.perform(patch("/api/usuarios/1/rol")
                .param("rol", "ADMIN"))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminar() throws Exception {
        doNothing().when(service).eliminarUsuario(1L);
        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());
    }
}
