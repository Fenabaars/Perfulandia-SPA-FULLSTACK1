package com.perfulandia.resenas.service;

import com.perfulandia.resenas.entity.Resena;
import com.perfulandia.resenas.repository.ResenaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    public Object createResena(Resena resena) {
        if (resena.getCalificacion() == null || resena.getCalificacion() < 1 || resena.getCalificacion() > 5) {
            return "La calificación debe estar entre 1 y 5.";
        }
        
        resena.setFechaCreacion(LocalDate.now());
        return resenaRepository.save(resena);
    }

    public List<Resena> getResenasByProductoId(Long productoId) {
        return resenaRepository.findByProductoId(productoId);
    }

    public boolean deleteResena(Long id) {
        Optional<Resena> resena = resenaRepository.findById(id);
        if (resena.isPresent()) {
            resenaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Resena> getAllResenas() {
        return resenaRepository.findAll();
    }
}
