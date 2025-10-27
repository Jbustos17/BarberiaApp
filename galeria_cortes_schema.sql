-- Script SQL para crear la tabla galeria_cortes
-- Ejecuta este script en tu base de datos MySQL

CREATE TABLE IF NOT EXISTS galeria_cortes (
    id_galeria BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_barbero BIGINT NOT NULL,
    foto_url VARCHAR(500) NOT NULL,
    descripcion VARCHAR(255),
    fecha_subida VARCHAR(50),
    FOREIGN KEY (id_barbero) REFERENCES barberos(id_barbero) ON DELETE CASCADE
);

-- Índice para mejorar el rendimiento de las consultas por barbero
CREATE INDEX idx_galeria_barbero ON galeria_cortes(id_barbero);

-- Verificar que la tabla se creó correctamente
DESCRIBE galeria_cortes;

