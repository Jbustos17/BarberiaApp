package com.example.barberia.model

import com.example.barberia.R
import com.google.gson.annotations.SerializedName

data class Barbero(
    val idBarbero: Long? = null,
    val nombre: String,
    val telefono: String?,
    val usuario: String?,
    val fotoUrl: String? = null,
    @SerializedName("contraseña") val contrasenia: String,
    val modalidadActual: String? = "PRESENCIAL", // PRESENCIAL o DOMICILIO (no puede ser AMBOS)
    val precioAdicionalDomicilio: Double? = 10000.0 // Precio adicional que cobra por servicio a domicilio
) {
    fun fotoResId(): Int = when (nombre.lowercase().trim()) {
        "andrés ramirez" -> R.drawable.foto_andres_ramirez
        "luis torres" -> R.drawable.foto_luis_torres
        "mateo hernandez" -> R.drawable.foto_mateo_hernandez
        "diego perez" -> R.drawable.foto_diego_perez
        "samuel moreno" -> R.drawable.foto_samuel_moreno
        else -> R.drawable.ic_barbero_placeholder
    }
}

