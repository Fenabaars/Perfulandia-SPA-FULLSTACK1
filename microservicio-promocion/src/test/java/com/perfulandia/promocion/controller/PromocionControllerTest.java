package com.perfulandia.promocion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.perfulandia.promocion.dto.PromocionDTO;
import com.perfulandia.promocion.entity.Promocion;
import com.perfulandia.promocion.service.PromocionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PromocionController.class)
class PromocionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PromocionService promocionService;

    private ObjectMapper objectMapper;
    private Promocion promocion;
    private PromocionDTO promocionDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        promocion = new Promocion();
        promocion.setId(1L);
        promocion.setCodigo("DESC10");
        promocion.setTipoDescuento("PORCENTAJE");
        promocion.setValorDescuento(10.0);
        promocion.setActivo(true);
        promocion.setFechaInicio(LocalDate.now());
        promocion.setFechaFin(LocalDate.now().plusDays(10));

        promocionDTO = new PromocionDTO();
        promocionDTO.setCodigo("DESC10");
        promocionDTO.setTipoDescuento("PORCENTAJE");
        promocionDTO.setValorDescuento(10.0);
        promocionDTO.setFechaInicio(LocalDate.now());
        promocionDTO.setFechaFin(LocalDate.now().plusDays(10));
        promocionDTO.setActivo(true);
    }

    @Test
    void testGetAllPromotions() throws Exception {
        when(promocionService.getAllPromotions()).thenReturn(Arrays.asList(promocion));
        
        mockMvc.perform(get("/api/promotions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value("DESC10"));
    }

    @Test
    void testCreatePromotion() throws Exception {
        when(promocionService.createPromotion(any(Promocion.class))).thenReturn(promocion);

        mockMvc.perform(post("/api/promotions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(promocionDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigo").value("DESC10"));
    }

    @Test
    void testUpdatePromotion() throws Exception {
        when(promocionService.updatePromotion(eq(1L), any(Promocion.class))).thenReturn(Optional.of(promocion));

        mockMvc.perform(put("/api/promotions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(promocionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("DESC10"));
    }
    
    @Test
    void testUpdatePromotionNotFound() throws Exception {
        when(promocionService.updatePromotion(eq(1L), any(Promocion.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/promotions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(promocionDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletePromotionSuccess() throws Exception {
        when(promocionService.deletePromotion(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/promotions/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeletePromotionNotFound() throws Exception {
        when(promocionService.deletePromotion(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/promotions/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testValidatePromotionSuccess() throws Exception {
        when(promocionService.validatePromotion("DESC10")).thenReturn(promocion);

        mockMvc.perform(get("/api/promotions/validate/DESC10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("DESC10"));
    }

    @Test
    void testValidatePromotionNotFound() throws Exception {
        when(promocionService.validatePromotion("NOT_FOUND")).thenReturn("El código promocional no existe.");

        mockMvc.perform(get("/api/promotions/validate/NOT_FOUND"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testValidatePromotionBadRequest() throws Exception {
        when(promocionService.validatePromotion("INACTIVE")).thenReturn("El código promocional está inactivo.");

        mockMvc.perform(get("/api/promotions/validate/INACTIVE"))
                .andExpect(status().isBadRequest());
    }
}
