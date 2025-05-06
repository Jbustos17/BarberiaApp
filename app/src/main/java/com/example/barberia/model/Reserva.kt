package com.example.barberia.model

data class Reserva(
    val id_reserva: Long,
    val fecha: String,           // "2025-05-10"
    val hora: String,            // "14:00"
    val clienteId: Long,         // FK de Cliente
    val barberoId: Long,         // FK de Barbero
    val servicioId: Long,        // FK de Servicio
    val horarioDisponibleId: Long // FK de HorarioDisponible
)
