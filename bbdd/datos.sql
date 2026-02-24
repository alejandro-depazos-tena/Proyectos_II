USE UFVshare;

-- Limpieza mínima para poder re-ejecutar este script sin errores de UNIQUE/FK
DELETE FROM producto;
DELETE FROM usuario;

-- 10 usuarios de prueba
INSERT INTO usuario (id_usuario, nombre, apellidos, correo, telefono, dni) VALUES
(1, 'Lucia', 'Martinez Gomez', 'lucia.martinez@ufv.es', '600111001', '00000001A'),
(2, 'Pablo', 'Sanchez Ruiz', 'pablo.sanchez@ufv.es', '600111002', '00000002B'),
(3, 'Marta', 'Lopez Diaz', 'marta.lopez@ufv.es', '600111003', '00000003C'),
(4, 'Carlos', 'Fernandez Gil', 'carlos.fernandez@ufv.es', '600111004', '00000004D'),
(5, 'Elena', 'Navarro Perez', 'elena.navarro@ufv.es', '600111005', '00000005E'),
(6, 'Javier', 'Moreno Torres', 'javier.moreno@ufv.es', '600111006', '00000006F'),
(7, 'Andrea', 'Romero Vega', 'andrea.romero@ufv.es', '600111007', '00000007G'),
(8, 'Diego', 'Hernandez Leon', 'diego.hernandez@ufv.es', '600111008', '00000008H'),
(9, 'Nuria', 'Castro Molina', 'nuria.castro@ufv.es', '600111009', '00000009J'),
(10, 'Alberto', 'Ortega Sanz', 'alberto.ortega@ufv.es', '600111010', '00000010K');

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
	id_propietario
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

