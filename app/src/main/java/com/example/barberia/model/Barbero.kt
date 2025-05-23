package com.example.barberia.model

import com.example.barberia.R
import com.google.gson.annotations.SerializedName

data class Barbero(
    val idBarbero: Long? = null,
    val nombre: String? = null,
    val telefono: String? = null,
    val usuario: String? = null,
    val fotoUrl: String? = null,
    @SerializedName("contraseña") val contrasenia: String? = null,
    @SerializedName("id_admin") val idAdministrador: Long? = null,
    val administrador: Administrador? = null
) {
    val idAdministradorSeguro: Long
        get() = administrador?.idAdmin ?: idAdministrador ?: 1L

    fun fotoResId(): Int = when (nombre?.lowercase()?.trim()) {
        "andrés ramirez" -> R.drawable.foto_andres_ramirez
        "luis torres" -> R.drawable.foto_luis_torres
        "mateo hernandez" -> R.drawable.foto_mateo_hernandez
        "diego perez" -> R.drawable.foto_diego_perez
        "samuel moreno" -> R.drawable.foto_samuel_moreno
        else -> R.drawable.ic_barbero_placeholder
    }
}

