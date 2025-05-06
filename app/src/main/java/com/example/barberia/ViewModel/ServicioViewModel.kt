package com.example.barberia.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberia.Repository.ServicioRepository
import com.example.barberia.model.Servicio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ServicioViewModel(private val repository: ServicioRepository) : ViewModel() {

    private val _servicios = MutableStateFlow<List<Servicio>>(emptyList())
    val servicios: StateFlow<List<Servicio>> = _servicios

    fun cargarServicios() {
        viewModelScope.launch {
            try {
                _servicios.value = repository.obtenerServicios()
            } catch (e: Exception) {
                // Manejo de errores
            }
        }
    }
}
