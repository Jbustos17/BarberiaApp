package com.example.barberia.repository

import com.example.barberia.interfaces.AuthService
import com.example.barberia.model.AuthResponse
import com.example.barberia.model.ClienteLogin
import com.example.barberia.model.ClienteRegistro
import com.example.barberia.model.ClienteResponse
import com.example.barberia.utils.SessionManager
import com.example.barberia.config.AppConfig
import retrofit2.Response

class AuthRepository(
    private val authService: AuthService,
    private val sessionManager: SessionManager
) {
    
    suspend fun registrarCliente(cliente: ClienteRegistro): Result<AuthResponse> {
        return try {
            val response = authService.registrarCliente(cliente)
            when (response.code()) {
                201 -> {
                    val authResponse = response.body()!!
                    if (authResponse.success && authResponse.token != null && authResponse.cliente != null) {
                        sessionManager.saveToken(authResponse.token!!)
                        sessionManager.saveCliente(authResponse.cliente!!)
                        Result.success(authResponse)
                    } else {
                        Result.failure(Exception(authResponse.message))
                    }
                }
                400 -> {
                    Result.failure(Exception(AppConfig.ErrorMessages.EMAIL_ALREADY_EXISTS))
                }
                409 -> {
                    Result.failure(Exception(AppConfig.ErrorMessages.EMAIL_ALREADY_EXISTS))
                }
                else -> {
                    Result.failure(Exception("Error en el registro: ${response.message()}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
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

