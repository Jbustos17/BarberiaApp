package com.example.barberia.model

import com.google.gson.annotations.SerializedName

data class ClienteRegistro(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("celular") val celular: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("contraseña") val contraseña: String,
    @SerializedName("direccion") val direccion: String
)

