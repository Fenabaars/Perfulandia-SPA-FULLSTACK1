package com.perfulandia.inventario.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;

@FeignClient(name = "catalog-service", url = "http://localhost:8081/api/catalog") // URL corregida
public interface PerfumeClient {
    @GetMapping("/{id}")
    Map<String, Object> getPerfumeById(@PathVariable("id") Long id);
}