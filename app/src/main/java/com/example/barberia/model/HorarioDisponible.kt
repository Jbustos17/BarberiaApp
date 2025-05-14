package com.example.barberia.model

import com.google.gson.annotations.SerializedName

data class HorarioDisponible(
    val idHorario: Long,
    val fecha: String,
    val horaInicio: String,
    val horaFin: String,
    val idBarbero: Long
)