-- H20: Datos iniciales de boletas generadas
INSERT IGNORE INTO boletas (numero_boleta, tipo, pedido_id, venta_id, cliente_id, cliente_nombre, cliente_email, rut_cliente, fecha_emision, subtotal, impuesto, total, estado)
VALUES ('BOL-20260520-0001', 'BOLETA', 1, NULL, 1, 'Juan Pérez', 'juan.perez@email.com', NULL, '2026-05-20 10:35:00', 179980.0, 34196.2, 214176.2, 'ENVIADA');

INSERT IGNORE INTO boletas (numero_boleta, tipo, pedido_id, venta_id, cliente_id, cliente_nombre, cliente_email, rut_cliente, fecha_emision, subtotal, impuesto, total, estado)
VALUES ('BOL-20260522-0002', 'BOLETA', NULL, 1, 2, 'María López', 'maria.lopez@email.com', NULL, '2026-05-22 11:05:00', 165980.0, 31536.2, 197516.2, 'EMITIDA');

INSERT IGNORE INTO boletas (numero_boleta, tipo, pedido_id, venta_id, cliente_id, cliente_nombre, cliente_email, rut_cliente, fecha_emision, subtotal, impuesto, total, estado)
VALUES ('FAC-20260522-0003', 'FACTURA', NULL, 2, 3, 'Empresa ABC Ltda.', 'contacto@empresaabc.cl', '76.123.456-7', '2026-05-22 16:35:00', 89990.0, 17098.1, 107088.1, 'ENVIADA');

-- Detalles de boleta 1 (pedido web)
INSERT IGNORE INTO detalles_boleta (boleta_id, perfume_id, descripcion, cantidad, precio_unitario, subtotal)
VALUES (1, 1, 'Chanel No. 5 - Eau de Parfum 100ml', 2, 89990.0, 179980.0);

-- Detalles de boleta 2 (venta física)
INSERT IGNORE INTO detalles_boleta (boleta_id, perfume_id, descripcion, cantidad, precio_unitario, subtotal)
VALUES (2, 2, 'Giorgio Armani Acqua di Gio - Eau de Toilette 100ml', 2, 82990.0, 165980.0);

-- Detalles de boleta 3 (factura - venta física)
INSERT IGNORE INTO detalles_boleta (boleta_id, perfume_id, descripcion, cantidad, precio_unitario, subtotal)
VALUES (3, 1, 'Chanel No. 5 - Eau de Parfum 100ml', 1, 89990.0, 89990.0);
