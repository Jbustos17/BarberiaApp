package com.example.barberia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberia.interfaces.RetrofitClient
import com.example.barberia.model.Cupon
import com.example.barberia.repository.CuponRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class CuponViewModel : ViewModel() {
    private val repository = CuponRepository(RetrofitClient.apiService)

    private val _cupones = MutableStateFlow<List<Cupon>>(emptyList())
    val cupones: StateFlow<List<Cupon>> = _cupones

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    fun cargarCupones(idAdministrador: Long, filtro: String = "todos") {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val response: Response<List<Cupon>> = repository.listarCupones(idAdministrador, filtro)
                if (response.isSuccessful) {
                    _cupones.value = response.body() ?: emptyList()
                } else {
                    when (response.code()) {
                        403 -> _error.value = "No tienes permisos de administrador"
                        401 -> _error.value = "Sesión expirada. Por favor, inicia sesión nuevamente"
                        else -> _error.value = "Error al cargar cupones: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun actualizarCupon(idAdministrador: Long, id: Long, codigo: String, porcentajeDescuento: Int, fechaValidez: String, activo: Boolean) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val response: Response<Cupon> = repository.actualizarCupon(idAdministrador, id, codigo, porcentajeDescuento, fechaValidez, activo)
                if (response.isSuccessful) {
                    _mensaje.value = "Cupón actualizado exitosamente"
                    cargarCupones(idAdministrador, "todos")
                } else {
                    when (response.code()) {
                        403 -> _error.value = "No tienes permisos de administrador"
                        401 -> _error.value = "Sesión expirada. Por favor, inicia sesión nuevamente"
                        else -> _error.value = "Error al actualizar cupón: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun eliminarCupon(idAdministrador: Long, id: Long) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val response: Response<Void> = repository.eliminarCupon(idAdministrador, id)
                if (response.isSuccessful) {
                    _mensaje.value = "Cupón eliminado exitosamente"
                    cargarCupones(idAdministrador, "todos")
                } else {
                    when (response.code()) {
                        403 -> _error.value = "No tienes permisos de administrador"
                        401 -> _error.value = "Sesión expirada. Por favor, inicia sesión nuevamente"
                        else -> _error.value = "Error al eliminar cupón: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}



