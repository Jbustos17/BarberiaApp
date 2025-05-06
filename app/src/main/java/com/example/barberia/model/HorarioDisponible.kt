package com.example.barberia.model

data class HorarioDisponible(
    val id_horario: Long,
    val fecha: String,     // ejemplo: "2025-05-08"
    val horaInicio: String, // ejemplo: "09:00"
    val horaFin: String,    // ejemplo: "10:00"
    val barberoId: Long     // referencia al barbero asignado
)
