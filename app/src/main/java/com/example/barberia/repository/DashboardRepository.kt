package com.example.barberia.repository

import com.example.barberia.interfaces.ApiService
import com.example.barberia.interfaces.RetrofitClient
import com.example.barberia.model.ComisionesConfig
import com.example.barberia.model.DashboardEstadisticas
import com.example.barberia.model.EstadisticasBarbero

class DashboardRepository {
    private val apiService: ApiService = RetrofitClient.apiService

    suspend fun obtenerEstadisticasDashboard(): DashboardEstadisticas {
        val response = apiService.obtenerEstadisticasDashboard()
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Error al obtener estadísticas: ${response.code()}")
        }
    }

    suspend fun obtenerEstadisticasBarberos(): List<EstadisticasBarbero> {
        val response = apiService.obtenerEstadisticasBarberos()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Error al obtener estadísticas de barberos: ${response.code()}")
        }
    }

    suspend fun obtenerEstadisticasBarbero(idBarbero: Long): EstadisticasBarbero? {
        val response = apiService.obtenerEstadisticasBarbero(idBarbero)
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw Exception("Error al obtener estadísticas del barbero: ${response.code()}")
        }
    }

    suspend fun obtenerComisiones(): ComisionesConfig {
        val response = apiService.obtenerComisiones()
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Error al obtener comisiones: ${response.code()}")
        }
    }

    suspend fun actualizarComisiones(comisiones: ComisionesConfig): String {
        val response = apiService.actualizarComisiones(comisiones)
        if (response.isSuccessful && response.body() != null) {
            return response.body()?.get("mensaje") ?: "Comisiones actualizadas"
        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception(errorBody ?: "Error al actualizar comisiones")
        }
    }
}

