package com.example.barberia.repository

import com.example.barberia.interfaces.AuthService
import com.example.barberia.model.AuthResponse
import com.example.barberia.model.ClienteLogin
import com.example.barberia.model.ClienteRegistro
import com.example.barberia.model.ClienteResponse
import com.example.barberia.utils.SessionManager
import com.example.barberia.config.AppConfig
import retrofit2.Response
import okhttp3.ResponseBody
import java.io.IOException

class AuthRepository(
    private val authService: AuthService,
    private val sessionManager: SessionManager
) {
    
    suspend fun registrarCliente(cliente: ClienteRegistro): Result<AuthResponse> {
        return try {
            val response = authService.registrarCliente(cliente)
            when (response.code()) {
                201 -> {
                    val authResponse = response.body()
                    if (authResponse != null && authResponse.success && authResponse.token != null && authResponse.cliente != null) {
                        sessionManager.saveToken(authResponse.token!!)
                        sessionManager.saveCliente(authResponse.cliente!!)
                        Result.success(authResponse)
                    } else {
                        val errorMsg = authResponse?.message ?: "Error: Respuesta inválida del servidor"
                        Result.failure(Exception(errorMsg))
                    }
                }
                400 -> {
                    val errorBody = try {
                        response.errorBody()?.string() ?: ""
                    } catch (e: IOException) {
                        ""
                    }
                    Result.failure(Exception("Error de validación: ${response.message()}. $errorBody"))
                }
                409 -> {
                    Result.failure(Exception("El correo electrónico ya está registrado"))
                }
                500 -> {
                    Result.failure(Exception("Error del servidor. Por favor intenta más tarde"))
                }
                else -> {
                    val errorBody = try {
                        response.errorBody()?.string() ?: ""
                    } catch (e: IOException) {
                        ""
                    }
                    Result.failure(Exception("Error en el registro (${response.code()}): ${response.message()}. $errorBody"))
                }
            }
        } catch (e: java.net.UnknownHostException) {
            Result.failure(Exception("Error de conexión. Verifica tu conexión a internet"))
        } catch (e: java.net.SocketTimeoutException) {
            Result.failure(Exception("Tiempo de espera agotado. Verifica tu conexión"))
        } catch (e: Exception) {
            Result.failure(Exception("Error al registrar: ${e.message ?: e.javaClass.simpleName}"))
        }
    }

    suspend fun loginCliente(login: ClienteLogin): Result<AuthResponse> {
        return try {
            val response = authService.loginCliente(login)
            when (response.code()) {
                200 -> {
                    val authResponse = response.body()!!
                    if (authResponse.success && authResponse.token != null && authResponse.cliente != null) {
                        sessionManager.saveToken(authResponse.token!!)
                        sessionManager.saveCliente(authResponse.cliente!!)
                        Result.success(authResponse)
                    } else {
                        Result.failure(Exception(authResponse.message))
                    }
                }
                401 -> {
                    Result.failure(Exception(AppConfig.ErrorMessages.INVALID_CREDENTIALS))
                }
                400 -> {
                    Result.failure(Exception(AppConfig.ErrorMessages.INVALID_DATA))
                }
                else -> {
                    Result.failure(Exception("Error en el login: ${response.message()}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verificarToken(): Result<AuthResponse> {
        return try {
            val token = sessionManager.getToken()
            if (token != null) {
                val response = authService.verificarToken("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!
                    if (authResponse.success) {
                        Result.success(authResponse)
                    } else {
                        sessionManager.logout()
                        Result.failure(Exception("Token expirado"))
                    }
                } else {
                    sessionManager.logout()
                    Result.failure(Exception("Token inválido"))
                }
            } else {
                Result.failure(Exception("No hay token guardado"))
            }
        } catch (e: Exception) {
            sessionManager.logout()
            Result.failure(e)
        }
    }

    suspend fun obtenerPerfil(): Result<ClienteResponse> {
        return try {
            val token = sessionManager.getToken()
            if (token != null) {
                val response = authService.obtenerPerfil("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    val cliente = response.body()!!
                    sessionManager.saveCliente(cliente)
                    Result.success(cliente)
                } else {
                    Result.failure(Exception("Error al obtener perfil: ${response.message()}"))
                }
            } else {
                Result.failure(Exception("No hay token guardado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        sessionManager.logout()
    }

    fun isLoggedIn(): Boolean {
        return sessionManager.isLoggedIn()
    }

    fun getCurrentCliente(): ClienteResponse? {
        return sessionManager.getCliente()
    }
}

