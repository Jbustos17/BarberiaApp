package com.example.barberia.config

object AppConfig {
    // URL por defecto para emulador (funciona automáticamente)
    const val BASE_URL = "http://10.0.2.2:8080/"
    
    // URLs alternativas para diferentes entornos
    const val BASE_URL_LOCALHOST = "http://localhost:8080/"
    const val BASE_URL_LOCAL_IP = "http://192.168.1.XXX:8080/"
    const val BASE_URL_PRODUCTION = "https://tu-dominio.com/"
    
    // Configuración de autenticación
    const val TOKEN_EXPIRY_HOURS = 5
    const val MIN_PASSWORD_LENGTH = 6
    const val MIN_NAME_LENGTH = 2
    const val PHONE_LENGTH_MIN = 10
    const val PHONE_LENGTH_MAX = 15
    
    // Mensajes de error comunes
    object ErrorMessages {
        const val EMAIL_ALREADY_EXISTS = "El correo ya está registrado"
        const val INVALID_CREDENTIALS = "Credenciales incorrectas"
        const val INVALID_DATA = "Datos inválidos"
        const val NETWORK_ERROR = "Error de conexión"
        const val TOKEN_EXPIRED = "Sesión expirada"
        const val SERVER_ERROR = "Error interno del servidor"
    }
    
    // Datos para testing
    object TestData {
        const val TEST_EMAIL = "test@email.com"
        const val TEST_PASSWORD = "test123"
        const val TEST_NAME = "Usuario Test"
        const val TEST_PHONE = "3001234567"
        const val TEST_ADDRESS = "Calle Test #123"
    }
}
