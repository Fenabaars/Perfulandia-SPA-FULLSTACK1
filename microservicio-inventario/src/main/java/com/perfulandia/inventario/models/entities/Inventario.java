package com.perfulandia.inventario.models.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "inventarios")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "perfume_id", nullable = false)
    private Long perfumeId;

    @Column(name = "sucursal_id", nullable = false)
    private Long sucursalId;

    @Column(nullable = false)
    private Integer cantidad;

    public Inventario() {}

    public Inventario(Long perfumeId, Long sucursalId, Integer cantidad) {
        this.perfumeId = perfumeId;
        this.sucursalId = sucursalId;
        this.cantidad = cantidad;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPerfumeId() { return perfumeId; }
    public void setPerfumeId(Long perfumeId) { this.perfumeId = perfumeId; }
    public Long getSucursalId() { return sucursalId; }
    public void setSucursalId(Long sucursalId) { this.sucursalId = sucursalId; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}