package com.example.barberia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberia.model.GaleriaCorte
import com.example.barberia.repository.GaleriaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GaleriaViewModel : ViewModel() {
    private val repository = GaleriaRepository()

    private val _galeria = MutableStateFlow<List<GaleriaCorte>>(emptyList())
    val galeria: StateFlow<List<GaleriaCorte>> = _galeria

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarGaleria(idBarbero: Long) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _galeria.value = repository.obtenerGaleriaPorBarbero(idBarbero)
            } catch (e: Exception) {
                _error.value = "Error al cargar galería: ${e.message}"
                _galeria.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun subirFoto(idBarbero: Long, fotoUrl: String, descripcion: String?) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val galeriaCorte = GaleriaCorte(
                    fotoUrl = fotoUrl,
                    descripcion = descripcion
                )
                repository.subirFotoGaleria(idBarbero, galeriaCorte)
                cargarGaleria(idBarbero) // Recargar la galería después de subir
            } catch (e: Exception) {
                _error.value = "Error al subir foto: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun eliminarFoto(idGaleria: Long, idBarbero: Long) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                repository.eliminarFotoGaleria(idGaleria)
                cargarGaleria(idBarbero) // Recargar la galería después de eliminar
            } catch (e: Exception) {
                _error.value = "Error al eliminar foto: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}

