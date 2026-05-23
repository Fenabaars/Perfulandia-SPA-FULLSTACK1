package com.perfulandia.carrito.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PedidoDTO {

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long usuarioId;

    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago; // TARJETA, TRANSFERENCIA, EFECTIVO

    @NotBlank(message = "El método de envío es obligatorio")
    private String metodoEnvio; // DOMICILIO, RETIRO_TIENDA

    @NotBlank(message = "La dirección de envío es obligatoria")
    private String direccionEnvio;

    @NotEmpty(message = "El pedido debe tener al menos un producto")
    private List<DetallePedidoDTO> detalles;

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getMetodoEnvio() { return metodoEnvio; }
    public void setMetodoEnvio(String metodoEnvio) { this.metodoEnvio = metodoEnvio; }

    public String getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }

    public List<DetallePedidoDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedidoDTO> detalles) { this.detalles = detalles; }
}
