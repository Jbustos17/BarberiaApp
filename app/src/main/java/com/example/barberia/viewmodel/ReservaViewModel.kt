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
            repository.eliminarReserva(id, idAdministrador)
            cargarReservas()
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

