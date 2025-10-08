package com.example.barberia.model

import com.google.gson.annotations.SerializedName

data class ClienteLogin(
    @SerializedName("correo") val correo: String,
    @SerializedName("contraseña") val contraseña: String
)

