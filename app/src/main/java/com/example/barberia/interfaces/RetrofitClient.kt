package com.example.barberia.interfaces

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"  // Barra final importante

    // Cliente HTTP con configuraciones mejoradas
    private val okHttpClient = OkHttpClient.Builder()
        .retryOnConnectionFailure(true)  // Reintenta conexiones fallidas
        .readTimeout(30, TimeUnit.SECONDS)  // Timeout aumentado
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY  // Logs detallados
        })
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)  // Usamos nuestro cliente configurado
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
