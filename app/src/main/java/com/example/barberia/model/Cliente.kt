package com.example.barberia.model

import com.google.gson.annotations.SerializedName

data class Cliente(
    @SerializedName("id_cliente") val idCliente: Long? = null, // ahora es nullable
    @SerializedName("nombre") val nombre: String? = null,
    @SerializedName("celular") val celular: String? = null,
    @SerializedName("correo") val correo: String? = null
)


