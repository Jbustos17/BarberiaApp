package com.example.barberia.Repository


import com.example.barberia.interfaces.RetrofitClient
import com.example.barberia.model.Reserva

class ReservaRepository {

    suspend fun obtenerReservas(): List<Reserva> {
        return RetrofitClient.apiService.obtenerReservas()
    }

    suspend fun obtenerReserva(id: Long): Reserva {
        return RetrofitClient.apiService.obtenerReserva(id)
    }

    suspend fun guardarReserva(reserva: Reserva): Reserva {
        return RetrofitClient.apiService.guardarReserva(reserva)
    }

    suspend fun eliminarReserva(id: Long) {
        RetrofitClient.apiService.eliminarReserva(id)
    }
}
