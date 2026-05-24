package com.perfulandia.carrito.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carrito_items")
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long usuarioId;

    @Column(nullable = false)
    private Long perfumeId;

    @Column(nullable = false)
    private String nombrePerfume;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer cantidad;
}
