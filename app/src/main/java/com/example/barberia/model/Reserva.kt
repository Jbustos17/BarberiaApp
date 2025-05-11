package com.example.barberia.model

import com.google.gson.annotations.SerializedName

data class BarberoIdOnly(
    @SerializedName("idBarbero") val idBarbero: Long
)

data class ServicioIdOnly(
    @SerializedName("idServicio") val idServicio: Long
)

data class HorarioIdOnly(
    @SerializedName("idHorario") val idHorario: Long
)

data class ClienteIdOnly(
    @SerializedName("id_cliente") val idCliente: Long
)

data class Reserva(
    @SerializedName("idReserva") val idReserva: Long? = null,
    @SerializedName("servicio") val servicio: ServicioIdOnly,
    @SerializedName("barbero") val barbero: BarberoIdOnly,
    @SerializedName("horarioDisponible") val horarioDisponible: HorarioIdOnly,
    @SerializedName("cliente") val cliente: ClienteIdOnly,
    @SerializedName("nombreCliente") val nombreCliente: String,
    @SerializedName("celularCliente") val celularCliente: String,
    @SerializedName("correoCliente") val correoCliente: String
)
