package com.perfulandia.carrito.models.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DetallePedidoDTO {

    @NotNull(message = "El ID del perfume es obligatorio")
    private Long perfumeId;

    @NotBlank(message = "El nombre del perfume es obligatorio")
    private String nombrePerfume;

    @NotNull(message = "El precio unitario es obligatorio")
    @Positive(message = "El precio debe ser mayor que cero")
    private Double precioUnitario;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima es 1")
    private Integer cantidad;

    public Long getPerfumeId() { return perfumeId; }
    public void setPerfumeId(Long perfumeId) { this.perfumeId = perfumeId; }

    public String getNombrePerfume() { return nombrePerfume; }
    public void setNombrePerfume(String nombrePerfume) { this.nombrePerfume = nombrePerfume; }

    public Double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
