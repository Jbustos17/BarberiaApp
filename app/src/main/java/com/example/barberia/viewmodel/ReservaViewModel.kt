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
            _reservas.value = repository.obtenerReservas()
        }
    }

    fun guardarReserva(reserva: Reserva, idAdministrador: Long) {
        viewModelScope.launch {
            var intentos = 0
            var exito = false

            while (intentos < 3 && !exito) {
                try {
                    val response = repository.guardarReserva(reserva, idAdministrador)
                    if (response.isSuccessful) {
                        exito = true

                    } else {

                        val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                        _error.value = "Error al guardar reserva: $errorMsg"
                        break
                    }
                } catch (e: Exception) {
                    intentos++
                    if (intentos == 3) {
                        _error.value = "Error de red: ${e.message}"
                    }
                }
            }
        }
    }

    fun eliminarReserva(id: Long, idAdministrador: Long) {
        viewModelScope.launch {
            try {
                repository.eliminarReserva(id, idAdministrador)
                cargarReservas() // refresca la lista
            } catch (e: Exception) {

                _error.value = "No se pudo eliminar la reserva: ${e.message}"
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
    fun actualizarEstadoReserva(
        id: Long,
        estado: String,
        idAdministrador: Long,
        idBarbero: Long,
        fecha: String
    ) {
        viewModelScope.launch {
            try {
                val response = repository.actualizarEstadoReserva(id, estado, idAdministrador)
                if (response.isSuccessful) {
                    cargarReservasPorBarberoYFecha(idBarbero, fecha) // Recarga las reservas filtradas
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                    _error.value = "No se pudo actualizar el estado: $errorMsg"
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.message}"
            }
        }
    }

    fun cargarReservasPorBarberoYFecha(
        idBarbero: Long,
        fecha: String,
        estado: String? = null
    ) {
        viewModelScope.launch {
            try {
                val response = repository.reservasPorBarberoYFecha(idBarbero, fecha, estado)
                if (response.isSuccessful) {
                    _reservas.value = response.body() ?: emptyList()
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                    _error.value = "No se pudo cargar reservas: $errorMsg"
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.message}"
            }
        }
    }
}




