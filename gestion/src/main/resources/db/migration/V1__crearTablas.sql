-- Tabla empleados
CREATE TABLE IF NOT EXISTS empleados (
    id          INT NOT NULL AUTO_INCREMENT,
    nombre      VARCHAR(100) NOT NULL,
    apellido    VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    telefono    VARCHAR(20),
    estado      VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    PRIMARY KEY (id)
);

-- Tabla departamentos
CREATE TABLE IF NOT EXISTS departamentos (
    id          INT NOT NULL AUTO_INCREMENT,
    nombre      VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    estado      VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    PRIMARY KEY (id)
);
