package com.example.barberia.Repository


import com.example.barberia.interfaces.RetrofitClient
import com.example.barberia.model.HorarioDisponible

class HorarioRepository {

    suspend fun obtenerHorarios(): List<HorarioDisponible> {
        return RetrofitClient.apiService.obtenerHorarios()
    }

    suspend fun obtenerHorario(id: Long): HorarioDisponible {
        return RetrofitClient.apiService.obtenerHorario(id)
    }

    suspend fun guardarHorario(horario: HorarioDisponible): HorarioDisponible {
        return RetrofitClient.apiService.guardarHorario(horario)
    }

    suspend fun eliminarHorario(id: Long) {
        RetrofitClient.apiService.eliminarHorario(id)
    }
}
