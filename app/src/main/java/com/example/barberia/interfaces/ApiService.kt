package com.example.barberia.interfaces

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.barberia.model.Servicio
import retrofit2.http.*

interface ApiService {


   //Servicios
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




}



