package com.example.barberia.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.barberia.model.ClienteResponse
import com.google.gson.Gson

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val PREFS_NAME = "BarberiaAppPrefs"
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_CLIENTE = "cliente_data"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    // Guardar token JWT
    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, true).apply()
    }

    // Obtener token JWT
    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    // Verificar si el usuario está logueado
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false) && getToken() != null
    }

    // Guardar datos del cliente
    fun saveCliente(cliente: ClienteResponse) {
        val clienteJson = gson.toJson(cliente)
        prefs.edit().putString(KEY_CLIENTE, clienteJson).apply()
    }

    // Obtener datos del cliente
    fun getCliente(): ClienteResponse? {
        val clienteJson = prefs.getString(KEY_CLIENTE, null)
        return if (clienteJson != null) {
            try {
                gson.fromJson(clienteJson, ClienteResponse::class.java)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    // Obtener headers de autenticación
    fun getAuthHeaders(): Map<String, String> {
        val token = getToken()
        return if (token != null) {
            mapOf("Authorization" to "Bearer $token")
        } else {
            emptyMap()
        }
    }

    // Cerrar sesión y limpiar datos
    fun logout() {
        prefs.edit().clear().apply()
    }

    // Verificar si el token está presente
    fun hasToken(): Boolean {
        return getToken() != null
    }
}

