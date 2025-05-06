package com.example.barberia.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberia.model.HorarioDisponible
import com.example.barberia.Repository.HorarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HorarioViewModel : ViewModel() {

    private val repository = HorarioRepository()

    private val _horarios = MutableStateFlow<List<HorarioDisponible>>(emptyList())
    val horarios: StateFlow<List<HorarioDisponible>> = _horarios

    fun cargarHorarios() {
        viewModelScope.launch {
            _horarios.value = repository.obtenerHorarios()
        }
    }

    fun guardarHorario(horario: HorarioDisponible) {
        viewModelScope.launch {
            repository.guardarHorario(horario)
            cargarHorarios()
        }
    }

    fun eliminarHorario(id: Long) {
        viewModelScope.launch {
            repository.eliminarHorario(id)
            cargarHorarios()
        }
    }
}
