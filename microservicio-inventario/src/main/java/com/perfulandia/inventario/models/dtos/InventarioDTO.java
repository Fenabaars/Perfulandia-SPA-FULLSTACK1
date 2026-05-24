package com.perfulandia.inventario.models.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class InventarioDTO {

    @NotNull(message = "El ID del perfume es obligatorio")
    private Long perfumeId;

    @NotNull(message = "El ID de la sucursal es obligatorio")
    private Long sucursalId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad a ingresar debe ser mayor a cero")
    private Integer cantidad;

    // Getters y Setters
    public Long getPerfumeId() { return perfumeId; }
    public void setPerfumeId(Long perfumeId) { this.perfumeId = perfumeId; }
    public Long getSucursalId() { return sucursalId; }
    public void setSucursalId(Long sucursalId) { this.sucursalId = sucursalId; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}