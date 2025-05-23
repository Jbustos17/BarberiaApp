package com.example.barberia.repository

import com.example.barberia.interfaces.RetrofitClient
import com.example.barberia.interfaces.RetrofitClient.apiService
import com.example.barberia.model.Reserva
import com.example.barberia.model.Servicio
import retrofit2.Response

class ReservaRepository {

    /*suspend fun obtenerReservas(): List<Reserva> {
        return RetrofitClient.apiService.obtenerReservas()
    }*/
    suspend fun obtenerReservas(): List<Reserva> {
        return apiService.listarReservas()
    }


    suspend fun obtenerReserva(id: Long): Reserva {
        return RetrofitClient.apiService.obtenerReserva(id)
    }

    suspend fun guardarReserva(reserva: Reserva, idAdministrador: Long): retrofit2.Response<Reserva> {
        return RetrofitClient.apiService.guardarReserva(reserva, idAdministrador)
    }
    suspend fun eliminarReserva(id: Long, idAdministrador: Long) {
        apiService.eliminarReserva(id, idAdministrador)
    }
    /*suspend fun eliminarReserva(id: Long, idAdministrador: Long) {
        RetrofitClient.apiService.eliminarReserva(id, idAdministrador)
    }*/
    suspend fun obtenerReservasPorBarbero(idBarbero: Long): List<Reserva> {
        return apiService.obtenerReservasPorBarbero(idBarbero)
    }

    suspend fun actualizarEstadoReserva(id: Long, estado: String, idAdministrador: Long): Response<Reserva> {
        return apiService.actualizarEstadoReserva(id, estado, idAdministrador)
    }

    suspend fun reservasPorBarberoYFecha(idBarbero: Long, fecha: String, estado: String? = null): Response<List<Reserva>> {
        return apiService.reservasPorBarberoYFecha(idBarbero, fecha, estado)
    }



}
