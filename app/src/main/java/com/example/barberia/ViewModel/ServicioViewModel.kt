import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
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
    private val servicioRepository = ServicioRepository(RetrofitClient.apiService)

    // Definir un StateFlow para la lista de servicios
    private val _servicios = MutableStateFlow<List<Servicio>>(emptyList())
    val servicios: StateFlow<List<Servicio>> = _servicios

    // Función para cargar los servicios desde la API
    fun cargarServicios(idAdministrador: Long) {
        viewModelScope.launch {
            val response: Response<List<Servicio>> = servicioRepository.obtenerServicios(idAdministrador)
            if (response.isSuccessful) {
                // Asignamos la respuesta al StateFlow
                _servicios.value = response.body() ?: emptyList()
            } else {
                // Manejar el error si es necesario
            }
        }
    }

    // Guardar un servicio
    fun guardarServicio(servicio: Servicio, idAdministrador: Long) {
        viewModelScope.launch {
            val response: Response<Servicio> = servicioRepository.guardarServicio(servicio, idAdministrador)
            if (response.isSuccessful) {
                // Procesar éxito
            } else {
                // Manejar error
            }
        }
    }

    // Eliminar un servicio
    fun eliminarServicio(id: Long, idAdministrador: Long) {
        viewModelScope.launch {
            val response: Response<Void> = servicioRepository.eliminarServicio(id, idAdministrador)
            if (response.isSuccessful) {
                // Eliminar servicio
            } else {
                // Manejar error
            }
        }
    }
}
