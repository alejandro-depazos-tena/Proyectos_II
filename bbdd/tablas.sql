CREATE Database IF NOT EXISTS UFVshare;

use UFVshare;   


CREATE TABLE usuario (
    id_usuario           BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre               VARCHAR(100) NOT NULL,
    apellidos            VARCHAR(150) NOT NULL,
    correo               VARCHAR(150) NOT NULL UNIQUE,
    telefono             VARCHAR(20) NOT NULL UNIQUE,
    dni                  VARCHAR(20) NOT NULL UNIQUE
) 


CREATE TABLE producto (
    id_producto          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    titulo               VARCHAR(150)     NOT NULL,
    descripcion          TEXT             NOT NULL,
    categoria            VARCHAR(100)     NOT NULL,
    tipo_transaccion     ENUM('PRESTAMO','ALQUILER','VENTA') NOT NULL,
    estado_producto      ENUM('DISPONIBLE','NO_DISPONIBLE') NOT NULL DEFAULT 'DISPONIBLE',
    precio               DECIMAL(10,2),
    id_propietario       BIGINT UNSIGNED  NOT NULL,
    
    CONSTRAINT fk_producto_propietario
        FOREIGN KEY (id_propietario)
        REFERENCES usuario(id_usuario)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
)


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
)


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
)

