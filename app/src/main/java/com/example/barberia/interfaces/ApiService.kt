package com.example.barberia.interfaces

import com.example.barberia.model.Administrador
import com.example.barberia.model.Barbero
import com.example.barberia.model.Cliente
import com.example.barberia.model.HorarioDisponible
import com.example.barberia.model.HorarioUi
import com.example.barberia.model.Reserva
import com.example.barberia.model.Servicio
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
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

  @GET("/horarios")
  suspend fun obtenerHorarios(
   @Query("idbarbero") idBarbero: Long
  ): List<HorarioDisponible>

 @GET("/horarios/disponibles")
 suspend fun obtenerHorariosDisponibles(
  @Query("idbarbero") idBarbero: Long,
  @Query("fecha") fecha: String
 ): List<HorarioUi>

 // Obtener un horario específico por su ID
 @GET("/horarios/{id}")
 suspend fun obtenerHorario(@Path("id") id: Long): HorarioDisponible

 // Guardar un nuevo horario
 @POST("/horarios")
 suspend fun guardarHorario(@Body horario: HorarioDisponible): HorarioDisponible

 // Eliminar un horario por su ID
 @DELETE("/horarios/{id}")
 suspend fun eliminarHorario(@Path("id") id: Long)

 @PATCH("horarios/{id}/disponibilidad")
 suspend fun actualizarDisponibilidadHorario(
  @Path("id") idHorario: Long,
  @Query("disponible") disponible: Boolean
 )


 @GET("/api/reservas")
 suspend fun obtenerReservas(): List<Reserva>

 @GET("reservas")
 suspend fun listarReservas(): List<Reserva>


 @GET("/api/reservas/{id}")
 suspend fun obtenerReserva(@Path("id") id: Long): Reserva


 @POST("/reservas")
 suspend fun guardarReserva(
  @Body reserva: Reserva,
  @Query("idAdministrador") idAdministrador: Long
 ): retrofit2.Response<Reserva>


 @DELETE("/reservas/{id}")
 suspend fun eliminarReserva(
  @Path("id") id: Long,
  @Query("idAdministrador") idAdministrador: Long
 )



 @POST("/clientes")
 suspend fun guardarCliente(
  @Body cliente: Cliente,
  @Query("idAdministrador") idAdministrador: Long
 ): Cliente

 @DELETE("/clientes/{id}")
 suspend fun eliminarCliente(
  @Path("id") id: Long,
  @Query("idAdministrador") idAdministrador: Long
 )


 @GET("/clientes")
 suspend fun obtenerClientes(): List<Cliente>

 @GET("/clientes/{id}")
 suspend fun obtenerCliente(@Path("id") id: Long): Cliente

 @GET("reservas/barbero/{idBarbero}")
 suspend fun obtenerReservasPorBarbero(@Path("idBarbero") idBarbero: Long): List<Reserva>
 @GET("horarios")
 suspend fun obtenerTodosLosHorarios(): List<HorarioDisponible>

 @PATCH("/reservas/{id}/estado")
 suspend fun actualizarEstadoReserva(
  @Path("id") id: Long,
  @Query("estado") estado: String,
  @Query("idAdministrador") idAdministrador: Long
 ): Response<Reserva>

 @GET("/reservas/barbero/{idBarbero}/fecha")
 suspend fun reservasPorBarberoYFecha(
  @Path("idBarbero") idBarbero: Long,
  @Query("fecha") fecha: String, // Formato: "2024-05-21"
  @Query("estado") estado: String? = null // Puede ser null para traer todas
 ): Response<List<Reserva>>

 @GET("/reservas/{id}")
 suspend fun obtenerReservaPorId(@Path("id") id: Long): Response<Reserva>




}


