package com.example.barberia.model


data class Administrador(
    val id_admin: Long = 0,
    val nombre: String,
    val usuario: String,
    val contrasenia: String,
    val correo: String,
    val rol: String
)