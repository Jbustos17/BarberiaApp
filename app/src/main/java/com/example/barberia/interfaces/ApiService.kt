package com.example.barberia.interfaces

import com.example.barberia.model.Administrador
import com.example.barberia.model.Barbero
import com.example.barberia.model.HorarioDisponible
import com.example.barberia.model.Servicio
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

 // Obtener lista de barberos
 @GET("/barberos")
 suspend fun obtenerBarberos(): Response<List<Barbero>>

 @POST("/barberos")
 suspend fun guardarBarbero(
  @Body barbero: Barbero,
  @Query("idAdministrador") idAdministrador: Long
 ): Response<Barbero>


 @DELETE("/barberos/{id}")
 suspend fun eliminarBarbero(
  @Path("id") id: Long,
  @Query("idAdministrador") idAdministrador: Long
 ): Response<Void>

 @GET("/servicios")
 suspend fun obtenerServicios(
  @Query("idAdministrador") idAdministrador: Long
 ): Response<List<Servicio>>

 // Guardar un servicio
 @POST("/servicios")
 suspend fun guardarServicio(
  @Body servicio: Servicio,
  @Query("idAdministrador") idAdministrador: Long
 ): Response<Servicio>

 // Eliminar un servicio
 @DELETE("/servicios/{id}")
 suspend fun eliminarServicio(
  @Path("id") id: Long,
  @Query("idAdministrador") idAdministrador: Long
 ): Response<Void>


 @GET("/api/administradores")
 suspend fun obtenerAdministradores(): List<Administrador>

 @GET("/api/administradores/{id}")
 suspend fun obtenerAdministrador(@Path("id") id: Long): Administrador

 @POST("/api/administradores")
 suspend fun guardarAdministrador(@Body administrador: Administrador): Administrador

 @DELETE("/api/administradores/{id}")
 suspend fun eliminarAdministrador(@Path("id") id: Long)


  // Obtener los horarios de un barbero específico
  @GET("/horarios")
  suspend fun obtenerHorarios(
   @Query("barberoId") barberoId: Long
  ): List<HorarioDisponible>

 @GET("/horarios/disponibles")
 suspend fun obtenerHorariosDisponibles(
  @Query("barberoId") barberoId: Long,
  @Query("fecha") fecha: String // formato "yyyy-MM-dd"
 ): List<String> // o List<LocalTime> si usas un adaptador de fecha/hora


 // Obtener un horario específico por su ID
  @GET("/horarios/{id}")
  suspend fun obtenerHorario(@Path("id") id: Long): HorarioDisponible

  // Guardar un nuevo horario
  @POST("/horarios")
  suspend fun guardarHorario(@Body horario: HorarioDisponible): HorarioDisponible

  // Eliminar un horario por su ID
  @DELETE("/horarios/{id}")
  suspend fun eliminarHorario(@Path("id") id: Long)
 }



