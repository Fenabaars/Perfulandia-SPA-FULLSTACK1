package com.perfulandia.inventario.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;

@FeignClient(name = "sucursal-service", url = "http://localhost:8083/api/sucursales")
public interface SucursalClient {
    @GetMapping("/{id}")
    Map<String, Object> getSucursalById(@PathVariable("id") Long id);
}