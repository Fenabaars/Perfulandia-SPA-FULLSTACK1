package com.perfulandia.inventario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MicroservicioInventarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicioInventarioApplication.class, args);
    }
}