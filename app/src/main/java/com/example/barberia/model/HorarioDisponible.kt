package com.example.barberia.model

data class HorarioDisponible(
    val id_horario: Long,
    val fecha: String,
    val horaInicio: String,
    val horaFin: String,
    val barberoId: Long
)