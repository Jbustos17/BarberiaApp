package com.example.barberia.model

import com.google.gson.annotations.SerializedName

data class Barbero(
    val id: Long,
    val nombre: String,
    val correo: String,
    val telefono: String,
    val especialidad: String
)

