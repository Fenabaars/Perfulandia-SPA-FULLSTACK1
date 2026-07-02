-- H16: Datos iniciales del carrito de compras
INSERT IGNORE INTO carrito_items (usuario_id, perfume_id, nombre_perfume, precio, cantidad)
VALUES (1, 1, 'Chanel No. 5', 89990.0, 2);

INSERT IGNORE INTO carrito_items (usuario_id, perfume_id, nombre_perfume, precio, cantidad)
VALUES (1, 3, 'Dior Sauvage', 75990.0, 1);

-- H17/H19: Datos iniciales de pedidos web
INSERT IGNORE INTO pedidos (usuario_id, fecha_pedido, estado, metodo_pago, metodo_envio, direccion_envio, total)
VALUES (1, '2026-05-20 10:30:00', 'ENTREGADO', 'TARJETA', 'DOMICILIO', 'Av. Providencia 1234, Santiago', 179980.0);

INSERT IGNORE INTO pedidos (usuario_id, fecha_pedido, estado, metodo_pago, metodo_envio, direccion_envio, total)
VALUES (1, '2026-05-22 15:45:00', 'PENDIENTE', 'TRANSFERENCIA', 'RETIRO_TIENDA', 'Sucursal Centro, Santiago', 75990.0);

INSERT IGNORE INTO detalles_pedido (pedido_id, perfume_id, nombre_perfume, precio_unitario, cantidad, subtotal)
VALUES (1, 1, 'Chanel No. 5', 89990.0, 2, 179980.0);

INSERT IGNORE INTO detalles_pedido (pedido_id, perfume_id, nombre_perfume, precio_unitario, cantidad, subtotal)
VALUES (2, 3, 'Dior Sauvage', 75990.0, 1, 75990.0);

-- H18: Datos iniciales de ventas físicas
INSERT IGNORE INTO ventas (sucursal_id, empleado_id, fecha_venta, total, metodo_pago)
VALUES (1, 2, '2026-05-22 11:00:00', 165980.0, 'EFECTIVO');

INSERT IGNORE INTO ventas (sucursal_id, empleado_id, fecha_venta, total, metodo_pago)
VALUES (2, 3, '2026-05-22 16:30:00', 89990.0, 'TARJETA');

INSERT IGNORE INTO detalles_venta (venta_id, perfume_id, nombre_perfume, precio_unitario, cantidad, subtotal)
VALUES (1, 2, 'Giorgio Armani Acqua di Gio', 82990.0, 2, 165980.0);

INSERT IGNORE INTO detalles_venta (venta_id, perfume_id, nombre_perfume, precio_unitario, cantidad, subtotal)
VALUES (2, 1, 'Chanel No. 5', 89990.0, 1, 89990.0);
