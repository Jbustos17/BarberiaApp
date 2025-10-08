package com.example.barberia.model

import com.google.gson.annotations.SerializedName

data class ClienteResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("celular") val celular: String? = null,
    @SerializedName("correo") val correo: String,
    @SerializedName("direccion") val direccion: String? = null,
    @SerializedName("role") val role: String? = null
)

