-- H22/H23: Datos iniciales de envíos

-- Envío 1: En preparación en bodega
INSERT IGNORE INTO envios (codigo_seguimiento, pedido_id, cliente_id, cliente_nombre, direccion_envio, metodo_envio, sucursal_id, estado, fecha_creacion, fecha_actualizacion, fecha_entrega)
VALUES ('ENV-20260522-0001', 2, 1, 'Juan Pérez', 'Sucursal Centro, Santiago', 'RETIRO_TIENDA', 1, 'PREPARACION', '2026-05-22 15:50:00', '2026-05-22 15:50:00', NULL);

-- Envío 2: En tránsito
INSERT IGNORE INTO envios (codigo_seguimiento, pedido_id, cliente_id, cliente_nombre, direccion_envio, metodo_envio, sucursal_id, estado, fecha_creacion, fecha_actualizacion, fecha_entrega)
VALUES ('ENV-20260520-0002', 1, 1, 'Juan Pérez', 'Av. Providencia 1234, Santiago', 'DOMICILIO', 1, 'TRANSITO', '2026-05-20 10:35:00', '2026-05-21 09:00:00', NULL);

-- Envío 3: Entregado
INSERT IGNORE INTO envios (codigo_seguimiento, pedido_id, cliente_id, cliente_nombre, direccion_envio, metodo_envio, sucursal_id, estado, fecha_creacion, fecha_actualizacion, fecha_entrega)
VALUES ('ENV-20260518-0003', 3, 2, 'María López', 'Calle Las Flores 567, Viña del Mar', 'DOMICILIO', 2, 'ENTREGADO', '2026-05-18 14:00:00', '2026-05-20 16:30:00', '2026-05-20 16:30:00');

-- Historial de rastreo del envío 1 (en preparación)
INSERT IGNORE INTO historial_envio (envio_id, estado_anterior, estado_nuevo, fecha, comentario)
VALUES (1, NULL, 'PREPARACION', '2026-05-22 15:50:00', 'Envío creado - Pedido ingresado a bodega para preparación');

-- Historial de rastreo del envío 2 (en tránsito)
INSERT IGNORE INTO historial_envio (envio_id, estado_anterior, estado_nuevo, fecha, comentario)
VALUES (2, NULL, 'PREPARACION', '2026-05-20 10:35:00', 'Envío creado - Pedido ingresado a bodega para preparación');

INSERT IGNORE INTO historial_envio (envio_id, estado_anterior, estado_nuevo, fecha, comentario)
VALUES (2, 'PREPARACION', 'TRANSITO', '2026-05-21 09:00:00', 'Paquete despachado por courier - Seguimiento: ENV-20260520-0002');

-- Historial de rastreo del envío 3 (entregado)
INSERT IGNORE INTO historial_envio (envio_id, estado_anterior, estado_nuevo, fecha, comentario)
VALUES (3, NULL, 'PREPARACION', '2026-05-18 14:00:00', 'Envío creado - Pedido ingresado a bodega para preparación');

INSERT IGNORE INTO historial_envio (envio_id, estado_anterior, estado_nuevo, fecha, comentario)
VALUES (3, 'PREPARACION', 'TRANSITO', '2026-05-19 08:30:00', 'Paquete despachado por courier');

INSERT IGNORE INTO historial_envio (envio_id, estado_anterior, estado_nuevo, fecha, comentario)
VALUES (3, 'TRANSITO', 'ENTREGADO', '2026-05-20 16:30:00', 'Entregado al cliente en dirección de destino');
