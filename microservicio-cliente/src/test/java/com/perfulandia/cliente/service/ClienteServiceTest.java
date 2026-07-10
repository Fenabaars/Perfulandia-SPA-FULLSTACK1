package com.perfulandia.cliente.service;

import com.perfulandia.cliente.model.Cliente;
import com.perfulandia.cliente.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan");
        cliente.setApellido("Perez");
        cliente.setEmail("juan@test.com");
        cliente.setTelefono("123456789");
        cliente.setDireccion("Calle 123");
    }

    @Test
    void testCreateCliente() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        Cliente result = clienteService.createCliente(new Cliente());
        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testCreateClienteWithDate() {
        cliente.setFechaRegistro(LocalDate.now());
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        Cliente result = clienteService.createCliente(cliente);
        assertNotNull(result.getFechaRegistro());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testGetAllClientes() {
        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente));
        List<Cliente> result = clienteService.getAllClientes();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void testGetClienteById() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        Optional<Cliente> result = clienteService.getClienteById(1L);
        assertTrue(result.isPresent());
        assertEquals("Juan", result.get().getNombre());
    }

    @Test
    void testGetClienteByEmail() {
        when(clienteRepository.findByEmail("juan@test.com")).thenReturn(Optional.of(cliente));
        Optional<Cliente> result = clienteService.getClienteByEmail("juan@test.com");
        assertTrue(result.isPresent());
        assertEquals("juan@test.com", result.get().getEmail());
    }

    @Test
    void testUpdateClienteSuccess() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        
        Cliente detalles = new Cliente();
        detalles.setNombre("Pedro");
        detalles.setApellido("Gomez");
        detalles.setTelefono("987654321");
        detalles.setDireccion("Calle 456");

        Optional<Cliente> result = clienteService.updateCliente(1L, detalles);
        assertTrue(result.isPresent());
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testUpdateClienteNotFound() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Cliente> result = clienteService.updateCliente(1L, new Cliente());
        assertFalse(result.isPresent());
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void testDeleteClienteSuccess() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        doNothing().when(clienteRepository).delete(any(Cliente.class));
        boolean result = clienteService.deleteCliente(1L);
        assertTrue(result);
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).delete(any(Cliente.class));
    }

    @Test
    void testDeleteClienteNotFound() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());
        boolean result = clienteService.deleteCliente(1L);
        assertFalse(result);
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, never()).delete(any(Cliente.class));
    }
}
