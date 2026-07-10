package com.perfulandia.facturacion.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class BoletaDTO {

    @NotBlank(message = "El tipo de documento es obligatorio (BOLETA o FACTURA)")
    private String tipo; // BOLETA o FACTURA

    // Uno de los dos debe estar presente
    private Long pedidoId;  // ID del pedido web
    private Long ventaId;   // ID de la venta física

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String clienteNombre;

    @NotBlank(message = "El email del cliente es obligatorio")
    private String clienteEmail;

    // Requerido solo para FACTURA
    private String rutCliente;

    @NotEmpty(message = "La boleta debe tener al menos un detalle")
    private List<DetalleBoletaDTO> detalles;

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    public Long getVentaId() { return ventaId; }
    public void setVentaId(Long ventaId) { this.ventaId = ventaId; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }

    public String getRutCliente() { return rutCliente; }
    public void setRutCliente(String rutCliente) { this.rutCliente = rutCliente; }

    public List<DetalleBoletaDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleBoletaDTO> detalles) { this.detalles = detalles; }
}
