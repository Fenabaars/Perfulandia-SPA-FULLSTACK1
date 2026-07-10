-- Inicialización de Inventario Ficticio para Perfulandia
-- Casa Matriz Meiggs (sucursal_id = 1)
-- Sucursal Sur (sucursal_id = 2)
-- Sucursal Costa (sucursal_id = 3)

-- Perfume ID 1: Chanel No. 5 (Chanel)
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (1, 1, 150);
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (1, 2, 45);
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (1, 3, 30);

-- Perfume ID 2: Sauvage (Dior)
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (2, 1, 200);
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (2, 2, 85);
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (2, 3, 90);

-- Perfume ID 3: Bleu de Chanel (Chanel)
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (3, 1, 110);
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (3, 2, 50);
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (3, 3, 65);

-- Perfume ID 4: J'adore (Dior)
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (4, 1, 95);
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (4, 2, 40);
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (4, 3, 50);

-- Perfume ID 5: 1 Million (Paco Rabanne)
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (5, 1, 180);
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (5, 2, 70);
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (5, 3, 80);

-- Perfume ID 6: La Vie Est Belle (Lancôme)
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (6, 1, 130);
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (6, 2, 60);
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (6, 3, 75);

-- Perfume ID 7: Acqua Di Gio (Giorgio Armani)
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (7, 1, 160);
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (7, 2, 90);
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (7, 3, 120); -- Alto stock en zona costera

-- Perfume ID 8: Good Girl (Carolina Herrera)
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (8, 1, 140);
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (8, 2, 55);
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (8, 3, 70);

-- Perfume ID 9: Aventus (Creed)
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (9, 1, 40); -- Lujoso, menor stock
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (9, 2, 15);
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (9, 3, 10);

-- Perfume ID 10: Black Orchid (Tom Ford)
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (10, 1, 75);
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (10, 2, 25);
INSERT INTO inventarios (perfume_id, sucursal_id, cantidad) VALUES (10, 3, 35);
