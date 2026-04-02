USE ufvshare;

-- Limpieza mínima para poder re-ejecutar este script sin errores de UNIQUE/FK
DELETE FROM mensaje;
DELETE FROM conversacion;
DELETE FROM favorito;
DELETE FROM foto_producto;
DELETE FROM transaccion;
DELETE FROM solicitud;
DELETE FROM reporte_producto;
DELETE FROM reporte_usuario;
DELETE FROM pending_cambio;
DELETE FROM sessions;
DELETE FROM producto;
DELETE FROM usuario;

-- 20 usuarios de prueba
-- contraseña en claro para todos: 1234ASDF
-- SHA-256(1234ASDF): f83940fd01327be194f7f676a10d6fcd24d2c04af7bd0d4eca4491fe530db902
INSERT INTO usuario (id_usuario, nombre, apellidos, correo, telefono, dni, password, es_admin) VALUES
(1, 'Lucia', 'Martinez Gomez', 'admin@ufv.es', '600111001', '00000001A', 'f83940fd01327be194f7f676a10d6fcd24d2c04af7bd0d4eca4491fe530db902', TRUE),
(2, 'Pablo', 'Sanchez Ruiz', 'pablo.sanchez@ufv.es', '600111002', '00000002B', 'f83940fd01327be194f7f676a10d6fcd24d2c04af7bd0d4eca4491fe530db902', FALSE),
(3, 'Marta', 'Lopez Diaz', 'marta.lopez@ufv.es', '600111003', '00000003C', 'f83940fd01327be194f7f676a10d6fcd24d2c04af7bd0d4eca4491fe530db902', FALSE),
(4, 'Carlos', 'Fernandez Gil', 'carlos.fernandez@ufv.es', '600111004', '00000004D', 'f83940fd01327be194f7f676a10d6fcd24d2c04af7bd0d4eca4491fe530db902', FALSE),
(5, 'Elena', 'Navarro Perez', 'elena.navarro@ufv.es', '600111005', '00000005E', 'f83940fd01327be194f7f676a10d6fcd24d2c04af7bd0d4eca4491fe530db902', FALSE),
(6, 'Javier', 'Moreno Torres', 'javier.moreno@ufv.es', '600111006', '00000006F', 'f83940fd01327be194f7f676a10d6fcd24d2c04af7bd0d4eca4491fe530db902', FALSE),
(7, 'Andrea', 'Romero Vega', 'andrea.romero@ufv.es', '600111007', '00000007G', 'f83940fd01327be194f7f676a10d6fcd24d2c04af7bd0d4eca4491fe530db902', FALSE),
(8, 'Diego', 'Hernandez Leon', 'diego.hernandez@ufv.es', '600111008', '00000008H', 'f83940fd01327be194f7f676a10d6fcd24d2c04af7bd0d4eca4491fe530db902', FALSE),
(9, 'Nuria', 'Castro Molina', 'nuria.castro@ufv.es', '600111009', '00000009J', 'f83940fd01327be194f7f676a10d6fcd24d2c04af7bd0d4eca4491fe530db902', FALSE),
(10, 'Alberto', 'Ortega Sanz', 'alberto.ortega@ufv.es', '600111010', '00000010K', 'f83940fd01327be194f7f676a10d6fcd24d2c04af7bd0d4eca4491fe530db902', FALSE),
(11, 'Sergio', 'Vidal Ramos', 'sergio.vidal@ufv.es', '600111011', '00000011L', 'f83940fd01327be194f7f676a10d6fcd24d2c04af7bd0d4eca4491fe530db902', FALSE),
(12, 'Irene', 'Mendez Prieto', 'irene.mendez@ufv.es', '600111012', '00000012M', 'f83940fd01327be194f7f676a10d6fcd24d2c04af7bd0d4eca4491fe530db902', FALSE),
(13, 'Rocio', 'Campos Nieto', 'rocio.campos@ufv.es', '600111013', '00000013N', 'f83940fd01327be194f7f676a10d6fcd24d2c04af7bd0d4eca4491fe530db902', FALSE),
(14, 'Hugo', 'Arias Molina', 'hugo.arias@ufv.es', '600111014', '00000014P', 'f83940fd01327be194f7f676a10d6fcd24d2c04af7bd0d4eca4491fe530db902', FALSE),
(15, 'Noelia', 'Serrano Vega', 'noelia.serrano@ufv.es', '600111015', '00000015R', 'f83940fd01327be194f7f676a10d6fcd24d2c04af7bd0d4eca4491fe530db902', FALSE),
(16, 'Raul', 'Benitez Cano', 'raul.benitez@ufv.es', '600111016', '00000016S', 'f83940fd01327be194f7f676a10d6fcd24d2c04af7bd0d4eca4491fe530db902', FALSE),
(17, 'Paula', 'Bravo Hidalgo', 'paula.bravo@ufv.es', '600111017', '00000017T', 'f83940fd01327be194f7f676a10d6fcd24d2c04af7bd0d4eca4491fe530db902', FALSE),
(18, 'Adrian', 'Prieto Lara', 'adrian.prieto@ufv.es', '600111018', '00000018V', 'f83940fd01327be194f7f676a10d6fcd24d2c04af7bd0d4eca4491fe530db902', FALSE),
(19, 'Clara', 'Soto Rubio', 'clara.soto@ufv.es', '600111019', '00000019W', 'f83940fd01327be194f7f676a10d6fcd24d2c04af7bd0d4eca4491fe530db902', FALSE),
(20, 'Tomas', 'Marin Oliva', 'tomas.marin@ufv.es', '600111020', '00000020X', 'f83940fd01327be194f7f676a10d6fcd24d2c04af7bd0d4eca4491fe530db902', FALSE);

