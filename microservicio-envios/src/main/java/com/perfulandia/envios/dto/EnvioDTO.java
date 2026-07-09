package com.perfulandia.envios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnvioDTO {
    @NotNull(message = "El id del pedido es obligatorio")
    private Long pedidoId;
    
    @NotBlank(message = "La dirección de destino es obligatoria")
    private String direccionDestino;
    
    @NotBlank(message = "El estado es obligatorio")
    private String estado; // PENDIENTE, EN_TRANSITO, ENTREGADO, CANCELADO
    
    @NotBlank(message = "La empresa de transporte es obligatoria")
    private String empresaTransporte;
    
    private String numeroSeguimiento;
}
