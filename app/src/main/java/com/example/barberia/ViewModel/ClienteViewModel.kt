package com.example.barberia.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberia.model.Cliente
import com.example.barberia.Repository.ClienteRepository
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

    fun guardarCliente(cliente: Cliente) {
        viewModelScope.launch {
            repository.guardarCliente(cliente)
            cargarClientes()
        }
    }

    fun eliminarCliente(id: Long) {
        viewModelScope.launch {
            repository.eliminarCliente(id)
            cargarClientes()
        }
    }
}
