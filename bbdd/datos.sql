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

-- 10 usuarios de prueba
-- contraseña en claro para todos: 123456
-- SHA-256(123456): 8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92
INSERT INTO usuario (id_usuario, nombre, apellidos, correo, telefono, dni, password) VALUES
(1, 'Lucia', 'Martinez Gomez', 'lucia.martinez@ufv.es', '600111001', '00000001A', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'),
(2, 'Pablo', 'Sanchez Ruiz', 'pablo.sanchez@ufv.es', '600111002', '00000002B', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'),
(3, 'Marta', 'Lopez Diaz', 'marta.lopez@ufv.es', '600111003', '00000003C', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'),
(4, 'Carlos', 'Fernandez Gil', 'carlos.fernandez@ufv.es', '600111004', '00000004D', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'),
(5, 'Elena', 'Navarro Perez', 'elena.navarro@ufv.es', '600111005', '00000005E', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'),
(6, 'Javier', 'Moreno Torres', 'javier.moreno@ufv.es', '600111006', '00000006F', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'),
(7, 'Andrea', 'Romero Vega', 'andrea.romero@ufv.es', '600111007', '00000007G', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'),
(8, 'Diego', 'Hernandez Leon', 'diego.hernandez@ufv.es', '600111008', '00000008H', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'),
(9, 'Nuria', 'Castro Molina', 'nuria.castro@ufv.es', '600111009', '00000009J', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'),
(10, 'Alberto', 'Ortega Sanz', 'alberto.ortega@ufv.es', '600111010', '00000010K', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92');

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
(1, 'Portatil Lenovo IdeaPad', 'Portatil de 15 pulgadas con cargador original. Bateria aguanta 4-5 horas. Perfecto para clases.', 'ELECTRONICA', 'ALQUILER', 'DISPONIBLE', 18.00, 1, 'BUENO', 'Residencia A, hab. 204', 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=600&auto=format&fit=crop', 34),
(2, 'Calculadora cientifica Casio', 'Casio fx-991SP X II. Ideal para ingenieria y matematicas. Sin rayaduras.', 'ELECTRONICA', 'VENTA', 'DISPONIBLE', 22.50, 1, 'COMO_NUEVO', 'Campus edificio A', 'https://images.unsplash.com/photo-1611532736597-de2d4265fba3?w=600&auto=format&fit=crop', 58),
(3, 'Tabla de surf corta', 'Tabla 6''2 en buen estado para nivel intermedio. Incluye leash y funda.', 'DEPORTE', 'PRESTAMO', 'NO_DISPONIBLE', NULL, 1, 'BUENO', 'Residencia B, trastero', 'https://images.unsplash.com/photo-1502680390469-be75c86b636f?w=600&auto=format&fit=crop', 12),
(4, 'Libro Fundamentos de Programacion', 'Manual universitario de la UFV con apuntes y tabs. Edicion 2022.', 'LIBROS', 'VENTA', 'DISPONIBLE', 12.00, 2, 'ACEPTABLE', 'Biblioteca, taquilla 12', 'https://images.unsplash.com/photo-1532012197267-da84d127e765?w=600&auto=format&fit=crop', 27),
(5, 'Raqueta de tenis Wilson', 'Wilson Blade 98. Incluye funda y overgrips nuevos. Usada una temporada.', 'DEPORTE', 'ALQUILER', 'DISPONIBLE', 6.50, 2, 'BUENO', 'Pabellon deportivo, conserjeria', 'https://images.unsplash.com/photo-1551773188-0801da12ddae?w=600&auto=format&fit=crop', 19),
(6, 'Lampara de escritorio LED', 'Luz regulable en 3 temperaturas de color y puerto USB integrado. Estado impecable.', 'HOGAR', 'VENTA', 'DISPONIBLE', 15.00, 4, 'COMO_NUEVO', 'Residencia C, recepcion', 'https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=600&auto=format&fit=crop', 41),
(7, 'Monitor Samsung 24"', 'Monitor Full HD IPS, 75 Hz. Cable HDMI incluido. Actualmente prestado.', 'ELECTRONICA', 'ALQUILER', 'RESERVADO', 14.00, 6, 'BUENO', 'Campus edificio B, sala de estudio', 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=600&auto=format&fit=crop', 63),
(8, 'Bicicleta urbana', 'Bicicleta talla M para desplazamientos cortos. Frenos revisados, neumaticos nuevos.', 'DEPORTE', 'ALQUILER', 'DISPONIBLE', 9.00, 6, 'BUENO', 'Aparcamiento principal, zona bicicletas', 'https://images.unsplash.com/photo-1485965120184-e220f721d03e?w=600&auto=format&fit=crop', 88),
(9, 'Pack 3 novelas clasicas', 'Coleccion de literatura: Cien Anos de Soledad, El Quijote (adaptacion) y La Regenta. Tapa blanda.', 'LIBROS', 'VENTA', 'DISPONIBLE', 17.00, 6, 'ACEPTABLE', 'Residencia A, zona comun', 'https://images.unsplash.com/photo-1512820790803-83ca734da794?w=600&auto=format&fit=crop', 22),
(10, 'Set de cocina basico', 'Sarten antiadherente, olla mediana y utensilios basicos. Ideal para primer piso de estudiantes.', 'HOGAR', 'PRESTAMO', 'DISPONIBLE', NULL, 6, 'BUENO', 'Residencia B, cocina comunitaria', 'https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=600&auto=format&fit=crop', 15),
(11, 'Aspiradora compacta', 'Aspiradora de mano, 800W. Ligera, facil de guardar. Bolsa lavable incluida.', 'HOGAR', 'ALQUILER', 'DISPONIBLE', 7.00, 7, 'COMO_NUEVO', 'Residencia C, hab. 108', 'https://images.unsplash.com/photo-1558317374-067fb5f30001?w=600&auto=format&fit=crop', 9),
(12, 'Mando PS5', 'DualSense original blanco. Poco uso, sin desgaste en joysticks. Cargador incluido.', 'ELECTRONICA', 'VENTA', 'DISPONIBLE', 45.00, 8, 'COMO_NUEVO', 'Residencia A, hab. 315', 'https://images.unsplash.com/photo-1606144042614-b2417e99c4e3?w=600&auto=format&fit=crop', 102),
(13, 'Patinete plegable', 'Patinete urbano con freno trasero y luces LED. Ruedas de 8 pulgadas. Vendido.', 'DEPORTE', 'VENTA', 'NO_DISPONIBLE', 55.00, 8, 'BUENO', 'Campus edificio C', 'https://images.unsplash.com/photo-1598550476439-6847785fcea6?w=600&auto=format&fit=crop', 77),
(14, 'Kit de herramientas', 'Caja con martillo, destornilladores, alicates, nivel y cinta metrica. Basico pero completo.', 'OTROS', 'PRESTAMO', 'DISPONIBLE', NULL, 9, 'BUENO', 'Residencia B, hab. 210', 'https://images.unsplash.com/photo-1504148455328-c376907d081c?w=600&auto=format&fit=crop', 6);

