package com.perfulandia.cliente.service;

import com.perfulandia.cliente.model.Cliente;
import com.perfulandia.cliente.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteService service;

    private Cliente clienteMock;

    @BeforeEach
    void setUp() {
        clienteMock = new Cliente();
        clienteMock.setId(1L);
        clienteMock.setNombre("Juan");
        clienteMock.setApellido("Perez");
        clienteMock.setEmail("juan@test.com");
    }

    @Test
    void givenClienteValido_whenCreateCliente_thenRetornaClienteConFechaRegistro() {
        // Given
        when(repository.save(any(Cliente.class))).thenReturn(clienteMock);

        // When
        Cliente resultado = service.createCliente(clienteMock);

        // Then
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        verify(repository, times(1)).save(any(Cliente.class));
    }

    @Test
    void givenClienteExistente_whenUpdateCliente_thenRetornaClienteActualizado() {
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(clienteMock));
        when(repository.save(any(Cliente.class))).thenReturn(clienteMock);

        Cliente detalles = new Cliente();
        detalles.setTelefono("987654321");

        // When
        Optional<Cliente> resultado = service.updateCliente(1L, detalles);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("987654321", resultado.get().getTelefono());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Cliente.class));
    }
}
