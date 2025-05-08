package com.example.barberia.interfaces

import com.example.barberia.model.Barbero
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.barberia.model.Servicio
import retrofit2.http.*

interface ApiService {

 // Servicios
 @GET("servicios")
 suspend fun obtenerServicios(): List<Servicio>

 @GET("servicios/{id}")
 suspend fun obtenerServicio(@Path("id") id: Long): Servicio

 @POST("servicios")
 suspend fun guardarServicio(
  @Body servicio: Servicio,
  @Query("idAdministrador") idAdmin: Long
 ): Servicio

 @DELETE("servicios/{id}")
 suspend fun eliminarServicio(
  @Path("id") id: Long,
  @Query("idAdministrador") idAdmin: Long
 )

 // Barberos
 @GET("barberos")
 suspend fun obtenerBarberos(): List<Barbero>

 @GET("barberos/{id}")
 suspend fun obtenerBarbero(@Path("id") id: Long): Barbero

 @POST("barberos")
 suspend fun guardarBarbero(@Body barbero: Barbero): Barbero

 @DELETE("barberos/{id}")
 suspend fun eliminarBarbero(@Path("id") id: Long)
}
