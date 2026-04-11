USE ufvshare;

CREATE TABLE IF NOT EXISTS app_review (
	id_review BIGINT AUTO_INCREMENT PRIMARY KEY,
	id_usuario BIGINT NOT NULL,
	nombre_usuario VARCHAR(120) NOT NULL,
	puntuacion INT NOT NULL,
	comentario VARCHAR(280) NOT NULL,
	fecha_actualizacion DATETIME NOT NULL,
	CONSTRAINT uk_app_review_usuario UNIQUE (id_usuario)
);

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
DELETE FROM app_review;
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

-- Reseñas de app de prueba (1 por usuario)
INSERT INTO app_review (id_usuario, nombre_usuario, puntuacion, comentario, fecha_actualizacion) VALUES
(1, 'Lucia', 5, 'La app me ha ayudado a mover cosas del campus rapido. El flujo general es muy comodo.', '2026-04-01 10:20:00'),
(2, 'Pablo', 4, 'Muy util para alquilar material de clase. Mejoraria un poco la busqueda en categorias.', '2026-04-01 12:05:00'),
(3, 'Marta', 5, 'Me gusta el enfoque entre estudiantes y que todo sea sencillo de entender.', '2026-04-01 15:40:00'),
(4, 'Carlos', 4, 'He vendido varios productos sin complicaciones. Buena experiencia en general.', '2026-04-02 09:15:00'),
(5, 'Elena', 5, 'Interfaz clara y agradable. El chat funciona bien para cerrar acuerdos.', '2026-04-02 11:32:00'),
(6, 'Javier', 4, 'Publicar anuncios es rapido. Estaria bien afinar algun detalle visual en movil.', '2026-04-02 13:27:00'),
(7, 'Andrea', 5, 'Me sorprendio para bien. Todo va fluido y sin pasos innecesarios.', '2026-04-02 18:08:00'),
(8, 'Diego', 4, 'Buen proyecto para comunidad universitaria. Lo uso sobre todo para electronica.', '2026-04-03 08:52:00'),
(9, 'Nuria', 5, 'Muy buena idea y muy bien ejecutada. El perfil y favoritos me parecen top.', '2026-04-03 10:44:00'),
(10, 'Alberto', 3, 'Cumple su objetivo, aunque en algunas pantallas todavia hay margen de mejora.', '2026-04-03 12:19:00'),
(11, 'Sergio', 4, 'El rendimiento es bueno y la navegacion bastante intuitiva.', '2026-04-03 16:03:00'),
(12, 'Irene', 5, 'La recomendaria para estudiantes. Facil encontrar cosas utiles del dia a dia.', '2026-04-03 19:50:00'),
(13, 'Rocio', 4, 'Me gusta la estructura general. La seccion de ajustes es un acierto.', '2026-04-04 09:36:00'),
(14, 'Hugo', 5, 'Muy completa para ser una app universitaria. Buen trabajo en la experiencia.', '2026-04-04 11:11:00'),
(15, 'Noelia', 4, 'El flujo de publicar y contactar funciona bien. Bastante comoda de usar.', '2026-04-04 13:48:00'),
(16, 'Raul', 5, 'La he usado varios dias y me ha resultado super practica.', '2026-04-04 17:20:00'),
(17, 'Paula', 4, 'Buen nivel general. El chat y favoritos aportan mucho.', '2026-04-04 19:02:00'),
(18, 'Adrian', 5, 'Experiencia positiva de principio a fin. Rapida y clara.', '2026-04-05 09:14:00'),
(19, 'Clara', 4, 'Muy buen punto de partida. Con pequeños ajustes quedara redonda.', '2026-04-05 11:06:00'),
(20, 'Tomas', 5, 'App util de verdad para el entorno UFV. La seguiria usando sin duda.', '2026-04-05 13:33:00');

