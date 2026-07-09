package com.perfulandia.promocion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PromocionDTO {
    @NotBlank(message = "El código es obligatorio")
    private String codigo;

    @NotBlank(message = "El tipo de descuento es obligatorio")
    private String tipoDescuento; // PORCENTAJE o MONTO_FIJO

    @NotNull(message = "El valor del descuento es obligatorio")
    @Positive(message = "El valor del descuento debe ser positivo")
    private Double valorDescuento;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;
}
