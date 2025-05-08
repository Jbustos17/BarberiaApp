package com.example.barberia.Repository

import com.example.barberia.interfaces.RetrofitClient
import com.example.barberia.model.Servicio

class ServicioRepository {

    suspend fun obtenerServicios(): List<Servicio> {
        return RetrofitClient.apiService.obtenerServicios()
    }

    suspend fun obtenerServicio(id: Long): Servicio {
        return RetrofitClient.apiService.obtenerServicio(id)
    }

    suspend fun guardarServicio(servicio: Servicio, idAdmin: Long): Servicio {
        return RetrofitClient.apiService.guardarServicio(servicio, idAdmin)
    }

    suspend fun eliminarServicio(id: Long, idAdmin: Long) {
        RetrofitClient.apiService.eliminarServicio(id, idAdmin)
    }

}