-- Mas anuncios para enriquecer el marketplace
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
(15, 'Camara web Logitech C920', 'Camara Full HD para clases online y streaming.', 'ELECTRONICA', 'VENTA', 'DISPONIBLE', 38.00, 11, 'BUENO', 'Edificio E, cafeteria', 'https://images.unsplash.com/photo-1587825140708-dfaf72ae4b04?w=600&auto=format&fit=crop', 29),
(16, 'Altavoz Bluetooth JBL', 'Altavoz portatil con buena bateria, ideal para reuniones.', 'ELECTRONICA', 'ALQUILER', 'DISPONIBLE', 4.50, 11, 'COMO_NUEVO', 'Residencia D, hab. 12', 'https://images.unsplash.com/photo-1589003077984-894e133dabab?w=600&auto=format&fit=crop', 18),
(17, 'Libro de Estadistica Aplicada', 'Libro recomendado para analitica de datos.', 'LIBROS', 'VENTA', 'DISPONIBLE', 14.00, 12, 'ACEPTABLE', 'Biblioteca central', 'https://images.unsplash.com/photo-1524995997946-a1c2e315a42f?w=600&auto=format&fit=crop', 33),
(18, 'Libro de Marketing Digital', 'Edicion 2023 con ejercicios practicos.', 'LIBROS', 'ALQUILER', 'DISPONIBLE', 3.00, 12, 'BUENO', 'Campus A, aula 2.4', 'https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=600&auto=format&fit=crop', 11),
(19, 'Esterilla de yoga', 'Esterilla antideslizante de 6 mm.', 'DEPORTE', 'PRESTAMO', 'DISPONIBLE', NULL, 13, 'COMO_NUEVO', 'Pabellon sur', 'https://images.unsplash.com/photo-1599447332412-6bc6830c815a?w=600&auto=format&fit=crop', 7),
(20, 'Mancuernas ajustables 20kg', 'Par de mancuernas con discos intercambiables.', 'DEPORTE', 'ALQUILER', 'DISPONIBLE', 8.00, 13, 'BUENO', 'Residencia E, gimnasio', 'https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=600&auto=format&fit=crop', 16),
(21, 'Microondas compacto', 'Microondas de 20 litros funcionando perfecto.', 'HOGAR', 'VENTA', 'DISPONIBLE', 32.00, 14, 'BUENO', 'Residencia C, hall', 'https://images.unsplash.com/photo-1574269909862-7e1d70bb8078?w=600&auto=format&fit=crop', 13),
(22, 'Tostadora doble ranura', 'Tostadora basica para piso compartido.', 'HOGAR', 'PRESTAMO', 'DISPONIBLE', NULL, 14, 'ACEPTABLE', 'Residencia C, cocina comun', 'https://images.unsplash.com/photo-1585238342024-78d387f4a707?w=600&auto=format&fit=crop', 4),
(23, 'Impresora HP DeskJet', 'Impresora con cartuchos al 60%. Incluye cable USB.', 'ELECTRONICA', 'VENTA', 'DISPONIBLE', 26.00, 15, 'ACEPTABLE', 'Campus B, coworking', 'https://images.unsplash.com/photo-1612815154858-60aa4c59eaa6?w=600&auto=format&fit=crop', 24),
(24, 'Auriculares Sony WH-CH520', 'Auriculares inalambricos con autonomia de 40h.', 'ELECTRONICA', 'ALQUILER', 'DISPONIBLE', 5.00, 15, 'COMO_NUEVO', 'Residencia F, hab. 40', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop', 45),
(25, 'Apuntes impresos Calculo I', 'Carpeta completa de apuntes y ejercicios resueltos.', 'LIBROS', 'VENTA', 'DISPONIBLE', 9.50, 16, 'BUENO', 'Biblioteca, planta 1', 'https://images.unsplash.com/photo-1455885666463-9f41f3f56f56?w=600&auto=format&fit=crop', 38),
(26, 'Libro de Derecho Civil', 'Manual actualizado con legislacion 2025.', 'LIBROS', 'ALQUILER', 'NO_DISPONIBLE', 4.00, 16, 'BUENO', 'Campus C, despacho alumnos', 'https://images.unsplash.com/photo-1519682337058-a94d519337bc?w=600&auto=format&fit=crop', 8),
(27, 'Balon de baloncesto Spalding', 'Balon talla oficial para pista interior o exterior.', 'DEPORTE', 'VENTA', 'DISPONIBLE', 12.00, 17, 'COMO_NUEVO', 'Pista exterior', 'https://images.unsplash.com/photo-1546519638-68e109498ffc?w=600&auto=format&fit=crop', 52),
(28, 'Botas de futbol talla 43', 'Botas con tacos FG, poco uso.', 'DEPORTE', 'ALQUILER', 'DISPONIBLE', 4.00, 17, 'BUENO', 'Vestuarios campus', 'https://images.unsplash.com/photo-1526232761682-d26e03ac148e?w=600&auto=format&fit=crop', 21),
(29, 'Silla ergonomica de estudio', 'Silla con soporte lumbar y altura ajustable.', 'HOGAR', 'VENTA', 'DISPONIBLE', 48.00, 18, 'BUENO', 'Residencia A, hab. 22', 'https://images.unsplash.com/photo-1505843490701-5be5d84f52f9?w=600&auto=format&fit=crop', 36),
(30, 'Flexo vintage', 'Lampara flexo metalica, luz calida.', 'HOGAR', 'PRESTAMO', 'DISPONIBLE', NULL, 18, 'ACEPTABLE', 'Residencia A, sala comun', 'https://images.unsplash.com/photo-1513506003901-1e6a229e2d15?w=600&auto=format&fit=crop', 5),
(31, 'Mochila 35L trekking', 'Mochila resistente al agua para escapadas de finde.', 'OTROS', 'ALQUILER', 'DISPONIBLE', 6.00, 19, 'BUENO', 'Residencia G', 'https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?w=600&auto=format&fit=crop', 17),
(32, 'Maleta cabina 55cm', 'Maleta rigida con 4 ruedas.', 'OTROS', 'VENTA', 'DISPONIBLE', 30.00, 19, 'BUENO', 'Residencia G, recepcion', 'https://images.unsplash.com/photo-1502920514313-52581002a659?w=600&auto=format&fit=crop', 14),
(33, 'Teclado mecanico Keychron', 'Teclado mecanico bluetooth, switches brown.', 'ELECTRONICA', 'VENTA', 'DISPONIBLE', 58.00, 20, 'COMO_NUEVO', 'Campus D, sala maker', 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600&auto=format&fit=crop', 73),
(34, 'Raton Logitech MX Master', 'Raton ergonomico para productividad.', 'ELECTRONICA', 'ALQUILER', 'DISPONIBLE', 6.50, 20, 'BUENO', 'Campus D, laboratorio 3', 'https://images.unsplash.com/photo-1527814050087-3793815479db?w=600&auto=format&fit=crop', 28),
(35, 'Libro de Psicologia Social', 'Subrayado en algunas paginas, muy util para examen.', 'LIBROS', 'VENTA', 'DISPONIBLE', 11.00, 3, 'ACEPTABLE', 'Campus A, banco central', 'https://images.unsplash.com/photo-1512820790803-83ca734da794?w=600&auto=format&fit=crop', 20),
(36, 'Tablet Samsung Galaxy Tab', 'Tablet de 10 pulgadas con funda y lapiz.', 'ELECTRONICA', 'VENTA', 'NO_DISPONIBLE', 95.00, 5, 'BUENO', 'Residencia B', 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=600&auto=format&fit=crop', 61),
(37, 'Proyector portatil', 'Proyector mini HDMI para presentaciones en aula.', 'ELECTRONICA', 'ALQUILER', 'DISPONIBLE', 9.00, 4, 'COMO_NUEVO', 'Campus E, aula multimedia', 'https://images.unsplash.com/photo-1579547621113-e4bb2a19bdd6?w=600&auto=format&fit=crop', 26),
(38, 'Patines en linea talla 40', 'Patines con freno trasero, incluyen protecciones.', 'DEPORTE', 'ALQUILER', 'DISPONIBLE', 5.00, 7, 'BUENO', 'Residencia C', 'https://images.unsplash.com/photo-1521334884684-d80222895322?w=600&auto=format&fit=crop', 12),
(39, 'Plancha de vapor', 'Plancha compacta para ropa, base ceramica.', 'HOGAR', 'PRESTAMO', 'DISPONIBLE', NULL, 10, 'BUENO', 'Residencia F', 'https://images.unsplash.com/photo-1588627541420-fce3f661b779?w=600&auto=format&fit=crop', 3),
(40, 'Router WiFi 6', 'Router dual band para mejorar cobertura en piso.', 'ELECTRONICA', 'VENTA', 'DISPONIBLE', 35.00, 2, 'COMO_NUEVO', 'Campus B', 'https://images.unsplash.com/photo-1612815154858-60aa4c59eaa6?w=600&auto=format&fit=crop', 31);

-- Favoritos de prueba en distintas fechas
INSERT INTO favorito (id_favorito, id_usuario, id_producto, fecha_creacion) VALUES
(1, 1, 12, '2026-03-01 10:00:00'), (2, 1, 21, '2026-03-02 10:00:00'),
(3, 2, 1, '2026-03-02 12:00:00'), (4, 2, 33, '2026-03-03 09:30:00'),
(5, 3, 5, '2026-03-03 18:10:00'), (6, 3, 24, '2026-03-04 19:15:00'),
(7, 4, 8, '2026-03-05 11:05:00'), (8, 4, 29, '2026-03-06 13:10:00'),
(9, 5, 27, '2026-03-07 16:40:00'), (10, 5, 34, '2026-03-08 16:45:00'),
(11, 6, 23, '2026-03-09 09:20:00'), (12, 6, 37, '2026-03-10 10:50:00'),
(13, 7, 12, '2026-03-11 08:00:00'), (14, 7, 16, '2026-03-11 08:30:00'),
(15, 8, 4, '2026-03-12 18:22:00'), (16, 8, 35, '2026-03-13 19:11:00'),
(17, 9, 8, '2026-03-14 17:01:00'), (18, 9, 33, '2026-03-15 12:22:00'),
(19, 10, 14, '2026-03-16 09:43:00'), (20, 10, 40, '2026-03-17 09:44:00'),
(21, 11, 18, '2026-03-18 11:10:00'), (22, 12, 37, '2026-03-18 15:10:00'),
(23, 13, 21, '2026-03-19 14:25:00'), (24, 14, 23, '2026-03-19 16:30:00'),
(25, 15, 1, '2026-03-20 10:40:00'), (26, 16, 31, '2026-03-20 10:50:00'),
(27, 17, 27, '2026-03-21 09:05:00'), (28, 18, 29, '2026-03-21 09:08:00'),
(29, 19, 33, '2026-03-22 17:13:00'), (30, 20, 34, '2026-03-22 17:15:00');

-- Solicitudes de distintos estados y dias
INSERT INTO solicitud (
	id_solicitud, id_producto, id_solicitante, tipo_transaccion,
	fecha_solicitud, fecha_inicio, fecha_fin, estado_solicitud
) VALUES
(1, 1, 2, 'ALQUILER', '2026-03-02 10:00:00', '2026-03-04 09:00:00', '2026-03-06 19:00:00', 'ACEPTADA'),
(2, 5, 3, 'ALQUILER', '2026-03-03 12:45:00', '2026-03-05 15:00:00', '2026-03-07 20:00:00', 'ACEPTADA'),
(3, 8, 9, 'ALQUILER', '2026-03-05 11:30:00', '2026-03-10 08:00:00', '2026-03-14 21:00:00', 'ACEPTADA'),
(4, 12, 7, 'VENTA', '2026-03-06 16:20:00', NULL, NULL, 'RECHAZADA'),
(5, 14, 10, 'PRESTAMO', '2026-03-07 09:20:00', '2026-03-08 09:00:00', '2026-03-12 22:00:00', 'CANCELADA'),
(6, 16, 4, 'ALQUILER', '2026-03-08 13:10:00', '2026-03-10 10:00:00', '2026-03-11 23:00:00', 'ACEPTADA'),
(7, 18, 11, 'ALQUILER', '2026-03-09 18:40:00', '2026-03-12 09:00:00', '2026-03-16 20:00:00', 'ACEPTADA'),
(8, 20, 2, 'ALQUILER', '2026-03-11 19:20:00', '2026-03-20 09:00:00', '2026-03-25 20:00:00', 'PENDIENTE'),
(9, 21, 6, 'VENTA', '2026-03-12 10:30:00', NULL, NULL, 'ACEPTADA'),
(10, 23, 14, 'VENTA', '2026-03-13 17:50:00', NULL, NULL, 'ACEPTADA'),
(11, 24, 1, 'ALQUILER', '2026-03-14 08:25:00', '2026-03-16 09:00:00', '2026-03-18 22:00:00', 'CANCELADA'),
(12, 27, 18, 'VENTA', '2026-03-15 11:40:00', NULL, NULL, 'ACEPTADA'),
(13, 28, 19, 'ALQUILER', '2026-03-17 15:00:00', '2026-03-23 08:30:00', '2026-03-27 21:30:00', 'PENDIENTE'),
(14, 29, 5, 'VENTA', '2026-03-18 12:00:00', NULL, NULL, 'ACEPTADA'),
(15, 31, 16, 'ALQUILER', '2026-03-19 16:15:00', '2026-03-22 09:00:00', '2026-03-24 18:30:00', 'ACEPTADA'),
(16, 33, 9, 'VENTA', '2026-03-20 10:05:00', NULL, NULL, 'ACEPTADA'),
(17, 34, 3, 'ALQUILER', '2026-03-21 13:35:00', '2026-03-28 10:00:00', '2026-03-30 20:00:00', 'RECHAZADA'),
(18, 37, 12, 'ALQUILER', '2026-03-22 09:50:00', '2026-03-26 09:00:00', '2026-03-28 18:00:00', 'ACEPTADA');

-- Transacciones para solicitudes aceptadas (incluye completadas y en curso)
INSERT INTO transaccion (
	id_transaccion, id_solicitud, fecha_creacion,
	fecha_inicio_real, fecha_fin_real, estado_transaccion
) VALUES
(1, 1, '2026-03-02 15:00:00', '2026-03-04 09:10:00', '2026-03-06 18:40:00', 'COMPLETADA'),
(2, 2, '2026-03-04 10:00:00', '2026-03-05 15:20:00', '2026-03-07 19:50:00', 'COMPLETADA'),
(3, 3, '2026-03-08 20:00:00', '2026-03-10 08:30:00', NULL, 'EN_CURSO'),
(4, 6, '2026-03-09 09:00:00', '2026-03-10 10:20:00', '2026-03-11 22:40:00', 'COMPLETADA'),
(5, 7, '2026-03-10 18:00:00', '2026-03-12 09:10:00', '2026-03-16 19:55:00', 'COMPLETADA'),
(6, 9, '2026-03-12 14:15:00', NULL, '2026-03-13 11:15:00', 'COMPLETADA'),
(7, 10, '2026-03-14 08:00:00', NULL, '2026-03-15 13:30:00', 'COMPLETADA'),
(8, 12, '2026-03-16 09:45:00', NULL, '2026-03-16 18:10:00', 'COMPLETADA'),
(9, 14, '2026-03-18 16:20:00', NULL, '2026-03-19 12:05:00', 'COMPLETADA'),
(10, 15, '2026-03-20 17:20:00', '2026-03-22 09:10:00', '2026-03-24 18:10:00', 'COMPLETADA'),
(11, 16, '2026-03-21 10:30:00', NULL, '2026-03-21 16:00:00', 'COMPLETADA'),
(12, 18, '2026-03-23 12:00:00', '2026-03-26 09:15:00', NULL, 'EN_CURSO');

-- Ajustes de estado de producto para simular reservas, alquileres y ventas reales
UPDATE producto SET estado_producto = 'RESERVADO' WHERE id_producto IN (8);
UPDATE producto SET estado_producto = 'ALQUILADO' WHERE id_producto IN (37);
UPDATE producto SET estado_producto = 'VENDIDO' WHERE id_producto IN (21, 23, 27, 29, 33);

-- Conversaciones entre usuarios en distintos dias
INSERT INTO conversacion (
	id_conversacion, id_usuario1, id_usuario2, id_producto,
	fecha_creacion, ultimo_mensaje, fecha_ultimo_msg
) VALUES
(1, 1, 2, 4, '2026-03-01 09:00:00', 'Te lo llevo mañana a primera hora', '2026-03-01 09:25:00'),
(2, 3, 4, 6, '2026-03-02 12:00:00', 'Perfecto, gracias por reservarla', '2026-03-02 12:26:00'),
(3, 5, 6, 8, '2026-03-03 16:20:00', 'Te confirmo en cuanto vuelva de clase', '2026-03-03 16:42:00'),
(4, 7, 8, 12, '2026-03-04 10:00:00', 'Hecho, quedamos en biblioteca', '2026-03-04 10:24:00'),
(5, 9, 10, 14, '2026-03-05 18:10:00', 'Te lo presto esta tarde', '2026-03-05 18:31:00'),
(6, 11, 12, 18, '2026-03-06 13:15:00', 'Me viene genial para el examen', '2026-03-06 13:35:00'),
(7, 13, 14, 21, '2026-03-07 11:10:00', 'Listo, trato cerrado', '2026-03-07 11:30:00'),
(8, 15, 16, 23, '2026-03-08 15:00:00', 'Si quieres te lo enseño por videollamada', '2026-03-08 15:23:00'),
(9, 17, 18, 27, '2026-03-09 19:40:00', 'Perfecto, lo pago por Bizum', '2026-03-09 20:00:00'),
(10, 19, 20, 33, '2026-03-10 09:30:00', 'Cierro el trato hoy mismo', '2026-03-10 09:54:00');

-- Mensajes con remitentes variados (todos los usuarios participan)
INSERT INTO mensaje (id_mensaje, id_conversacion, id_remitente, contenido, fecha_envio, leido) VALUES
(1, 1, 1, 'Hola Pablo, sigo teniendo el libro disponible', '2026-03-01 09:02:00', TRUE),
(2, 1, 2, 'Genial, me interesa para esta semana', '2026-03-01 09:08:00', TRUE),
(3, 1, 1, 'Te lo reservo hasta mañana', '2026-03-01 09:16:00', TRUE),
(4, 1, 2, 'Te lo llevo mañana a primera hora', '2026-03-01 09:25:00', FALSE),
(5, 2, 3, 'Carlos, te sigue interesando la lampara?', '2026-03-02 12:04:00', TRUE),
(6, 2, 4, 'Si, la necesito para el escritorio', '2026-03-02 12:09:00', TRUE),
(7, 2, 3, 'Perfecto, te la guardo', '2026-03-02 12:19:00', TRUE),
(8, 2, 4, 'Perfecto, gracias por reservarla', '2026-03-02 12:26:00', FALSE),
(9, 3, 5, 'Javier, tienes libre la bici este finde?', '2026-03-03 16:21:00', TRUE),
(10, 3, 6, 'Creo que si, te confirmo en breve', '2026-03-03 16:27:00', TRUE),
(11, 3, 5, 'Me vale para dos dias', '2026-03-03 16:36:00', TRUE),
(12, 3, 6, 'Te confirmo en cuanto vuelva de clase', '2026-03-03 16:42:00', FALSE),
(13, 4, 7, 'Diego, sigo buscando mando para PS5', '2026-03-04 10:02:00', TRUE),
(14, 4, 8, 'Lo tengo listo y con bateria', '2026-03-04 10:06:00', TRUE),
(15, 4, 7, 'Quedamos a las 18?', '2026-03-04 10:19:00', TRUE),
(16, 4, 8, 'Hecho, quedamos en biblioteca', '2026-03-04 10:24:00', FALSE),
(17, 5, 9, 'Alberto, te viene bien hoy el kit?', '2026-03-05 18:11:00', TRUE),
(18, 5, 10, 'Si, lo necesito para montar una estanteria', '2026-03-05 18:14:00', TRUE),
(19, 5, 9, 'Te lo dejo hasta el domingo', '2026-03-05 18:27:00', TRUE),
(20, 5, 10, 'Te lo presto esta tarde', '2026-03-05 18:31:00', FALSE),
(21, 6, 11, 'Irene, te puedo dejar el libro de marketing', '2026-03-06 13:16:00', TRUE),
(22, 6, 12, 'Perfecto, justo lo necesitaba', '2026-03-06 13:20:00', TRUE),
(23, 6, 11, 'Te lo llevo mañana al campus', '2026-03-06 13:28:00', TRUE),
(24, 6, 12, 'Me viene genial para el examen', '2026-03-06 13:35:00', FALSE),
(25, 7, 13, 'Hugo, sigue disponible el microondas?', '2026-03-07 11:12:00', TRUE),
(26, 7, 14, 'Si, funciona perfecto', '2026-03-07 11:16:00', TRUE),
(27, 7, 13, 'Te hago transferencia ahora', '2026-03-07 11:25:00', TRUE),
(28, 7, 14, 'Listo, trato cerrado', '2026-03-07 11:30:00', FALSE),
(29, 8, 15, 'Raul, te interesa la impresora para esta semana?', '2026-03-08 15:03:00', TRUE),
(30, 8, 16, 'Si, necesito imprimir el TFG', '2026-03-08 15:06:00', TRUE),
(31, 8, 15, 'Sin problema, te la enseño cuando quieras', '2026-03-08 15:18:00', TRUE),
(32, 8, 16, 'Si quieres te lo enseño por videollamada', '2026-03-08 15:23:00', FALSE),
(33, 9, 17, 'Adrian, tengo el balon listo para vender', '2026-03-09 19:41:00', TRUE),
(34, 9, 18, 'Me encaja, lo quiero hoy', '2026-03-09 19:45:00', TRUE),
(35, 9, 17, 'Te paso mi telefono por privado', '2026-03-09 19:53:00', TRUE),
(36, 9, 18, 'Perfecto, lo pago por Bizum', '2026-03-09 20:00:00', FALSE),
(37, 10, 19, 'Tomas, aun tienes el teclado keychron?', '2026-03-10 09:32:00', TRUE),
(38, 10, 20, 'Si, incluye cable y keycaps extra', '2026-03-10 09:40:00', TRUE),
(39, 10, 19, 'Me lo quedo, puedes traerlo al campus?', '2026-03-10 09:50:00', TRUE),
(40, 10, 20, 'Cierro el trato hoy mismo', '2026-03-10 09:54:00', FALSE);

