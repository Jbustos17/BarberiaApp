-- =====================================================
-- SCRIPT SQL COMPLETO - SISTEMA DE BARBERÍA
-- =====================================================
-- Ejecuta este script en tu base de datos MySQL

-- =====================================================
-- 1. TABLA DE GALERÍA DE CORTES
-- =====================================================
CREATE TABLE IF NOT EXISTS galeria_cortes (
    id_galeria BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_barbero BIGINT NOT NULL,
    foto_url VARCHAR(500) NOT NULL,
    descripcion VARCHAR(255),
    fecha_subida VARCHAR(50),
    FOREIGN KEY (id_barbero) REFERENCES barberos(id_barbero) ON DELETE CASCADE,
    INDEX idx_galeria_barbero (id_barbero)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 2. TABLA DE CONFIGURACIÓN
-- =====================================================
CREATE TABLE IF NOT EXISTS configuracion (
    id_configuracion BIGINT AUTO_INCREMENT PRIMARY KEY,
    clave VARCHAR(100) UNIQUE NOT NULL,
    valor VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255),
    INDEX idx_config_clave (clave)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 3. INSERTAR CONFIGURACIONES POR DEFECTO
-- =====================================================
-- Comisiones por defecto (40% admin, 60% barbero)
INSERT INTO configuracion (clave, valor, descripcion) 
VALUES 
    ('comision_admin', '40', 'Porcentaje de comisión del administrador'),
    ('comision_barbero', '60', 'Porcentaje de comisión del barbero')
ON DUPLICATE KEY UPDATE 
    valor = VALUES(valor),
    descripcion = VALUES(descripcion);

-- =====================================================
-- 4. VERIFICAR TABLAS CREADAS
-- =====================================================
SHOW TABLES LIKE '%galeria%';
SHOW TABLES LIKE '%configuracion%';

-- =====================================================
-- 5. DESCRIBIR ESTRUCTURA DE TABLAS
-- =====================================================
DESCRIBE galeria_cortes;
DESCRIBE configuracion;

-- =====================================================
-- 6. CONSULTAR CONFIGURACIONES
-- =====================================================
SELECT 
    clave AS 'Configuración',
    valor AS 'Valor',
    descripcion AS 'Descripción'
FROM configuracion
ORDER BY clave;

-- =====================================================
-- 7. CONSULTAS ÚTILES PARA EL DASHBOARD
-- =====================================================

-- Total de reservas
SELECT COUNT(*) AS total_reservas FROM reservas;

-- Total de barberos
SELECT COUNT(*) AS total_barberos FROM barberos;

-- Total de clientes
SELECT COUNT(*) AS total_clientes FROM clientes;

-- Total de servicios
SELECT COUNT(*) AS total_servicios FROM servicios;

-- Ingresos totales (requiere que la tabla servicios tenga el campo precio)
SELECT 
    SUM(s.precio) AS ingresos_totales
FROM reservas r
INNER JOIN servicios s ON r.id_servicio = s.id_servicio;

-- Reservas por barbero
SELECT 
    b.nombre AS barbero,
    COUNT(r.id_reserva) AS total_cortes,
    SUM(s.precio) AS ingresos_generados
FROM barberos b
LEFT JOIN reservas r ON b.id_barbero = r.id_barbero
LEFT JOIN servicios s ON r.id_servicio = s.id_servicio
GROUP BY b.id_barbero, b.nombre
ORDER BY total_cortes DESC;

-- Reservas por mes
SELECT 
    DATE_FORMAT(h.fecha, '%Y-%m') AS mes,
    COUNT(*) AS cantidad_reservas,
    SUM(s.precio) AS ingresos_mes
FROM reservas r
INNER JOIN horarios_disponibles h ON r.id_horario = h.id_horario
INNER JOIN servicios s ON r.id_servicio = s.id_servicio
GROUP BY DATE_FORMAT(h.fecha, '%Y-%m')
ORDER BY mes DESC;

-- =====================================================
-- 8. FUNCIONES ÚTILES PARA ADMINISTRACIÓN
-- =====================================================

-- Obtener comisión del administrador
SELECT valor FROM configuracion WHERE clave = 'comision_admin';

-- Obtener comisión del barbero
SELECT valor FROM configuracion WHERE clave = 'comision_barbero';

-- Actualizar comisiones (ejemplo: 35% admin, 65% barbero)
-- UPDATE configuracion SET valor = '35' WHERE clave = 'comision_admin';
-- UPDATE configuracion SET valor = '65' WHERE clave = 'comision_barbero';

-- =====================================================
-- 9. VERIFICAR INTEGRIDAD DE DATOS
-- =====================================================

-- Verificar servicios sin precio
SELECT 
    id_servicio,
    nombre_servicio,
    precio
FROM servicios
WHERE precio IS NULL OR precio = 0;

-- Si hay servicios sin precio, actualizarlos (ejemplo):
-- UPDATE servicios SET precio = 25000 WHERE id_servicio = 1;

-- Verificar reservas con datos completos
SELECT 
    r.id_reserva,
    r.nombre_cliente,
    s.nombre_servicio,
    s.precio,
    b.nombre AS barbero,
    h.fecha,
    h.hora_inicio
FROM reservas r
INNER JOIN servicios s ON r.id_servicio = s.id_servicio
INNER JOIN barberos b ON r.id_barbero = b.id_barbero
INNER JOIN horarios_disponibles h ON r.id_horario = h.id_horario
ORDER BY h.fecha DESC, h.hora_inicio DESC
LIMIT 10;

-- =====================================================
-- 10. LIMPIEZA Y MANTENIMIENTO (USAR CON PRECAUCIÓN)
-- =====================================================

-- Eliminar fotos de galería sin barbero asociado
-- DELETE FROM galeria_cortes 
-- WHERE id_barbero NOT IN (SELECT id_barbero FROM barberos);

-- Eliminar horarios pasados (más de 30 días)
-- DELETE FROM horarios_disponibles 
-- WHERE fecha < DATE_SUB(CURDATE(), INTERVAL 30 DAY);

-- =====================================================
-- FIN DEL SCRIPT
-- =====================================================

SELECT '¡Script ejecutado exitosamente!' AS mensaje;
SELECT 'Sistema de Galería y Dashboard configurado correctamente' AS estado;

