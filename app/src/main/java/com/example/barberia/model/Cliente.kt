package com.example.barberia.model

data class Cliente(
    val id_cliente: Long = 0,
    val nombre: String,
    val celular: String,
    val correo: String
)
