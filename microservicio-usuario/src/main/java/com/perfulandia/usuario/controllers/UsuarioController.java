package com.perfulandia.usuario.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        log.info("Petición REST recibida en UsuarioController");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        log.info("Petición REST recibida en UsuarioController");
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrar(@Valid @RequestBody UsuarioDTO dto) {
        log.info("Petición REST recibida en UsuarioController");
        return new ResponseEntity<>(service.registrarUsuario(dto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@Valid @RequestBody LoginDTO dto) {
        log.info("Petición REST recibida en UsuarioController");
        return ResponseEntity.ok(service.autenticar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarPerfil(@PathVariable Long id, @Valid @RequestBody UsuarioDTO dto) {
        log.info("Petición REST recibida en UsuarioController");
        return ResponseEntity.ok(service.actualizarPerfil(id, dto));
    }

    @PatchMapping("/{id}/rol")
    public ResponseEntity<Usuario> asignarRol(@PathVariable Long id, @RequestParam String rol) {
        log.info("Petición REST recibida en UsuarioController");
        return ResponseEntity.ok(service.asignarRol(id, rol));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Petición REST recibida en UsuarioController");
        service.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}