package com.example.barberia.repository

import com.example.barberia.interfaces.ApiService
import com.example.barberia.interfaces.RetrofitClient
import com.example.barberia.model.GaleriaCorte

class GaleriaRepository {
    private val apiService: ApiService = RetrofitClient.apiService

    suspend fun obtenerGaleriaPorBarbero(idBarbero: Long): List<GaleriaCorte> {
        val response = apiService.obtenerGaleriaPorBarbero(idBarbero)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Error al obtener galería: ${response.code()}")
        }
    }

    suspend fun subirFotoGaleria(idBarbero: Long, galeriaCorte: GaleriaCorte): GaleriaCorte {
        val response = apiService.subirFotoGaleria(idBarbero, galeriaCorte)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Error al subir foto: ${response.code()}")
        }
    }

    suspend fun eliminarFotoGaleria(idGaleria: Long) {
        val response = apiService.eliminarFotoGaleria(idGaleria)
        if (!response.isSuccessful) {
            throw Exception("Error al eliminar foto: ${response.code()}")
        }
    }
}

