package com.perfulandia.microservicio_catalogo.service;

import com.perfulandia.microservicio_catalogo.model.Perfume;
import com.perfulandia.microservicio_catalogo.repository.PerfumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PerfumeService {

    @Autowired
    private PerfumeRepository perfumeRepository;

    public List<Perfume> getAllPerfumes() {
        return perfumeRepository.findAll();
    }

    public Optional<Perfume> getPerfumeById(Long id) {
        return perfumeRepository.findById(id);
    }

    public Perfume createPerfume(Perfume perfume) {
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
            
            return Optional.of(perfumeRepository.save(perfume));
        }
        return Optional.empty();
    }

    public boolean deletePerfume(Long id) {
        if (perfumeRepository.existsById(id)) {
            perfumeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
