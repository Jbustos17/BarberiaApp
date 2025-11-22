package com.example.barberia.repository

import com.example.barberia.interfaces.ApiService
import com.example.barberia.model.Cupon
import retrofit2.Response

class CuponRepository(private val apiService: ApiService) {

    suspend fun listarCupones(idAdministrador: Long, filtro: String = "todos"): Response<List<Cupon>> {
        return apiService.listarCupones(idAdministrador, filtro)
    }

    suspend fun actualizarCupon(
        idAdministrador: Long,
        id: Long,
        codigo: String,
        porcentajeDescuento: Int,
        fechaValidez: String,
        activo: Boolean
    ): Response<Cupon> {
        val cupon = Cupon(
            id = id,
            codigo = codigo,
            porcentajeDescuento = porcentajeDescuento,
            fechaValidez = fechaValidez,
            activo = activo
        )
        return apiService.actualizarCupon(id, cupon, idAdministrador)
    }

    suspend fun eliminarCupon(idAdministrador: Long, id: Long): Response<Void> {
        return apiService.eliminarCupon(id, idAdministrador)
    }
}
