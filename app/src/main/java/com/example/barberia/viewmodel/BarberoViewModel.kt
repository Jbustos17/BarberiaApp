package com.example.barberia.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberia.interfaces.RetrofitClient
import com.example.barberia.model.Barbero
import com.example.barberia.repository.BarberoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BarberoViewModel : ViewModel() {

    private val barberoRepository = BarberoRepository(RetrofitClient.apiService)

    // ✅ Nuevo: MutableStateFlow para exponer los barberos
    private val _barberos = MutableStateFlow<List<Barbero>>(emptyList())
    val barberos: StateFlow<List<Barbero>> = _barberos

    // Método para guardar un barbero
    fun guardarBarbero(barbero: Barbero, idAdministrador: Long) {
        viewModelScope.launch {
            val response = barberoRepository.guardarBarbero(barbero, idAdministrador)
            if (response.isSuccessful) {
                obtenerBarberos() // Recargar barberos después de guardar
            }
        }
    }

    fun obtenerBarberos() {
        viewModelScope.launch {
            try {
                val response = barberoRepository.obtenerBarberos()
                if (response.isSuccessful) {
                    _barberos.value = response.body() ?: emptyList()
                } else {
                    Log.e("API", "Error obteniendo barberos: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API", "Error fatal: ${e.printStackTrace()}")
            }
        }
    }

    fun eliminarBarbero(id: Long, idAdministrador: Long) {
        viewModelScope.launch {
            val response = barberoRepository.eliminarBarbero(id, idAdministrador)
            if (response.isSuccessful) {
                obtenerBarberos() // Recarga la lista después de eliminar
            }
        }
    }

}
