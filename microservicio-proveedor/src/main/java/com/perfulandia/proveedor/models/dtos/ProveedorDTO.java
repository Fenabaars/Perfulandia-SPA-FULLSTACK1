package com.perfulandia.proveedor.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ProveedorDTO {

    @NotBlank(message = "El RUT es obligatorio")
    // Validación simple para formato de RUT chileno (sin puntos y con guion)
    @Pattern(regexp = "^[0-9]+-[0-9kK]{1}$", message = "El formato del RUT debe ser 12345678-9")
    private String rut;

    @NotBlank(message = "La razón social es obligatoria")
    private String razonSocial;

    @NotBlank(message = "El nombre de contacto es obligatorio")
    private String nombreContacto;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de correo inválido")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    private String direccion;

    // Getters y Setters
    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
    public String getNombreContacto() { return nombreContacto; }
    public void setNombreContacto(String nombreContacto) { this.nombreContacto = nombreContacto; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}