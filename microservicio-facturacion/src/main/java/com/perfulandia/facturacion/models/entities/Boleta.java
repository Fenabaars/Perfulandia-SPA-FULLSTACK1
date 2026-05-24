package com.perfulandia.facturacion.models.entities;

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
@Table(name = "boletas")
public class Boleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Número único de boleta (ej: BOL-20260523-0001)
    @Column(nullable = false, unique = true)
    private String numeroBoleta;

    // BOLETA o FACTURA
    @Column(nullable = false)
    private String tipo;

    // ID del pedido web asociado (puede ser null si es venta física)
    private Long pedidoId;

    // ID de la venta física asociada (puede ser null si es pedido web)
    private Long ventaId;

    @Column(nullable = false)
    private Long clienteId;

    @Column(nullable = false)
    private String clienteNombre;

    @Column(nullable = false)
    private String clienteEmail;

    // RUT del cliente (requerido para FACTURA)
    private String rutCliente;

    @Column(nullable = false)
    private LocalDateTime fechaEmision;

    @Column(nullable = false)
    private Double subtotal;

    // IVA 19%
    @Column(nullable = false)
    private Double impuesto;

    @Column(nullable = false)
    private Double total;

    // EMITIDA, ENVIADA, ANULADA
    @Column(nullable = false)
    private String estado;

    @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetalleBoleta> detalles = new ArrayList<>();
}
