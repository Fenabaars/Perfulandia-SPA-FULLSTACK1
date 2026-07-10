package com.perfulandia.inventario.exceptions;

import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleValidations_shouldReturnBadRequest() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(
                new FieldError("inventarioDTO", "cantidad", "La cantidad es obligatoria")
        ));

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidations(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("La cantidad es obligatoria", response.getBody().get("cantidad"));
    }

    @Test
    void handleRuntimeExceptions_insuficiente_shouldReturnConflict() {
        RuntimeException ex = new RuntimeException("Stock insuficiente para realizar la reserva o venta.");
        
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleRuntimeExceptions(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Stock insuficiente para realizar la reserva o venta.", response.getBody().get("error"));
    }

    @Test
    void handleRuntimeExceptions_other_shouldReturnNotFound() {
        RuntimeException ex = new RuntimeException("El perfume indicado no existe en el sistema.");
        
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleRuntimeExceptions(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("El perfume indicado no existe en el sistema.", response.getBody().get("error"));
    }

    @Test
    void handleFeignExceptions_shouldReturnServiceUnavailable() {
        FeignException ex = mock(FeignException.class);
        
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleFeignExceptions(ex);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("Error de comunicación con servicio externo. Verifica que el Catálogo y la Sucursal estén corriendo.", response.getBody().get("error"));
    }
}
