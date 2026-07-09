package com.perfulandia.envios.repositories;

import com.perfulandia.envios.models.entities.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EnvioRepository extends JpaRepository<Envio, Long> {
    Optional<Envio> findByPedidoId(Long pedidoId);
}
