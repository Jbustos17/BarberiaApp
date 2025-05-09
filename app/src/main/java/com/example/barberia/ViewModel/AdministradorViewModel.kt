package com.example.barberia.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberia.model.Administrador
import com.example.barberia.repository.AdministradorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdministradorViewModel : ViewModel() {

    private val repository = AdministradorRepository()

    private val _administradores = MutableStateFlow<List<Administrador>>(emptyList())
    val administradores: StateFlow<List<Administrador>> = _administradores

    fun cargarAdministradores() {
        viewModelScope.launch {
            _administradores.value = repository.obtenerAdministradores()
        }
    }

    fun guardarAdministrador(administrador: Administrador) {
        viewModelScope.launch {
            repository.guardarAdministrador(administrador)
            cargarAdministradores()
        }
    }

    fun eliminarAdministrador(id: Long) {
        viewModelScope.launch {
            repository.eliminarAdministrador(id)
            cargarAdministradores()
        }
    }
}