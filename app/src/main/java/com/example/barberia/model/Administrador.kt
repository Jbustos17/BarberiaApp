package com.example.barberia.model


import com.google.gson.annotations.SerializedName

data class Administrador(
    @SerializedName("id_admin") val idAdmin: Long = 0,
    val nombre: String,
    val usuario: String,
    @SerializedName("contraseña") val contrasenia: String,
    val correo: String,
    val rol: String
)

