package com.example.barberia.interfaces

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.barberia.model.Servicio
import retrofit2.http.*

interface ServicioApi {

    @GET("/servicios")
    suspend fun obtenerServicios(): List<Servicio>

    @GET("/servicios/{id}")
    suspend fun obtenerServicio(@Path("id") id: Long): Servicio

    @POST("/servicios")
    suspend fun guardarServicio(@Body servicio: Servicio): Servicio

    @PUT("/servicios/{id}")
    suspend fun actualizarServicio(@Path("id") id: Long, @Body servicio: Servicio): Servicio

    @DELETE("/servicios/{id}")
    suspend fun eliminarServicio(@Path("id") id: Long)
}

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080"

    val servicioApi: ServicioApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServicioApi::class.java)
    }
}
