package com.example.barberia.model

import com.google.gson.annotations.SerializedName

data class Servicio(
    @SerializedName("idServicio") val id: Long,
    @SerializedName("nombreServicio") val nombre: String?,
    @SerializedName("descripcion") val descripcion: String?
)
