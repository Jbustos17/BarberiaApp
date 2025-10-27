-- Script SQL para crear las tablas de galería y configuración
-- Ejecuta este script en tu base de datos MySQL

-- Tabla de galería de cortes (si aún no la has creado)
CREATE TABLE IF NOT EXISTS galeria_cortes (
    id_galeria BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_barbero BIGINT NOT NULL,
    foto_url VARCHAR(500) NOT NULL,
    descripcion VARCHAR(255),
    fecha_subida VARCHAR(50),
    FOREIGN KEY (id_barbero) REFERENCES barberos(id_barbero) ON DELETE CASCADE
);

CREATE INDEX idx_galeria_barbero ON galeria_cortes(id_barbero);

-- Tabla de configuración para comisiones y otros ajustes
CREATE TABLE IF NOT EXISTS configuracion (
    id_configuracion BIGINT AUTO_INCREMENT PRIMARY KEY,
    clave VARCHAR(100) UNIQUE NOT NULL,
    valor VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255)
);

-- Insertar valores por defecto de comisiones
INSERT INTO configuracion (clave, valor, descripcion) 
VALUES 
    ('comision_admin', '40', 'Porcentaje de comisión del administrador'),
    ('comision_barbero', '60', 'Porcentaje de comisión del barbero')
ON DUPLICATE KEY UPDATE 
    valor = VALUES(valor),
    descripcion = VALUES(descripcion);

-- Verificar las tablas creadas
DESCRIBE galeria_cortes;
DESCRIBE configuracion;

-- Ver las configuraciones
SELECT * FROM configuracion;

