package com.perfulandia.inventario.exceptions;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidations(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeExceptions(RuntimeException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        
        HttpStatus status = ex.getMessage().contains("insuficiente") ? HttpStatus.CONFLICT : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(response, status);
    }

    // Manejamos si el otro microservicio está apagado (Connection Refused)
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, String>> handleFeignExceptions(FeignException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Error de comunicación con servicio externo. Verifica que el Catálogo y la Sucursal estén corriendo.");
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
}