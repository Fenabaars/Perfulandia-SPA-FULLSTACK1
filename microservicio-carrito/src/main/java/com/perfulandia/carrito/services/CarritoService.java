package com.perfulandia.carrito.services;

import com.perfulandia.carrito.models.dtos.CarritoItemDTO;
import com.perfulandia.carrito.models.entities.CarritoItem;
import com.perfulandia.carrito.repositories.CarritoItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarritoService {

    private static final Logger logger = LoggerFactory.getLogger(CarritoService.class);
    private final CarritoItemRepository repository;

    public CarritoService(CarritoItemRepository repository) {
        this.repository = repository;
    }

    // H16: Obtener el carrito de un usuario
    public List<CarritoItem> obtenerCarritoPorUsuario(Long usuarioId) {
        logger.info("Obteniendo carrito del usuario ID: {}", usuarioId);
        return repository.findByUsuarioId(usuarioId);
    }

    // H16: Agregar producto al carrito
    public CarritoItem agregarItem(CarritoItemDTO dto) {
        logger.info("Agregando perfume ID {} al carrito del usuario ID: {}", dto.getPerfumeId(), dto.getUsuarioId());

        // Si el perfume ya existe en el carrito, incrementar cantidad
        List<CarritoItem> items = repository.findByUsuarioId(dto.getUsuarioId());
        for (CarritoItem item : items) {
            if (item.getPerfumeId().equals(dto.getPerfumeId())) {
                logger.info("Perfume ya en carrito, incrementando cantidad");
                item.setCantidad(item.getCantidad() + dto.getCantidad());
                return repository.save(item);
            }
        }

        CarritoItem nuevoItem = new CarritoItem();
        nuevoItem.setUsuarioId(dto.getUsuarioId());
        nuevoItem.setPerfumeId(dto.getPerfumeId());
        nuevoItem.setNombrePerfume(dto.getNombrePerfume());
        nuevoItem.setPrecio(dto.getPrecio());
        nuevoItem.setCantidad(dto.getCantidad());
        return repository.save(nuevoItem);
    }

    // H16: Actualizar cantidad de un ítem
    public CarritoItem actualizarCantidad(Long id, Integer nuevaCantidad) {
        CarritoItem item = repository.findById(id).orElseThrow(() -> {
            logger.error("Item de carrito no encontrado con ID: {}", id);
            return new RuntimeException("Item no encontrado en el carrito");
        });
        if (nuevaCantidad < 1) {
            throw new RuntimeException("La cantidad mínima es 1");
        }
        logger.info("Actualizando cantidad del item ID {} a {}", id, nuevaCantidad);
        item.setCantidad(nuevaCantidad);
        return repository.save(item);
    }

    // H16: Quitar un producto del carrito
    public void eliminarItem(Long id) {
        CarritoItem item = repository.findById(id).orElseThrow(() -> {
            logger.error("Item de carrito no encontrado con ID: {}", id);
            return new RuntimeException("Item no encontrado en el carrito");
        });
        repository.delete(item);
        logger.info("Item ID {} eliminado del carrito", id);
    }

    // H16: Vaciar todo el carrito (después de confirmar pedido)
    @Transactional
    public void vaciarCarrito(Long usuarioId) {
        logger.info("Vaciando carrito del usuario ID: {}", usuarioId);
        repository.deleteByUsuarioId(usuarioId);
    }
}
