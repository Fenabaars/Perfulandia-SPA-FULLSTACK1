package com.perfulandia.envio.services;

import com.perfulandia.envio.models.dtos.ActualizarEstadoDTO;
import com.perfulandia.envio.models.dtos.EnvioDTO;
import com.perfulandia.envio.models.entities.Envio;
import com.perfulandia.envio.models.entities.HistorialEnvio;
import com.perfulandia.envio.repositories.EnvioRepository;
import com.perfulandia.envio.repositories.HistorialEnvioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class EnvioService {

    private static final Logger logger = LoggerFactory.getLogger(EnvioService.class);

    // Flujo de estados válido: PREPARACION -> TRANSITO -> ENTREGADO
    private static final Map<String, Set<String>> TRANSICIONES_VALIDAS = Map.of(
            "PREPARACION", Set.of("TRANSITO"),
            "TRANSITO", Set.of("ENTREGADO")
    );

    private final EnvioRepository envioRepository;
    private final HistorialEnvioRepository historialRepository;

    public EnvioService(EnvioRepository envioRepository, HistorialEnvioRepository historialRepository) {
        this.envioRepository = envioRepository;
        this.historialRepository = historialRepository;
    }

    // ==================== CONSULTAS ====================

    public List<Envio> obtenerTodos() {
        logger.info("Obteniendo todos los envíos");
        return envioRepository.findAll();
    }

    public Envio obtenerPorId(Long id) {
        return envioRepository.findById(id).orElseThrow(() -> {
            logger.error("Envío no encontrado con ID: {}", id);
            return new RuntimeException("Envío no encontrado");
        });
    }

    // H22: Ver pedidos por estado (ej: PREPARACION para la bodega)
    public List<Envio> obtenerPorEstado(String estado) {
        logger.info("Obteniendo envíos con estado: {}", estado);
        return envioRepository.findByEstadoOrderByFechaCreacionAsc(estado.toUpperCase());
    }

    // H23: Listar envíos del cliente para rastreo
    public List<Envio> obtenerPorCliente(Long clienteId) {
        logger.info("Obteniendo envíos del cliente ID: {}", clienteId);
        return envioRepository.findByClienteIdOrderByFechaCreacionDesc(clienteId);
    }

    // H23: Rastrear por código de seguimiento
    public Envio obtenerPorCodigoSeguimiento(String codigo) {
        return envioRepository.findByCodigoSeguimiento(codigo).orElseThrow(() -> {
            logger.error("Envío no encontrado con código de seguimiento: {}", codigo);
            return new RuntimeException("Envío no encontrado con el código de seguimiento proporcionado");
        });
    }

    // Buscar envío de un pedido
    public Envio obtenerPorPedido(Long pedidoId) {
        return envioRepository.findByPedidoId(pedidoId).orElseThrow(() -> {
            logger.error("Envío no encontrado para pedido ID: {}", pedidoId);
            return new RuntimeException("Envío no encontrado para el pedido especificado");
        });
    }

    // H22: Envíos por sucursal/bodega
    public List<Envio> obtenerPorSucursal(Long sucursalId) {
        logger.info("Obteniendo envíos de la sucursal/bodega ID: {}", sucursalId);
        return envioRepository.findBySucursalIdOrderByFechaCreacionAsc(sucursalId);
    }

    // H23: Historial de rastreo de un envío
    public List<HistorialEnvio> obtenerHistorial(Long envioId) {
        // Validar que el envío existe
        obtenerPorId(envioId);
        logger.info("Obteniendo historial de rastreo del envío ID: {}", envioId);
        return historialRepository.findByEnvioIdOrderByFechaDesc(envioId);
    }

    // ==================== H22: CREAR ENVÍO ====================

    @Transactional
    public Envio crearEnvio(EnvioDTO dto) {
        logger.info("Creando envío para pedido ID: {} - Cliente: {}", dto.getPedidoId(), dto.getClienteNombre());

        // Validar que no exista ya un envío para este pedido
        if (envioRepository.findByPedidoId(dto.getPedidoId()).isPresent()) {
            throw new RuntimeException("Ya existe un envío para el pedido ID: " + dto.getPedidoId());
        }

        LocalDateTime ahora = LocalDateTime.now();

        Envio envio = new Envio();
        envio.setCodigoSeguimiento(generarCodigoSeguimiento());
        envio.setPedidoId(dto.getPedidoId());
        envio.setClienteId(dto.getClienteId());
        envio.setClienteNombre(dto.getClienteNombre());
        envio.setDireccionEnvio(dto.getDireccionEnvio());
        envio.setMetodoEnvio(dto.getMetodoEnvio().toUpperCase());
        envio.setSucursalId(dto.getSucursalId());
        envio.setEstado("PREPARACION");
        envio.setFechaCreacion(ahora);
        envio.setFechaActualizacion(ahora);

        // Registrar en historial
        HistorialEnvio historial = new HistorialEnvio();
        historial.setEnvio(envio);
        historial.setEstadoAnterior(null);
        historial.setEstadoNuevo("PREPARACION");
        historial.setFecha(ahora);
        historial.setComentario("Envío creado - Pedido ingresado a bodega para preparación");
        envio.getHistorial().add(historial);

        Envio guardado = envioRepository.save(envio);
        logger.info("Envío creado exitosamente - Código: {} | Estado: PREPARACION | Sucursal: {}",
                guardado.getCodigoSeguimiento(), guardado.getSucursalId());

        return guardado;
    }

    // ==================== H23: ACTUALIZAR ESTADO ====================

    @Transactional
    public Envio actualizarEstado(Long envioId, ActualizarEstadoDTO dto) {
        Envio envio = obtenerPorId(envioId);
        String estadoActual = envio.getEstado();
        String nuevoEstado = dto.getEstado().toUpperCase();

        // Validar que el estado ya no sea ENTREGADO
        if ("ENTREGADO".equals(estadoActual)) {
            throw new RuntimeException("El envío ya fue entregado, no se puede cambiar el estado");
        }

        // Validar transición de estado
        Set<String> estadosPermitidos = TRANSICIONES_VALIDAS.get(estadoActual);
        if (estadosPermitidos == null || !estadosPermitidos.contains(nuevoEstado)) {
            throw new RuntimeException(
                    String.format("Transición de estado no válida: %s → %s. Estados permitidos: %s",
                            estadoActual, nuevoEstado,
                            estadosPermitidos != null ? estadosPermitidos : "ninguno"));
        }

        logger.info("Actualizando envío {} de {} a {}", envio.getCodigoSeguimiento(), estadoActual, nuevoEstado);

        LocalDateTime ahora = LocalDateTime.now();

        // Actualizar estado
        envio.setEstado(nuevoEstado);
        envio.setFechaActualizacion(ahora);

        // Si se entrega, registrar fecha de entrega
        if ("ENTREGADO".equals(nuevoEstado)) {
            envio.setFechaEntrega(ahora);
        }

        // Registrar en historial
        HistorialEnvio historial = new HistorialEnvio();
        historial.setEnvio(envio);
        historial.setEstadoAnterior(estadoActual);
        historial.setEstadoNuevo(nuevoEstado);
        historial.setFecha(ahora);
        historial.setComentario(dto.getComentario() != null ? dto.getComentario() : "Estado actualizado");
        envio.getHistorial().add(historial);

        Envio actualizado = envioRepository.save(envio);
        logger.info("Envío {} actualizado a {} exitosamente", actualizado.getCodigoSeguimiento(), nuevoEstado);

        return actualizado;
    }

    // ==================== ELIMINACIÓN ====================

    public void eliminarEnvio(Long id) {
        Envio envio = obtenerPorId(id);
        envioRepository.delete(envio);
        logger.info("Envío {} (ID: {}) eliminado del sistema", envio.getCodigoSeguimiento(), id);
    }

    // ==================== UTILIDADES ====================

    /**
     * Genera un código de seguimiento único con formato: ENV-YYYYMMDD-XXXX
     */
    private String generarCodigoSeguimiento() {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = envioRepository.count() + 1;
        return String.format("ENV-%s-%04d", fecha, count);
    }
}
