package com.perfulandia.envio.repositories;

import com.perfulandia.envio.models.entities.HistorialEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialEnvioRepository extends JpaRepository<HistorialEnvio, Long> {

    // H23: Historial completo de rastreo de un envío
    List<HistorialEnvio> findByEnvioIdOrderByFechaDesc(Long envioId);
}
