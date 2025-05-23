package com.example.barberia.model

import com.google.gson.annotations.SerializedName


data class Reserva(
    val idReserva: Long? = null,
    val servicio: Servicio,
    val barbero: Barbero,
    val horarioDisponible: HorarioDisponible,
    val cliente: Cliente,
    val nombreCliente: String,
    val celularCliente: String,
    val correoCliente: String,
    val estado: String = "PENDIENTE"
)


