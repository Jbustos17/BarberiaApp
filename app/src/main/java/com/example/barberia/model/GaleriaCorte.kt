package com.example.barberia.model

import com.google.gson.annotations.SerializedName

data class GaleriaCorte(
    @SerializedName("idGaleria") val id: Long? = null,
    @SerializedName("barbero") val barbero: BarberoIdOnly? = null,
    @SerializedName("fotoUrl") val fotoUrl: String,
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("fechaSubida") val fechaSubida: String? = null
)

