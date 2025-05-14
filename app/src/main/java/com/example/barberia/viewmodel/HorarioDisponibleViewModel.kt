package com.example.barberia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberia.model.HorarioDisponible
import com.example.barberia.model.HorarioUi
import com.example.barberia.repository.HorarioDisponibleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HorarioDisponibleViewModel : ViewModel() {

    private val repository = HorarioDisponibleRepository()
    private var idBarbero: Long? = null

    private val _horarios = MutableStateFlow<List<HorarioDisponible>>(emptyList())
    val horarios: StateFlow<List<HorarioDisponible>> = _horarios

    private val _horasDisponibles = MutableStateFlow<List<HorarioUi>>(emptyList())
    val horasDisponibles: StateFlow<List<   HorarioUi>> = _horasDisponibles


    fun cargarHorarios(idBarbero: Long) {
        this.idBarbero = idBarbero
        viewModelScope.launch {
            _horarios.value = repository.obtenerHorarios(idBarbero)
        }
    }

    fun cargarHorasDisponibles(idBarbero: Long, fecha: String) {
        viewModelScope.launch {
            _horasDisponibles.value = repository.obtenerHorariosDisponibles(idBarbero, fecha)
        }
    }


    fun guardarHorario(horario: HorarioDisponible) {
        viewModelScope.launch {
            repository.guardarHorario(horario)
            idBarbero?.let { cargarHorarios(it) }
        }
    }

    fun eliminarHorario(id: Long) {
        viewModelScope.launch {
            repository.eliminarHorario(id)
            idBarbero?.let { cargarHorarios(it) }
        }
    }
    fun cargarTodosLosHorarios() {
        viewModelScope.launch {
            _horarios.value = repository.obtenerTodosLosHorarios()
        }
    }

}


