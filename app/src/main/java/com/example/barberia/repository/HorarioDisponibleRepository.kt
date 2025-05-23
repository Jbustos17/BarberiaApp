package com.example.barberia.repository

import com.example.barberia.interfaces.RetrofitClient
import com.example.barberia.interfaces.RetrofitClient.apiService
import com.example.barberia.model.HorarioDisponible
import com.example.barberia.model.HorarioUi

class HorarioDisponibleRepository {

    // Obtiene todos los horarios (no solo los disponibles)
    suspend fun obtenerHorarios(idBarbero: Long): List<HorarioDisponible> {
        return try {
            RetrofitClient.apiService.obtenerHorarios(idBarbero)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Obtiene los horarios disponibles (con idHorario y horaInicio)
    suspend fun obtenerHorariosDisponibles(idBarbero: Long, fecha: String): List<HorarioUi> {
        return try {
            RetrofitClient.apiService.obtenerHorariosDisponibles(idBarbero, fecha)
        } catch (e: Exception) {
            emptyList()
        }
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

    // En HorarioDisponibleRepository
    suspend fun obtenerTodosLosHorarios(): List<HorarioDisponible> {
        return apiService.obtenerTodosLosHorarios() // Define este endpoint en tu ApiService y backend si no existe
    }

    suspend fun actualizarDisponibilidad(idHorario: Long, disponible: Boolean): Boolean {
        return try {
            RetrofitClient.apiService.actualizarDisponibilidadHorario(idHorario, disponible)
            true
        } catch (e: Exception) {
            false
        }
    }


}
