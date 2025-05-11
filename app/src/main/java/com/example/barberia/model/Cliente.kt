package com.example.barberia.model

import com.google.gson.annotations.SerializedName

data class Cliente(
    @SerializedName("id_cliente") val id_cliente: Long? = null,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("celular") val celular: String,
    @SerializedName("correo") val correo: String
)

