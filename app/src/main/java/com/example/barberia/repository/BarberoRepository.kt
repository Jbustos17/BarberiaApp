package com.example.barberia.repository


import com.example.barberia.interfaces.ApiService
import com.example.barberia.model.Barbero
import retrofit2.Response

class BarberoRepository(private val apiService: ApiService) {

    suspend fun obtenerBarberos(): Response<List<Barbero>> {
        return apiService.obtenerBarberos()
    }

    suspend fun guardarBarbero(barbero: Barbero, idAdministrador: Long): Response<Barbero> {
        return apiService.guardarBarbero(barbero, idAdministrador)
    }

    suspend fun eliminarBarbero(id: Long, idAdministrador: Long): Response<Void> {
        return apiService.eliminarBarbero(id, idAdministrador)
    }

    suspend fun cambiarModalidadBarbero(id: Long, body: Map<String, String>): Response<Barbero> {
        return apiService.cambiarModalidadBarbero(id, body)
    }

    suspend fun actualizarPrecioDomicilioBarbero(id: Long, body: Map<String, Double>): Response<Barbero> {
        return apiService.actualizarPrecioDomicilioBarbero(id, body)
    }

}