-- 14 productos de prueba
-- Distribución de propietarios:
-- usuario 1 -> 3 productos
-- usuario 2 -> 2 productos
-- usuario 3 -> 0 productos
-- usuario 4 -> 1 producto
-- usuario 5 -> 0 productos
-- usuario 6 -> 4 productos
-- usuario 7 -> 1 producto
-- usuario 8 -> 2 productos
-- usuario 9 -> 1 producto
-- usuario 10 -> 0 productos
INSERT INTO producto (
	id_producto,
	titulo,
	descripcion,
	categoria,
	tipo_transaccion,
	estado_producto,
	precio,
	id_propietario,
	condicion,
	ubicacion,
	imagen_url,
	vistas
) VALUES
(1, 'Portatil Lenovo IdeaPad', 'Portatil de 15 pulgadas con cargador original.', 'ELECTRONICA', 'ALQUILER', 'DISPONIBLE', 18.00, 1),
(2, 'Calculadora cientifica Casio', 'Ideal para ingenieria y matematicas.', 'ELECTRONICA', 'VENTA', 'DISPONIBLE', 22.50, 1),
(3, 'Tabla de surf corta', 'Tabla en buen estado para nivel intermedio.', 'DEPORTE', 'PRESTAMO', 'NO_DISPONIBLE', NULL, 1),
(4, 'Libro Fundamentos de Programacion', 'Manual universitario con apuntes.', 'LIBROS', 'VENTA', 'DISPONIBLE', 12.00, 2),
(5, 'Raqueta de tenis Wilson', 'Incluye funda y overgrips nuevos.', 'DEPORTE', 'ALQUILER', 'DISPONIBLE', 6.50, 2),
(6, 'Lampara de escritorio LED', 'Luz regulable y puerto USB.', 'HOGAR', 'VENTA', 'DISPONIBLE', 15.00, 4),
(7, 'Monitor Samsung 24"', 'Resolucion Full HD, HDMI incluido.', 'ELECTRONICA', 'ALQUILER', 'NO_DISPONIBLE', 14.00, 6),
(8, 'Bicicleta urbana', 'Bicicleta talla M para desplazamientos cortos.', 'DEPORTE', 'ALQUILER', 'DISPONIBLE', 9.00, 6),
(9, 'Pack 3 novelas clasicas', 'Coleccion de literatura en tapa blanda.', 'LIBROS', 'VENTA', 'DISPONIBLE', 17.00, 6),
(10, 'Set de cocina basico', 'Sarten, olla y utensilios para piso de estudiantes.', 'HOGAR', 'PRESTAMO', 'DISPONIBLE', NULL, 6),
(11, 'Aspiradora compacta', 'Ligera y facil de guardar.', 'HOGAR', 'ALQUILER', 'DISPONIBLE', 7.00, 7),
(12, 'Mando PS5', 'Mando original con poco uso.', 'ELECTRONICA', 'VENTA', 'DISPONIBLE', 45.00, 8),
(13, 'Patinete plegable', 'Patinete urbano con freno trasero.', 'DEPORTE', 'VENTA', 'NO_DISPONIBLE', 55.00, 8),
(14, 'Otros: Kit de herramientas', 'Caja con herramientas basicas para bricolaje.', 'OTROS', 'PRESTAMO', 'DISPONIBLE', NULL, 9);

