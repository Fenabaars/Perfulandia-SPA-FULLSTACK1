package com.perfulandia.envios.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "envios")
@Data
public class Envio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pedidoId;
    
    private String direccionDestino;
    
    private String estado; // PENDIENTE, EN_TRANSITO, ENTREGADO, CANCELADO
    
    private String empresaTransporte;
    
    private String numeroSeguimiento;
    
    private LocalDateTime fechaActualizacion;
}
