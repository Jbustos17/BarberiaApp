package com.example.barberia.repository

import com.example.barberia.interfaces.RetrofitClient
import com.example.barberia.model.Reserva

class ReservaRepository {

    suspend fun obtenerReservas(): List<Reserva> {
        return RetrofitClient.apiService.obtenerReservas()
    }

    suspend fun obtenerReserva(id: Long): Reserva {
        return RetrofitClient.apiService.obtenerReserva(id)
    }

    suspend fun guardarReserva(reserva: Reserva, idAdministrador: Long): retrofit2.Response<Reserva> {
        return RetrofitClient.apiService.guardarReserva(reserva, idAdministrador)
    }

    suspend fun eliminarReserva(id: Long, idAdministrador: Long) {
        RetrofitClient.apiService.eliminarReserva(id, idAdministrador)
    }
}
