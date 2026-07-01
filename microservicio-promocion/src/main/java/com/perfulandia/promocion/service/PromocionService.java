package com.perfulandia.promocion.service;

import com.perfulandia.promocion.entity.Promocion;
import com.perfulandia.promocion.repository.PromocionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PromocionService {

    @Autowired
    private PromocionRepository promocionRepository;

    public List<Promocion> getAllPromotions() {
        return promocionRepository.findAll();
    }

    public Promocion createPromotion(Promocion promocion) {
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
                    return promocionRepository.save(promocion);
                });
    }

    public boolean deletePromotion(Long id) {
        return promocionRepository.findById(id)
                .map(promocion -> {
                    promocionRepository.delete(promocion);
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
