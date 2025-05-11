package com.example.barberia.repository

import com.example.barberia.interfaces.RetrofitClient
import com.example.barberia.model.HorarioDisponible

class HorarioDisponibleRepository {

    // Obtiene los horarios disponibles de un barbero específico
    suspend fun obtenerHorarios(idBarbero: Long): List<HorarioDisponible> {
        return try {
            RetrofitClient.apiService.obtenerHorarios(idBarbero)
        } catch (e: Exception) {
            // Maneja el error de red o cualquier otra excepción
            emptyList() // Retorna una lista vacía si ocurre un error
        }
    }

    suspend fun obtenerHorariosDisponibles(idBarbero: Long, fecha: String): List<String> {
        return RetrofitClient.apiService.obtenerHorariosDisponibles(idBarbero, fecha)
    }

    // Obtiene un horario específico por su ID
    suspend fun obtenerHorario(id: Long): HorarioDisponible? {
        return try {
            RetrofitClient.apiService.obtenerHorario(id)
        } catch (e: Exception) {
            null // Retorna null si ocurre un error
        }
    }

    // Guarda un nuevo horario
    suspend fun guardarHorario(horario: HorarioDisponible): HorarioDisponible? {
        return try {
            RetrofitClient.apiService.guardarHorario(horario)
        } catch (e: Exception) {
            null // Retorna null si ocurre un error
        }
    }

    // Elimina un horario específico por su ID
    suspend fun eliminarHorario(id: Long): Boolean {
        return try {
            RetrofitClient.apiService.eliminarHorario(id)
            true // Retorna true si se eliminó correctamente
        } catch (e: Exception) {
            false // Retorna false si ocurre un error
        }
    }
}
