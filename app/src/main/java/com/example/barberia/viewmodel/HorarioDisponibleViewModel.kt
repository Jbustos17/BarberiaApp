package com.example.barberia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberia.model.HorarioDisponible
import com.example.barberia.repository.HorarioDisponibleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HorarioDisponibleViewModel : ViewModel() {
    private val repository = HorarioDisponibleRepository()
    private var barberoId: Long? = null

    private val _horarios = MutableStateFlow<List<HorarioDisponible>>(emptyList())
    val horarios: StateFlow<List<HorarioDisponible>> = _horarios

    private val _horasDisponibles = MutableStateFlow<List<String>>(emptyList())
    val horasDisponibles: StateFlow<List<String>> = _horasDisponibles

    fun cargarHorarios(barberoId: Long) {
        this.barberoId = barberoId
        viewModelScope.launch {
            _horarios.value = repository.obtenerHorarios(barberoId)
        }
    }

    fun cargarHorasDisponibles(barberoId: Long, fecha: String) {
        viewModelScope.launch {
            _horasDisponibles.value = repository.obtenerHorariosDisponibles(barberoId, fecha)
        }
    }

    fun guardarHorario(horario: HorarioDisponible) {
        viewModelScope.launch {
            repository.guardarHorario(horario)
            barberoId?.let { cargarHorarios(it) }
        }
    }

    fun eliminarHorario(id: Long) {
        viewModelScope.launch {
            repository.eliminarHorario(id)
            barberoId?.let { cargarHorarios(it) }
        }
    }
}



