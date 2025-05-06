package com.example.barberia.Repository

import com.example.barberia.interfaces.RetrofitClient
import com.example.barberia.model.Cliente

class ClienteRepository {

    suspend fun obtenerClientes(): List<Cliente> {
        return RetrofitClient.apiService.obtenerClientes()
    }

    suspend fun obtenerCliente(id: Long): Cliente {
        return RetrofitClient.apiService.obtenerCliente(id)
    }

    suspend fun guardarCliente(cliente: Cliente): Cliente {
        return RetrofitClient.apiService.guardarCliente(cliente)
    }

    suspend fun eliminarCliente(id: Long) {
        RetrofitClient.apiService.eliminarCliente(id)
    }
}
