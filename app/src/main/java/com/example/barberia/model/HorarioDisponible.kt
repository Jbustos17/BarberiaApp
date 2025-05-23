package com.example.barberia.model

import com.google.gson.annotations.SerializedName


data class HorarioDisponible(
    val idHorario: Long,
    val fecha: String? = null,
    val horaInicio: String? = null,
    val horaFin: String? = null,
    val disponible: Boolean? = null
)