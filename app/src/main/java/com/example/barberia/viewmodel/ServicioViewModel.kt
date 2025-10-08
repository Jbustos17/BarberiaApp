package com.example.barberia.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberia.interfaces.RetrofitClient
import com.example.barberia.model.Servicio
import com.example.barberia.repository.ServicioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class ServicioViewModel : ViewModel() {
    private val servicioRepository = ServicioRepository(RetrofitClient.apiService)


    private val _servicios = MutableStateFlow<List<Servicio>>(emptyList())
    val servicios: StateFlow<List<Servicio>> = _servicios

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error


    fun cargarServicios(idAdministrador: Long) {
        viewModelScope.launch {
            try {
                _error.value = null
                val response: Response<List<Servicio>> = servicioRepository.obtenerServicios(idAdministrador)
                if (response.isSuccessful) {
                    _servicios.value = response.body() ?: emptyList()
                } else {
                    when (response.code()) {
                        403 -> _error.value = "No tienes permisos de administrador"
                        401 -> _error.value = "Sesión expirada. Por favor, inicia sesión nuevamente"
                        else -> _error.value = "Error al obtener servicios: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            }
        }
    }

    fun guardarServicio(servicio: Servicio, idAdministrador: Long) {
        viewModelScope.launch {
            val response: Response<Servicio> = servicioRepository.guardarServicio(servicio, idAdministrador)
            if (response.isSuccessful) {
                cargarServicios(idAdministrador)
            } else {

            }
        }
    }


    // Eliminar un servicio
    fun eliminarServicio(id: Long, idAdministrador: Long) {
        viewModelScope.launch {
            val response: Response<Void> = servicioRepository.eliminarServicio(id, idAdministrador)
            if (response.isSuccessful) {

            } else {

            }
        }
    }
}
