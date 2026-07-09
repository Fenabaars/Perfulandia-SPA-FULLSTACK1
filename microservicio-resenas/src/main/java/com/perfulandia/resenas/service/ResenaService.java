package com.perfulandia.resenas.service;

import com.perfulandia.resenas.entity.Resena;
import com.perfulandia.resenas.repository.ResenaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@SuppressWarnings("null")
public class ResenaService {

    private final ResenaRepository resenaRepository;

    public ResenaService(ResenaRepository resenaRepository) {
        this.resenaRepository = resenaRepository;
    }

    public Object createResena(Resena resena) {
        if (resena.getCalificacion() == null || resena.getCalificacion() < 1 || resena.getCalificacion() > 5) {
            log.warn("Fallo validacion de resena: calificacion invalida {}", resena.getCalificacion());
            return "La calificación debe estar entre 1 y 5.";
        }
        
        resena.setFechaCreacion(LocalDate.now());
        log.info("Guardando nueva resena para producto id: {}", resena.getProductoId());
        return resenaRepository.save(resena);
    }

    public List<Resena> getResenasByProductoId(Long productoId) {
        return resenaRepository.findByProductoId(productoId);
    }

    public boolean deleteResena(Long id) {
        Optional<Resena> resena = resenaRepository.findById(id);
        if (resena.isPresent()) {
            resenaRepository.deleteById(id);
            log.info("Resena eliminada id: {}", id);
            return true;
        }
        log.warn("Intento de eliminar resena inexistente id: {}", id);
        return false;
    }

    public List<Resena> getAllResenas() {
        return resenaRepository.findAll();
    }
}
