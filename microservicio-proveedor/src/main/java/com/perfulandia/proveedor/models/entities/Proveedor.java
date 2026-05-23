package com.perfulandia.proveedor.models.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String rut;

    @Column(name = "razon_social", nullable = false)
    private String razonSocial;

    @Column(name = "nombre_contacto", nullable = false)
    private String nombreContacto;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String telefono;

    private String direccion;

    public Proveedor() {}

    public Proveedor(String rut, String razonSocial, String nombreContacto, String email, String telefono, String direccion) {
        this.rut = rut;
        this.razonSocial = razonSocial;
        this.nombreContacto = nombreContacto;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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