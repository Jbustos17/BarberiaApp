package com.example.barberia.di

import android.content.Context
import com.example.barberia.interfaces.AuthService
import com.example.barberia.interfaces.RetrofitClient
import com.example.barberia.repository.AuthRepository
import com.example.barberia.utils.SessionManager
import com.example.barberia.viewmodel.AuthViewModel

object AppModule {
    
    fun provideSessionManager(context: Context): SessionManager {
        return SessionManager(context)
    }
    
    fun provideAuthService(): AuthService {
        return RetrofitClient.authService
    }
    
    fun provideAuthRepository(
        context: Context
    ): AuthRepository {
        val sessionManager = provideSessionManager(context)
        val authService = provideAuthService()
        return AuthRepository(authService, sessionManager)
    }
    
    fun provideAuthViewModel(
        context: Context
    ): AuthViewModel {
        val authRepository = provideAuthRepository(context)
        return AuthViewModel(authRepository)
    }
}
