package com.example.barberia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberia.model.Cupon
import com.example.barberia.repository.CuponRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CuponViewModel : ViewModel() {
    private val repository = CuponRepository()

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
                _cupones.value = repository.listarCupones(idAdministrador, filtro)
            } catch (e: Exception) {
                _error.value = "Error al cargar cupones: ${e.message}"
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
                repository.actualizarCupon(idAdministrador, id, codigo, porcentajeDescuento, fechaValidez, activo)
                _mensaje.value = "Cupón actualizado exitosamente"
                cargarCupones(idAdministrador, "todos")
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al actualizar cupón"
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
                repository.eliminarCupon(idAdministrador, id)
                _mensaje.value = "Cupón eliminado exitosamente"
                cargarCupones(idAdministrador, "todos")
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al eliminar cupón"
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



