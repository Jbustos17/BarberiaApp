package com.example.barberia.interfaces

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.barberia.network.AuthInterceptor
import com.example.barberia.utils.SessionManager
import com.example.barberia.config.AppConfig
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private val BASE_URL = AppConfig.BASE_URL

    private var sessionManager: SessionManager? = null

    fun initialize(context: Context) {
        sessionManager = SessionManager(context)
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .apply {
                // Agregar interceptor de auth si está disponible
                sessionManager?.let { 
                    addInterceptor(AuthInterceptor(it))
                }
            }
            .build()
    }

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    val authService: AuthService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthService::class.java)
    }



}



