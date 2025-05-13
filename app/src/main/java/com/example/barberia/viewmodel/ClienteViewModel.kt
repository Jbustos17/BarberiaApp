package com.example.barberia.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberia.model.Cliente
import com.example.barberia.repository.ClienteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClienteViewModel : ViewModel() {

    private val repository = ClienteRepository()

    private val _clientes = MutableStateFlow<List<Cliente>>(emptyList())
    val clientes: StateFlow<List<Cliente>> = _clientes

    fun cargarClientes() {
        viewModelScope.launch {
            _clientes.value = repository.obtenerClientes()
        }
    }

    suspend fun guardarCliente(cliente: Cliente, idAdministrador: Long): Cliente {
        return repository.guardarCliente(cliente, idAdministrador)
    }

    fun eliminarCliente(id: Long, idAdministrador: Long) {
        viewModelScope.launch {
            repository.eliminarCliente(id, idAdministrador)
            cargarClientes()
        }
    }

}