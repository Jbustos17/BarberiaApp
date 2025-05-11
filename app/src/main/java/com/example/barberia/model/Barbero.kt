package com.example.barberia.model

import com.example.barberia.R

data class Barbero(
    val idBarbero: Long,
    val nombre: String,
    val especialidad: String?,
    val telefono: String?
) {
    fun fotoResId(): Int = when (nombre.lowercase().trim()) {
        "andrés ramírez" -> R.drawable.foto_andres_ramirez
        "luis torres" -> R.drawable.foto_luis_torres
        "mateo hernández" -> R.drawable.foto_mateo_hernandez
        "diego pérez" -> R.drawable.foto_diego_perez
        "samuel moreno" -> R.drawable.foto_samuel_moreno
        else -> R.drawable.ic_barbero_placeholder
    }
}

