package com.example.barberia.repository

import com.example.barberia.interfaces.RetrofitClient
import com.example.barberia.model.HorarioDisponible

class HorarioDisponibleRepository {

    // Obtiene los horarios disponibles de un barbero específico
    suspend fun obtenerHorarios(idBarbero: Long): List<HorarioDisponible> {
        return try {
            RetrofitClient.apiService.obtenerHorarios(idBarbero)
        } catch (e: Exception) {

            emptyList()
        }
    }

    suspend fun obtenerHorariosDisponibles(idBarbero: Long, fecha: String): List<String> {
        return RetrofitClient.apiService.obtenerHorariosDisponibles(idBarbero, fecha)
    }


    suspend fun obtenerHorario(id: Long): HorarioDisponible? {
        return try {
            RetrofitClient.apiService.obtenerHorario(id)
        } catch (e: Exception) {
            null
        }
    }

    // Guarda un nuevo horario
    suspend fun guardarHorario(horario: HorarioDisponible): HorarioDisponible? {
        return try {
            RetrofitClient.apiService.guardarHorario(horario)
        } catch (e: Exception) {
            null
        }
    }


    suspend fun eliminarHorario(id: Long): Boolean {
        return try {
            RetrofitClient.apiService.eliminarHorario(id)
            true
        } catch (e: Exception) {
            false
        }
    }
}
