package com.example.barberia.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberia.model.Servicio
import com.example.barberia.Repository.ServicioRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ServicioViewModel : ViewModel() {

    private val repository = ServicioRepository()

    private val _servicios = MutableStateFlow<List<Servicio>>(emptyList())
    val servicios: StateFlow<List<Servicio>> = _servicios

    fun cargarServicios() {
        viewModelScope.launch {
            _servicios.value = repository.obtenerServicios()
        }
    }

    fun guardarServicio(servicio: Servicio, idAdmin: Long) {
        viewModelScope.launch {
            repository.guardarServicio(servicio, idAdmin)
            cargarServicios()
        }
    }

    fun eliminarServicio(id: Long, idAdmin: Long) {
        viewModelScope.launch {
            repository.eliminarServicio(id, idAdmin)
            cargarServicios()
        }
    }

}

