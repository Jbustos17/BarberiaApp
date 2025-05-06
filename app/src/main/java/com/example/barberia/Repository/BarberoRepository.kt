package com.example.barberia.Repository

import com.example.barberia.interfaces.RetrofitClient
import com.example.barberia.model.Barbero

class BarberoRepository {
    suspend fun obtenerBarberos(): List<Barbero> {
        return RetrofitClient.apiService.obtenerBarberos()
    }

    suspend fun obtenerBarbero(id: Long): Barbero {
        return RetrofitClient.apiService.obtenerBarbero(id)
    }

    suspend fun guardarBarbero(barbero: Barbero): Barbero {
        return RetrofitClient.apiService.guardarBarbero(barbero)
    }

    suspend fun eliminarBarbero(id: Long) {
        RetrofitClient.apiService.eliminarBarbero(id)
    }
}
