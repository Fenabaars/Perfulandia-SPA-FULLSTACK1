package com.perfulandia.usuario.controllers;

import com.perfulandia.usuario.models.dtos.LoginDTO;
import com.perfulandia.usuario.models.dtos.UsuarioDTO;
import com.perfulandia.usuario.models.entities.Usuario;
import com.perfulandia.usuario.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrar(@Valid @RequestBody UsuarioDTO dto) {
        return new ResponseEntity<>(service.registrarUsuario(dto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@Valid @RequestBody LoginDTO dto) {
        return ResponseEntity.ok(service.autenticar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarPerfil(@PathVariable Long id, @Valid @RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(service.actualizarPerfil(id, dto));
    }

    @PatchMapping("/{id}/rol")
    public ResponseEntity<Usuario> asignarRol(@PathVariable Long id, @RequestParam String rol) {
        return ResponseEntity.ok(service.asignarRol(id, rol));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}