CREATE TABLE existencias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    ubicacion VARCHAR(100)
);
