package com.example.barberia.repository

import com.example.barberia.interfaces.RetrofitClient
import com.example.barberia.model.Cliente

class ClienteRepository {

    suspend fun obtenerClientes(): List<Cliente> {
        return RetrofitClient.apiService.obtenerClientes()
    }

    suspend fun obtenerCliente(id: Long): Cliente {
        return RetrofitClient.apiService.obtenerCliente(id)
    }

    suspend fun guardarCliente(cliente: Cliente, idAdministrador: Long): Cliente {
        return RetrofitClient.apiService.guardarCliente(cliente, idAdministrador)
    }

    suspend fun eliminarCliente(id: Long, idAdministrador: Long) {
        RetrofitClient.apiService.eliminarCliente(id, idAdministrador)
    }

}