package com.perfulandia.inventario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients // ¡Obligatorio para que funcione la comunicación!
public class MicroservicioInventarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicioInventarioApplication.class, args);
    }
}