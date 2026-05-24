package com.perfulandia.carrito.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long usuarioId;

    @Column(nullable = false)
    private LocalDateTime fechaPedido;

    // PENDIENTE, CONFIRMADO, ENVIADO, ENTREGADO, CANCELADO
    @Column(nullable = false)
    private String estado;

    // TARJETA, TRANSFERENCIA, EFECTIVO
    @Column(nullable = false)
    private String metodoPago;

    // DOMICILIO, RETIRO_TIENDA
    @Column(nullable = false)
    private String metodoEnvio;

    @Column(nullable = false)
    private String direccionEnvio;

    @Column(nullable = false)
    private Double total;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetallePedido> detalles = new ArrayList<>();
}
