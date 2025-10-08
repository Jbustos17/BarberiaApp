package com.example.barberia.interfaces

import com.example.barberia.model.AuthResponse
import com.example.barberia.model.ClienteLogin
import com.example.barberia.model.ClienteRegistro
import com.example.barberia.model.ClienteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    
    @POST("api/auth/cliente/registro")
    suspend fun registrarCliente(@Body cliente: ClienteRegistro): Response<AuthResponse>
    
    @POST("api/auth/cliente/login")
    suspend fun loginCliente(@Body login: ClienteLogin): Response<AuthResponse>
    
    @POST("api/auth/cliente/verificar-token")
    suspend fun verificarToken(@Header("Authorization") token: String): Response<AuthResponse>
    
    @GET("api/clientes/perfil")
    suspend fun obtenerPerfil(@Header("Authorization") token: String): Response<ClienteResponse>
}

