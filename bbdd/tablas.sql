CREATE Database IF NOT EXISTS ufvshare;

use ufvshare;   

CREATE TABLE usuario (
    id_usuario           BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre               VARCHAR(100) NOT NULL,
    apellidos            VARCHAR(150) NOT NULL,
    correo               VARCHAR(150) NOT NULL UNIQUE,
    telefono             VARCHAR(20) NOT NULL UNIQUE,
    dni                  VARCHAR(20) NOT NULL UNIQUE,
    password             VARCHAR(255) NOT NULL,
    foto_perfil          VARCHAR(512),
    pregunta_seguridad   VARCHAR(255),
    respuesta_seguridad_hash VARCHAR(255)
);

CREATE TABLE sessions (
    token                VARCHAR(36) PRIMARY KEY,
    email                VARCHAR(255) NOT NULL
);

CREATE TABLE pending_cambio (
    id                   BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_usuario           BIGINT UNSIGNED NOT NULL,
    campo                VARCHAR(30) NOT NULL,
    valor_nuevo          VARCHAR(255) NOT NULL,
    token                VARCHAR(64) NOT NULL UNIQUE,
    expiracion           DATETIME NOT NULL,
    usado                BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_pending_cambio_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


CREATE TABLE producto (
    id_producto          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    titulo               VARCHAR(150)     NOT NULL,
    descripcion          TEXT             NOT NULL,
    categoria ENUM('ELECTRONICA','LIBROS','DEPORTE','HOGAR','OTROS') NOT NULL,
    tipo_transaccion     ENUM('PRESTAMO','ALQUILER','VENTA') NOT NULL,
    estado_producto      ENUM('DISPONIBLE','RESERVADO','ALQUILADO','VENDIDO','NO_DISPONIBLE') NOT NULL DEFAULT 'DISPONIBLE',
    precio               DECIMAL(10,2),
    id_propietario       BIGINT UNSIGNED  NOT NULL,
    condicion            ENUM('NUEVO','COMO_NUEVO','BUENO','ACEPTABLE','DETERIORADO'),
    ubicacion            VARCHAR(200),
    imagen_url           VARCHAR(500),
    fecha_publicacion    DATETIME,
    vistas               INTEGER NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_producto_propietario
        FOREIGN KEY (id_propietario)
        REFERENCES usuario(id_usuario)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);


CREATE TABLE solicitud (
    id_solicitud         BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_producto          BIGINT UNSIGNED NOT NULL,
    id_solicitante       BIGINT UNSIGNED NOT NULL,
    tipo_transaccion     ENUM('PRESTAMO','ALQUILER','VENTA') NOT NULL,
    fecha_solicitud      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- Para alquiler o prestamo
    fecha_inicio         DATETIME, 
    fecha_fin            DATETIME,
    -- --------------------------
    estado_solicitud     ENUM('PENDIENTE','ACEPTADA','RECHAZADA','CANCELADA') NOT NULL DEFAULT 'PENDIENTE',

    CONSTRAINT fk_solicitud_producto
        FOREIGN KEY (id_producto)
        REFERENCES producto(id_producto)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_solicitud_usuario
        FOREIGN KEY (id_solicitante)
        REFERENCES usuario(id_usuario)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    -- Un usuario no debería poder pedir su propio producto (regla en lógica de negocio)
    INDEX idx_solicitud_producto (id_producto),
    INDEX idx_solicitud_solicitante (id_solicitante)
);


CREATE TABLE transaccion (
    id_transaccion       BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_solicitud         BIGINT UNSIGNED NOT NULL UNIQUE,
    fecha_creacion       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_inicio_real    DATETIME,
    fecha_fin_real       DATETIME,
    estado_transaccion   ENUM('EN_CURSO','COMPLETADA','CANCELADA','NO_DEVUELTA')
                         NOT NULL DEFAULT 'EN_CURSO',

    CONSTRAINT fk_transaccion_solicitud
        FOREIGN KEY (id_solicitud)
        REFERENCES solicitud(id_solicitud)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE foto_producto (
    id_foto         BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_producto     BIGINT UNSIGNED NOT NULL,
    url_foto        VARCHAR(500) NOT NULL,
    es_principal    BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_foto_producto
        FOREIGN KEY (id_producto)
        REFERENCES producto(id_producto)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE favorito (
    id_favorito          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_usuario           BIGINT UNSIGNED NOT NULL,
    id_producto          BIGINT UNSIGNED NOT NULL,
    fecha_creacion       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_favorito_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_favorito_producto
        FOREIGN KEY (id_producto)
        REFERENCES producto(id_producto)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT uk_favorito_usuario_producto UNIQUE (id_usuario, id_producto)
);

CREATE TABLE reporte_usuario (
    id_reporte              BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_usuario_reportante   BIGINT UNSIGNED NOT NULL,
    id_usuario_reportado    BIGINT UNSIGNED NOT NULL,
    motivo              VARCHAR(100) NOT NULL,
    comentario          TEXT,
    fecha_reporte       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado_reporte      ENUM('ABIERTO','EN_REVISION','CERRADO')
                        NOT NULL DEFAULT 'ABIERTO',

    CONSTRAINT fk_rep_user_reportante
        FOREIGN KEY (id_usuario_reportante)
        REFERENCES usuario(id_usuario)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_rep_user_reportado
        FOREIGN KEY (id_usuario_reportado)
        REFERENCES usuario(id_usuario)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE reporte_producto (
    id_reporte              BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_usuario_reportante   BIGINT UNSIGNED NOT NULL,
    id_producto_reportado   BIGINT UNSIGNED NOT NULL,
    motivo              VARCHAR(100) NOT NULL,
    comentario          TEXT,
    fecha_reporte       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado_reporte      ENUM('ABIERTO','EN_REVISION','CERRADO')
                        NOT NULL DEFAULT 'ABIERTO',

    CONSTRAINT fk_rep_prod_reportante
        FOREIGN KEY (id_usuario_reportante)
        REFERENCES usuario(id_usuario)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_rep_prod_producto
        FOREIGN KEY (id_producto_reportado)
        REFERENCES producto(id_producto)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS conversacion (
    id_conversacion      BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_usuario1          BIGINT UNSIGNED NOT NULL,
    id_usuario2          BIGINT UNSIGNED NOT NULL,
    id_producto          BIGINT UNSIGNED NOT NULL,
    fecha_creacion       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ultimo_mensaje       VARCHAR(500),
    fecha_ultimo_msg     DATETIME,

    CONSTRAINT fk_conv_usuario1
        FOREIGN KEY (id_usuario1)
        REFERENCES usuario(id_usuario)
        ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_conv_usuario2
        FOREIGN KEY (id_usuario2)
        REFERENCES usuario(id_usuario)
        ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_conv_producto
        FOREIGN KEY (id_producto)
        REFERENCES producto(id_producto)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS mensaje (
    id_mensaje           BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_conversacion      BIGINT UNSIGNED NOT NULL,
    id_remitente         BIGINT UNSIGNED NOT NULL,
    contenido            TEXT NOT NULL,
    fecha_envio          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    leido                BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_msg_conv
        FOREIGN KEY (id_conversacion)
        REFERENCES conversacion(id_conversacion)
        ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_msg_remitente
        FOREIGN KEY (id_remitente)
        REFERENCES usuario(id_usuario)
        ON DELETE CASCADE ON UPDATE CASCADE,

    INDEX idx_msg_conv (id_conversacion),
    INDEX idx_msg_poll (id_conversacion, id_mensaje)
);

