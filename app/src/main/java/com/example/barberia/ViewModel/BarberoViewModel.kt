package com.example.barberia.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberia.model.Barbero
import com.example.barberia.Repository.BarberoRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BarberoViewModel : ViewModel() {

    private val repository = BarberoRepository()
    private val _barberos = MutableStateFlow<List<Barbero>>(emptyList())
    val barberos: StateFlow<List<Barbero>> = _barberos

    fun cargarBarberos() {
        viewModelScope.launch {
            _barberos.value = repository.obtenerBarberos()
        }
    }

    fun agregarBarbero(barbero: Barbero) {
        viewModelScope.launch {
            repository.guardarBarbero(barbero)
            cargarBarberos()
        }
    }

    fun eliminarBarbero(id: Long) {
        viewModelScope.launch {
            repository.eliminarBarbero(id)
            cargarBarberos()
        }
    }
}
