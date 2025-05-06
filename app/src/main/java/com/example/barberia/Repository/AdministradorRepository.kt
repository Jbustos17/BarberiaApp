package com.example.barberia.Repository


import com.example.barberia.interfaces.RetrofitClient
import com.example.barberia.model.Administrador

class AdministradorRepository {

    suspend fun obtenerAdministradores(): List<Administrador> {
        return RetrofitClient.apiService.obtenerAdministradores()
    }

    suspend fun obtenerAdministrador(id: Long): Administrador {
        return RetrofitClient.apiService.obtenerAdministrador(id)
    }

    suspend fun guardarAdministrador(administrador: Administrador): Administrador {
        return RetrofitClient.apiService.guardarAdministrador(administrador)
    }

    suspend fun eliminarAdministrador(id: Long) {
        RetrofitClient.apiService.eliminarAdministrador(id)
    }
}
