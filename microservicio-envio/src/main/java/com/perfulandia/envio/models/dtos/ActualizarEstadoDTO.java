package com.perfulandia.envio.models.dtos;

import jakarta.validation.constraints.NotBlank;

public class ActualizarEstadoDTO {

    @NotBlank(message = "El estado es obligatorio (PREPARACION, TRANSITO, ENTREGADO)")
    private String estado; // PREPARACION, TRANSITO, ENTREGADO

    // Nota opcional del encargado de logística
    private String comentario;

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}
