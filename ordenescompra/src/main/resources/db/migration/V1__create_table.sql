CREATE TABLE ordenes_compra (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    proveedor_id BIGINT NOT NULL,
    fecha DATE,
    estado VARCHAR(50),
    total DOUBLE
);
