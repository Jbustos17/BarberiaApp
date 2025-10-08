package com.example.barberia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberia.model.AuthResponse
import com.example.barberia.model.ClienteLogin
import com.example.barberia.model.ClienteRegistro
import com.example.barberia.model.ClienteResponse
import com.example.barberia.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    sealed class AuthState {
        object Idle : AuthState()
        object Unauthenticated : AuthState()
        data class Authenticated(val cliente: ClienteResponse?) : AuthState()
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentCliente = MutableStateFlow<ClienteResponse?>(null)
    val currentCliente: StateFlow<ClienteResponse?> = _currentCliente.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        checkAuthStatus()
    }

    fun registrarCliente(cliente: ClienteRegistro) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            authRepository.registrarCliente(cliente)
                .onSuccess { authResponse ->
                    _authState.value = AuthState.Authenticated(authResponse.cliente!!)
                    _currentCliente.value = authResponse.cliente
                    _isLoading.value = false
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "Error en el registro"
                    _isLoading.value = false
                }
        }
    }

    fun loginCliente(login: ClienteLogin) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            authRepository.loginCliente(login)
                .onSuccess { authResponse ->
                    _authState.value = AuthState.Authenticated(authResponse.cliente!!)
                    _currentCliente.value = authResponse.cliente
                    _isLoading.value = false
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "Error en el login"
                    _isLoading.value = false
                }
        }
    }

    fun verificarToken() {
        viewModelScope.launch {
            authRepository.verificarToken()
                .onSuccess { authResponse ->
                    _authState.value = AuthState.Authenticated(authResponse.cliente!!)
                    _currentCliente.value = authResponse.cliente
                }
                .onFailure {
                    _authState.value = AuthState.Unauthenticated
                    _currentCliente.value = null
                }
        }
    }

    fun logout() {
        authRepository.logout()
        _authState.value = AuthState.Unauthenticated
        _currentCliente.value = null
        _errorMessage.value = null
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }

    private fun checkAuthStatus() {
        if (authRepository.isLoggedIn()) {
            _currentCliente.value = authRepository.getCurrentCliente()
            _authState.value = AuthState.Authenticated(authRepository.getCurrentCliente())
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }
}

