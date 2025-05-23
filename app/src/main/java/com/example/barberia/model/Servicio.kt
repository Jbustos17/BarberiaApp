package com.example.barberia.model

import com.google.gson.annotations.SerializedName
import com.example.barberia.R

data class Servicio(
    @SerializedName("idServicio") val idServicio: Long? = null,
    @SerializedName("nombreServicio") val nombre: String? = null,
    @SerializedName("descripcion") val descripcion: String? = null,
    val fotoUrl: String? = null,
    @SerializedName("precio") val precio: Double? = null
)

 {
    fun iconoResId(): Int = when (nombre?.lowercase()?.trim()) {
        "corte" -> R.drawable.ic_corte
        "corte y cejas" -> R.drawable.ic_cejas
        "corte y barba" -> R.drawable.ic_barba
        "corte vip" -> R.drawable.ic_vip
        "corte y mascarilla" -> R.drawable.ic_mascarilla
        else -> R.drawable.ic_placeholder_servicio
    }

}

