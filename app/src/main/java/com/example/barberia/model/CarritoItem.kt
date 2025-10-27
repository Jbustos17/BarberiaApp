package com.example.barberia.model

data class CarritoItem(
    val servicio: Servicio,
    val barberoId: Long,
    val barberoNombre: String,
    val fecha: String,
    val hora: String,
    val horarioDisponibleId: Long
) {
    fun calcularTotal(): Double {
        return servicio.precio ?: 0.0
    }
}


