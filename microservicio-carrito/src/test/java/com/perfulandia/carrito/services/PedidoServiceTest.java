package com.perfulandia.carrito.services;

import com.perfulandia.carrito.models.dtos.DetallePedidoDTO;
import com.perfulandia.carrito.models.dtos.PedidoDTO;
import com.perfulandia.carrito.models.entities.Pedido;
import com.perfulandia.carrito.repositories.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class PedidoServiceTest {

    @Mock
    private PedidoRepository repository;

    @InjectMocks
    private PedidoService service;

    private Pedido pedidoMock;
    private PedidoDTO pedidoDTO;

    @BeforeEach
    void setUp() {
        pedidoMock = new Pedido();
        pedidoMock.setId(1L);
        pedidoMock.setUsuarioId(10L);
        pedidoMock.setEstado("PENDIENTE");
        pedidoMock.setTotal(500.0);

        pedidoDTO = new PedidoDTO();
        pedidoDTO.setUsuarioId(10L);
        pedidoDTO.setMetodoPago("TARJETA");
        pedidoDTO.setMetodoEnvio("EXPRESS");
        pedidoDTO.setDireccionEnvio("Av. Siempreviva 742");
        
        DetallePedidoDTO detalle = new DetallePedidoDTO();
        detalle.setPerfumeId(1L);
        detalle.setNombrePerfume("Chanel N5");
        detalle.setPrecioUnitario(100.0);
        detalle.setCantidad(2);
        
        List<DetallePedidoDTO> detalles = new ArrayList<>();
        detalles.add(detalle);
        pedidoDTO.setDetalles(detalles);
    }

    @Test
    void givenPedidoDTOValido_whenConfirmarPedido_thenCalculaTotalYGuarda() {
        // Given
        when(repository.save(any(Pedido.class))).thenAnswer(i -> {
            Pedido p = i.getArgument(0);
            p.setId(1L);
            return p;
        });

        // When
        Pedido resultado = service.confirmarPedido(pedidoDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("PENDIENTE", resultado.getEstado());
        assertEquals(200.0, resultado.getTotal()); // 100.0 * 2 = 200.0
        assertEquals("TARJETA", resultado.getMetodoPago());
        verify(repository, times(1)).save(any(Pedido.class));
    }

    @Test
    void givenPedidoExistente_whenCancelarPedido_thenCambiaEstadoACancelado() {
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(pedidoMock));
        when(repository.save(any(Pedido.class))).thenReturn(pedidoMock);

        // When
        service.cancelarPedido(1L);

        // Then
        assertEquals("CANCELADO", pedidoMock.getEstado());
        verify(repository, times(1)).save(pedidoMock);
    }

    @Test
    void givenPedidoEntregado_whenCancelarPedido_thenLanzaExcepcion() {
        // Given
        pedidoMock.setEstado("ENTREGADO");
        when(repository.findById(1L)).thenReturn(Optional.of(pedidoMock));

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.cancelarPedido(1L);
        });

        assertEquals("No se puede cancelar un pedido ya entregado", exception.getMessage());
        verify(repository, never()).save(any(Pedido.class));
    }
}
