package com.perfulandia.promocion.service;

import com.perfulandia.promocion.entity.Promocion;
import com.perfulandia.promocion.repository.PromocionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PromocionService {

    private final PromocionRepository promocionRepository;

    public PromocionService(PromocionRepository promocionRepository) {
        this.promocionRepository = promocionRepository;
    }

    public List<Promocion> getAllPromotions() {
        return promocionRepository.findAll();
    }

    public Promocion createPromotion(Promocion promocion) {
        log.info("Guardando nueva promocion: {}", promocion.getCodigo());
        return promocionRepository.save(promocion);
    }

    public Optional<Promocion> updatePromotion(Long id, Promocion promocionDetails) {
        return promocionRepository.findById(id)
                .map(promocion -> {
                    promocion.setCodigo(promocionDetails.getCodigo());
                    promocion.setTipoDescuento(promocionDetails.getTipoDescuento());
                    promocion.setValorDescuento(promocionDetails.getValorDescuento());
                    promocion.setFechaInicio(promocionDetails.getFechaInicio());
                    promocion.setFechaFin(promocionDetails.getFechaFin());
                    promocion.setActivo(promocionDetails.getActivo());
                    log.info("Actualizada promocion: {}", id);
                    return promocionRepository.save(promocion);
                });
    }

    public boolean deletePromotion(Long id) {
        return promocionRepository.findById(id)
                .map(promocion -> {
                    promocionRepository.delete(promocion);
                    log.info("Promocion eliminada: {}", id);
                    return true;
                })
                .orElse(false);
    }

    public Object validatePromotion(String codigo) {
        Optional<Promocion> promocionOpt = promocionRepository.findByCodigo(codigo);
        
        if (promocionOpt.isEmpty()) {
            return "El código promocional no existe.";
        }
        
        Promocion promocion = promocionOpt.get();
        
        if (!Boolean.TRUE.equals(promocion.getActivo())) {
            return "El código promocional está inactivo.";
        }
        
        LocalDate hoy = LocalDate.now();
        if (hoy.isBefore(promocion.getFechaInicio()) || hoy.isAfter(promocion.getFechaFin())) {
            return "El código promocional está vencido o aún no es válido.";
        }
        
        return promocion;
    }
}
