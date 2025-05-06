package com.example.barberia.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberia.model.Reserva
import com.example.barberia.Repository.ReservaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReservaViewModel : ViewModel() {

    private val repository = ReservaRepository()

    private val _reservas = MutableStateFlow<List<Reserva>>(emptyList())
    val reservas: StateFlow<List<Reserva>> = _reservas

    fun cargarReservas() {
        viewModelScope.launch {
            _reservas.value = repository.obtenerReservas()
        }
    }

    fun guardarReserva(reserva: Reserva) {
        viewModelScope.launch {
            repository.guardarReserva(reserva)
            cargarReservas()
        }
    }

    fun eliminarReserva(id: Long) {
        viewModelScope.launch {
            repository.eliminarReserva(id)
            cargarReservas()
        }
    }
}
