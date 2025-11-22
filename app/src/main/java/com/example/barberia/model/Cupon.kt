package com.example.barberia.model

import com.google.gson.annotations.SerializedName

data class Cupon(
    val id: Long? = null,
    val codigo: String,
    @SerializedName("porcentajeDescuento") val porcentajeDescuento: Int,
    @SerializedName("fechaValidez") val fechaValidez: String,
    val activo: Boolean = true
)



