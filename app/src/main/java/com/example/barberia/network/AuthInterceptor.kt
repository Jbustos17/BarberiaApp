package com.example.barberia.network

import okhttp3.Interceptor
import okhttp3.Response
import com.example.barberia.utils.SessionManager

class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Agregar headers de autenticación si están disponibles
        val authHeaders = sessionManager.getAuthHeaders()
        
        val newRequest = if (authHeaders.isNotEmpty()) {
            originalRequest.newBuilder()
                .addHeader("Authorization", authHeaders["Authorization"]!!)
                .build()
        } else {
            originalRequest
        }
        
        return chain.proceed(newRequest)
    }
}

