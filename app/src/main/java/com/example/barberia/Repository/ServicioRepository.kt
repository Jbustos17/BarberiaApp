package com.example.barberia.Repository


import com.example.barberia.interfaces.ServicioApi
import com.example.barberia.model.Servicio

class ServicioRepository(private val api: ServicioApi) {

    suspend fun obtenerServicios(): List<Servicio> = api.obtenerServicios()

    suspend fun obtenerServicio(id: Long): Servicio = api.obtenerServicio(id)

    suspend fun guardarServicio(servicio: Servicio): Servicio = api.guardarServicio(servicio)

    suspend fun actualizarServicio(id: Long, servicio: Servicio): Servicio =
        api.actualizarServicio(id, servicio)

    suspend fun eliminarServicio(id: Long) = api.eliminarServicio(id)
}
