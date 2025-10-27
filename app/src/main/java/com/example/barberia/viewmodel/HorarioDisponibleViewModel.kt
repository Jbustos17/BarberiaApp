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
    val horasDisponibles: StateFlow<List<HorarioUi>> = _horasDisponibles

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarHorarios(idBarbero: Long) {
        this.idBarbero = idBarbero
        viewModelScope.launch {
            _horarios.value = repository.obtenerHorarios(idBarbero)
        }
    }

    fun cargarHorasDisponibles(idBarbero: Long, fecha: String) {
        viewModelScope.launch {
            try {
                android.util.Log.d("HorarioViewModel", "Cargando horarios para barbero: $idBarbero, fecha: $fecha")
                _error.value = null
                _horasDisponibles.value = repository.obtenerHorariosDisponibles(idBarbero, fecha)
                android.util.Log.d("HorarioViewModel", "Horarios cargados exitosamente: ${_horasDisponibles.value.size}")
            } catch (e: retrofit2.HttpException) {
                android.util.Log.e("HorarioViewModel", "Error HTTP al cargar horarios: ${e.code()}", e)
                when (e.code()) {
                    401 -> _error.value = "Sesión expirada. Por favor, inicia sesión nuevamente"
                    403 -> _error.value = "No tienes permisos para ver los horarios"
                    404 -> _error.value = "No se encontraron horarios para esta fecha"
                    else -> _error.value = "Error al cargar horarios: ${e.message()}"
                }
                _horasDisponibles.value = emptyList()
            } catch (e: Exception) {
                android.util.Log.e("HorarioViewModel", "Error al cargar horarios", e)
                _error.value = "Error de conexión: ${e.message}"
                _horasDisponibles.value = emptyList()
            }
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
            try {
                _error.value = null
                _horarios.value = repository.obtenerTodosLosHorarios()
            } catch (e: retrofit2.HttpException) {
                when (e.code()) {
                    403 -> _error.value = "No tienes permisos de administrador"
                    401 -> _error.value = "Sesión expirada. Por favor, inicia sesión nuevamente"
                    else -> _error.value = "Error al obtener horarios: ${e.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            }
        }
    }

}


