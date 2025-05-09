package com.example.barberia.repository

import com.example.barberia.interfaces.ApiService
import com.example.barberia.model.Servicio
import retrofit2.Response

class ServicioRepository(private val apiService: ApiService) {

    // Obtener todos los servicios
    suspend fun obtenerServicios(idAdministrador: Long): Response<List<Servicio>> {
        return apiService.obtenerServicios(idAdministrador)
    }

    // Guardar un nuevo servicio
    suspend fun guardarServicio(servicio: Servicio, idAdministrador: Long): Response<Servicio> {
        return apiService.guardarServicio(servicio, idAdministrador)
    }

    // Eliminar un servicio
    suspend fun eliminarServicio(id: Long, idAdministrador: Long): Response<Void> {
        return apiService.eliminarServicio(id, idAdministrador)
    }
}
