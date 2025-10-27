package com.example.barberia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberia.model.Reserva
import com.example.barberia.repository.ReservaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class ReservaViewModel : ViewModel() {

    private val repository = ReservaRepository()

    private val _reservas = MutableStateFlow<List<Reserva>>(emptyList())
    val reservas: StateFlow<List<Reserva>> = _reservas


    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarReservas() {
        viewModelScope.launch {
            try {
                _error.value = null
                _reservas.value = repository.obtenerReservas()
            } catch (e: retrofit2.HttpException) {
                when (e.code()) {
                    403 -> _error.value = "No tienes permisos de administrador"
                    401 -> _error.value = "Sesión expirada. Por favor, inicia sesión nuevamente"
                    else -> _error.value = "Error al obtener reservas: ${e.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            }
        }
    }

    fun guardarReserva(reserva: Reserva, idAdministrador: Long) {
        viewModelScope.launch {
            try {
                _error.value = null
                val response = repository.guardarReserva(reserva, idAdministrador)
                if (response.isSuccessful) {
                    // Reserva guardada exitosamente
                    cargarReservas() // Refrescar la lista
                } else {
                    when (response.code()) {
                        404 -> _error.value = "Endpoint no encontrado. Verifica la configuración del servidor."
                        400 -> _error.value = "Datos inválidos en la reserva."
                        403 -> _error.value = "No tienes permisos para crear reservas."
                        401 -> _error.value = "Sesión expirada. Por favor, inicia sesión nuevamente."
                        else -> {
                            val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                            _error.value = "Error al guardar reserva: HTTP ${response.code()} - $errorMsg"
                        }
                    }
                }
            } catch (e: retrofit2.HttpException) {
                when (e.code()) {
                    404 -> _error.value = "Endpoint no encontrado. Verifica la configuración del servidor."
                    400 -> _error.value = "Datos inválidos en la reserva."
                    403 -> _error.value = "No tienes permisos para crear reservas."
                    401 -> _error.value = "Sesión expirada. Por favor, inicia sesión nuevamente."
                    else -> _error.value = "Error HTTP ${e.code()}: ${e.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            }
        }
    }

    fun eliminarReserva(id: Long, idAdministrador: Long, idBarbero: Long? = null) {
        viewModelScope.launch {
            try {
                _error.value = null
                repository.eliminarReserva(id, idAdministrador)
                // Si se proporciona idBarbero, recargar solo las reservas de ese barbero
                if (idBarbero != null) {
                    cargarReservasPorBarbero(idBarbero)
                } else {
                    cargarReservas() // refresca la lista completa
                }
            } catch (e: retrofit2.HttpException) {
                when (e.code()) {
                    403 -> _error.value = "No tienes permisos para eliminar reservas"
                    401 -> _error.value = "Sesión expirada. Por favor, inicia sesión nuevamente"
                    404 -> _error.value = "Reserva no encontrada"
                    else -> _error.value = "Error al eliminar reserva: ${e.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            }
        }
    }

    fun cargarReservasPorBarbero(idBarbero: Long) {
        viewModelScope.launch {
            try {
                val reservas = repository.obtenerReservasPorBarbero(idBarbero)
                _reservas.value = reservas
            } catch (e: Exception) {

            }
        }
    }



}


