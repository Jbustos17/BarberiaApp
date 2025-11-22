package com.example.barberia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberia.model.ComisionesConfig
import com.example.barberia.model.DashboardEstadisticas
import com.example.barberia.model.EstadisticasBarbero
import com.example.barberia.repository.DashboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
    private val repository = DashboardRepository()

    private val _estadisticas = MutableStateFlow<DashboardEstadisticas?>(null)
    val estadisticas: StateFlow<DashboardEstadisticas?> = _estadisticas

    private val _estadisticasBarberos = MutableStateFlow<List<EstadisticasBarbero>>(emptyList())
    val estadisticasBarberos: StateFlow<List<EstadisticasBarbero>> = _estadisticasBarberos

    private val _comisiones = MutableStateFlow<ComisionesConfig?>(null)
    val comisiones: StateFlow<ComisionesConfig?> = _comisiones

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    fun cargarEstadisticas() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _estadisticas.value = repository.obtenerEstadisticasDashboard()
            } catch (e: Exception) {
                _error.value = "Error al cargar estadísticas: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun cargarEstadisticasBarberos() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _estadisticasBarberos.value = repository.obtenerEstadisticasBarberos()
            } catch (e: Exception) {
                _error.value = "Error al cargar estadísticas de barberos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun cargarComisiones() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _comisiones.value = repository.obtenerComisiones()
            } catch (e: Exception) {
                _error.value = "Error al cargar comisiones: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun actualizarComisiones(comisionAdmin: Double, comisionBarbero: Double) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val comisiones = ComisionesConfig(comisionAdmin, comisionBarbero)
                val mensaje = repository.actualizarComisiones(comisiones)
                _mensaje.value = mensaje
                _comisiones.value = comisiones
                // Recargar estadísticas después de actualizar
                cargarEstadisticas()
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al actualizar comisiones"
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

    fun enviarCorreosInformativos(idAdministrador: Long, mensaje: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val resultado = repository.enviarCorreosInformativos(idAdministrador, mensaje)
                _mensaje.value = resultado
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al enviar correos informativos"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun enviarCorreosPromocion(idAdministrador: Long, nombreCupon: String, porcentajeDescuento: Int, fechaValidez: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val resultado = repository.enviarCorreosPromocion(idAdministrador, nombreCupon, porcentajeDescuento, fechaValidez)
                _mensaje.value = resultado
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al enviar correos de promoción"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

