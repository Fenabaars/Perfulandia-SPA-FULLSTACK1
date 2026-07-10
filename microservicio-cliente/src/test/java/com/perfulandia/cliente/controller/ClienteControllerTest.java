package com.perfulandia.cliente.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.perfulandia.cliente.dto.ClienteDTO;
import com.perfulandia.cliente.model.Cliente;
import com.perfulandia.cliente.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    private ObjectMapper objectMapper;
    private Cliente cliente;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan");
        cliente.setApellido("Perez");
        cliente.setEmail("juan@test.com");
        cliente.setTelefono("123456789");
        cliente.setDireccion("Calle 123");

        clienteDTO = new ClienteDTO();
        clienteDTO.setNombre("Juan");
        clienteDTO.setApellido("Perez");
        clienteDTO.setEmail("juan@test.com");
        clienteDTO.setTelefono("123456789");
        clienteDTO.setDireccion("Calle 123");
    }

    @Test
    void testCreateCliente() throws Exception {
        when(clienteService.createCliente(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("juan@test.com"));
    }

    @Test
    void testGetAllClientes() throws Exception {
        when(clienteService.getAllClientes()).thenReturn(Arrays.asList(cliente));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("juan@test.com"));
    }

    @Test
    void testGetClienteByIdFound() throws Exception {
        when(clienteService.getClienteById(1L)).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan@test.com"));
    }

    @Test
    void testGetClienteByIdNotFound() throws Exception {
        when(clienteService.getClienteById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetClienteByEmailFound() throws Exception {
        when(clienteService.getClienteByEmail("juan@test.com")).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/api/customers/email/juan@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void testGetClienteByEmailNotFound() throws Exception {
        when(clienteService.getClienteByEmail("juan@test.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/customers/email/juan@test.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateClienteSuccess() throws Exception {
        when(clienteService.updateCliente(eq(1L), any(Cliente.class))).thenReturn(Optional.of(cliente));

        mockMvc.perform(put("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void testUpdateClienteNotFound() throws Exception {
        when(clienteService.updateCliente(eq(1L), any(Cliente.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteClienteSuccess() throws Exception {
        when(clienteService.deleteCliente(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteClienteNotFound() throws Exception {
        when(clienteService.deleteCliente(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNotFound());
    }
}
