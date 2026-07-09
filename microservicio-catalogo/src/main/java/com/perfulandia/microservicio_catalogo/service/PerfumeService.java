package com.perfulandia.microservicio_catalogo.service;

import com.perfulandia.microservicio_catalogo.model.Perfume;
import com.perfulandia.microservicio_catalogo.repository.PerfumeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PerfumeService {

    private final PerfumeRepository perfumeRepository;

    public PerfumeService(PerfumeRepository perfumeRepository) {
        this.perfumeRepository = perfumeRepository;
    }

    public List<Perfume> getAllPerfumes() {
        return perfumeRepository.findAll();
    }

    public Optional<Perfume> getPerfumeById(Long id) {
        return perfumeRepository.findById(id);
    }

    public Perfume createPerfume(Perfume perfume) {
        log.info("Guardando perfume en base de datos: {}", perfume.getNombre());
        return perfumeRepository.save(perfume);
    }

    public Optional<Perfume> updatePerfume(Long id, Perfume perfumeDetalles) {
        Optional<Perfume> perfumeOptional = perfumeRepository.findById(id);

        if (perfumeOptional.isPresent()) {
            Perfume perfume = perfumeOptional.get();
            perfume.setNombre(perfumeDetalles.getNombre());
            perfume.setMarca(perfumeDetalles.getMarca());
            perfume.setCategoria(perfumeDetalles.getCategoria());
            perfume.setDescripcion(perfumeDetalles.getDescripcion());
            perfume.setPrecio(perfumeDetalles.getPrecio());
            perfume.setNotasOlfativas(perfumeDetalles.getNotasOlfativas());
            
            log.info("Perfume actualizado correctamente en BD con id: {}", id);
            return Optional.of(perfumeRepository.save(perfume));
        }
        log.warn("Intento de actualizar perfume inexistente con id: {}", id);
        return Optional.empty();
    }

    public boolean deletePerfume(Long id) {
        if (perfumeRepository.existsById(id)) {
            perfumeRepository.deleteById(id);
            log.info("Perfume eliminado con id: {}", id);
            return true;
        }
        log.warn("Intento de eliminar perfume inexistente con id: {}", id);
        return false;
    }
}
