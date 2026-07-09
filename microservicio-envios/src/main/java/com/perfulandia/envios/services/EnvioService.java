package com.perfulandia.envios.services;

import com.perfulandia.envios.models.entities.Envio;
import com.perfulandia.envios.repositories.EnvioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class EnvioService {

    private final EnvioRepository repository;

    public EnvioService(EnvioRepository repository) {
        this.repository = repository;
    }

    public List<Envio> obtenerTodos() {
        return repository.findAll();
    }

    public Envio obtenerPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Envio no encontrado"));
    }
    
    public Envio obtenerPorPedidoId(Long pedidoId) {
        return repository.findByPedidoId(pedidoId).orElseThrow(() -> new RuntimeException("Envío no encontrado para el pedido " + pedidoId));
    }

    public Envio registrarEnvio(Envio envio) {
        envio.setEstado("PENDIENTE");
        envio.setFechaActualizacion(LocalDateTime.now());
        log.info("Registrando envio en base de datos para pedidoId: {}", envio.getPedidoId());
        return repository.save(envio);
    }

    public Envio actualizarEstado(Long id, String estado) {
        Envio envio = obtenerPorId(id);
        envio.setEstado(estado);
        envio.setFechaActualizacion(LocalDateTime.now());
        log.info("Actualizando estado en base de datos de envio {} a {}", id, estado);
        return repository.save(envio);
    }
}
