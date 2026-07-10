package com.perfulandia.envio.models.entities;

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
@Table(name = "envios")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Código único de rastreo (ej: ENV-20260523-0001)
    @Column(nullable = false, unique = true)
    private String codigoSeguimiento;

    // ID del pedido web confirmado
    @Column(nullable = false)
    private Long pedidoId;

    @Column(nullable = false)
    private Long clienteId;

    @Column(nullable = false)
    private String clienteNombre;

    @Column(nullable = false)
    private String direccionEnvio;

    // DOMICILIO o RETIRO_TIENDA
    @Column(nullable = false)
    private String metodoEnvio;

    // ID de la sucursal/bodega donde se prepara el pedido
    @Column(nullable = false)
    private Long sucursalId;

    // PREPARACION, TRANSITO, ENTREGADO
    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    // Se completa cuando el estado cambia a ENTREGADO
    private LocalDateTime fechaEntrega;

    @OneToMany(mappedBy = "envio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<HistorialEnvio> historial = new ArrayList<>();
}
