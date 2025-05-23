package com.example.barberia.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberia.interfaces.RetrofitClient
import com.example.barberia.model.Servicio
import com.example.barberia.repository.ServicioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class ServicioViewModel : ViewModel() {
    private val repository = ServicioRepository(RetrofitClient.apiService)

    private val _servicios = MutableStateFlow<List<Servicio>>(emptyList())
    val servicios: StateFlow<List<Servicio>> = _servicios

    fun cargarServicios(idAdministrador: Long) {
        viewModelScope.launch {
            val response = repository.obtenerServicios(idAdministrador)
            if (response.isSuccessful) {
                _servicios.value = response.body() ?: emptyList()
            } else {
                _servicios.value = emptyList()
            }
        }
    }


    fun guardarServicio(servicio: Servicio, idAdministrador: Long) {
        viewModelScope.launch {
            val response: Response<Servicio> = repository.guardarServicio(servicio, idAdministrador)
            if (response.isSuccessful) {
                cargarServicios(idAdministrador)
            } else {

            }
        }
    }



    fun eliminarServicio(id: Long, idAdministrador: Long) {
        viewModelScope.launch {
            val response: Response<Void> = repository.eliminarServicio(id, idAdministrador)
            if (response.isSuccessful) {

            } else {

            }
        }
    }
}
