package com.example.barberia.model

import com.google.gson.annotations.SerializedName

data class HorarioDisponible(
    val idHorario: Long,
    val fecha: String,
    val horaInicio: String,
    val horaFin: String,
    val idBarbero: Long,
    val modalidadServicio: String? = "PRESENCIAL" // PRESENCIAL, DOMICILIO, AMBOS
)

// Enum para modalidades de servicio
enum class ModalidadServicio {
    PRESENCIAL,
    DOMICILIO,
    AMBOS
}