package com.example.barberia.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberia.interfaces.RetrofitClient
import com.example.barberia.model.Barbero
import com.example.barberia.repository.BarberoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BarberoViewModel : ViewModel() {

    private val barberoRepository = BarberoRepository(RetrofitClient.apiService)


    private val _barberos = MutableStateFlow<List<Barbero>>(emptyList())
    val barberos: StateFlow<List<Barbero>> = _barberos

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun guardarBarbero(barbero: Barbero, idAdministrador: Long) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val response = barberoRepository.guardarBarbero(barbero, idAdministrador)
                if (response.isSuccessful) {
                    // Refrescar la lista inmediatamente
                    obtenerBarberos()
                } else {
                    when (response.code()) {
                        403 -> _error.value = "No tienes permisos de administrador"
                        401 -> _error.value = "Sesión expirada. Por favor, inicia sesión nuevamente"
                        else -> _error.value = "Error al guardar barbero: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun obtenerBarberos() {
        viewModelScope.launch {
            try {
                _error.value = null
                val response = barberoRepository.obtenerBarberos()
                if (response.isSuccessful) {
                    _barberos.value = response.body() ?: emptyList()
                } else {
                    when (response.code()) {
                        403 -> _error.value = "No tienes permisos de administrador"
                        401 -> _error.value = "Sesión expirada. Por favor, inicia sesión nuevamente"
                        else -> _error.value = "Error al obtener barberos: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            }
        }
    }

    fun eliminarBarbero(id: Long, idAdministrador: Long) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val response = barberoRepository.eliminarBarbero(id, idAdministrador)
                if (response.isSuccessful) {
                    // Refrescar la lista inmediatamente
                    obtenerBarberos()
                } else {
                    when (response.code()) {
                        403 -> _error.value = "No tienes permisos de administrador"
                        401 -> _error.value = "Sesión expirada. Por favor, inicia sesión nuevamente"
                        else -> _error.value = "Error al eliminar barbero: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun cambiarModalidadBarbero(idBarbero: Long, modalidad: String): Boolean {
        return try {
            _error.value = null
            val body = mapOf("modalidad" to modalidad)
            val response = barberoRepository.cambiarModalidadBarbero(idBarbero, body)
            if (response.isSuccessful) {
                // Refrescar la lista para obtener el estado actualizado
                obtenerBarberos()
                true
            } else {
                _error.value = "Error al cambiar modalidad: ${response.message()}"
                false
            }
        } catch (e: Exception) {
            _error.value = "Error de conexión: ${e.message}"
            false
        }
    }

    suspend fun actualizarPrecioDomicilio(idBarbero: Long, precio: Double): Boolean {
        return try {
            _error.value = null
            val body = mapOf("precio" to precio)
            val response = barberoRepository.actualizarPrecioDomicilioBarbero(idBarbero, body)
            if (response.isSuccessful) {
                // Refrescar la lista para obtener el estado actualizado
                obtenerBarberos()
                true
            } else {
                _error.value = "Error al actualizar precio: ${response.message()}"
                false
            }
        } catch (e: Exception) {
            _error.value = "Error de conexión: ${e.message}"
            false
        }
    }

}
