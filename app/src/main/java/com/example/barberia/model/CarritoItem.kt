package com.example.barberia.model

data class CarritoItem(
    val servicio: Servicio,
    val barberoId: Long,
    val barberoNombre: String,
    val fecha: String,
    val hora: String,
    val horarioDisponibleId: Long,
    val esADomicilio: Boolean = false,
    val precioAdicionalDomicilio: Double = 0.0
) {
    fun calcularTotal(): Double {
        val precioBase = servicio.precio ?: 0.0
        return if (esADomicilio) {
            precioBase + precioAdicionalDomicilio
        } else {
            precioBase
        }
    }
}


