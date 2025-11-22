package com.example.barberia.interfaces

import com.example.barberia.model.Administrador
import com.example.barberia.model.Barbero
import com.example.barberia.model.Cliente
import com.example.barberia.model.ComisionesConfig
import com.example.barberia.model.Cupon
import com.example.barberia.model.DashboardEstadisticas
import com.example.barberia.model.EstadisticasBarbero
import com.example.barberia.model.GaleriaCorte
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

 @PATCH("/barberos/{id}/modalidad")
 suspend fun cambiarModalidadBarbero(
  @Path("id") id: Long,
  @Body body: Map<String, String>
 ): Response<Barbero>

 @PATCH("/barberos/{id}/precio-domicilio")
 suspend fun actualizarPrecioDomicilioBarbero(
  @Path("id") id: Long,
  @Body body: Map<String, Double>
 ): Response<Barbero>

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

 @GET("/reservas/barbero/{idBarbero}")
 suspend fun obtenerReservasPorBarbero(@Path("idBarbero") idBarbero: Long): List<Reserva>
 @GET("/horarios")
 suspend fun obtenerTodosLosHorarios(): List<HorarioDisponible>

 // Galería de cortes
 @GET("/galeria/barbero/{idBarbero}")
 suspend fun obtenerGaleriaPorBarbero(@Path("idBarbero") idBarbero: Long): Response<List<GaleriaCorte>>

 @POST("/galeria/barbero/{idBarbero}")
 suspend fun subirFotoGaleria(
  @Path("idBarbero") idBarbero: Long,
  @Body galeriaCorte: GaleriaCorte
 ): Response<GaleriaCorte>

 @DELETE("/galeria/{idGaleria}")
 suspend fun eliminarFotoGaleria(@Path("idGaleria") idGaleria: Long): Response<Void>

 @GET("/galeria/{idGaleria}")
 suspend fun obtenerFotoGaleria(@Path("idGaleria") idGaleria: Long): Response<GaleriaCorte>

 // Dashboard y estadísticas
 @GET("/dashboard/estadisticas")
 suspend fun obtenerEstadisticasDashboard(): Response<DashboardEstadisticas>

 @GET("/dashboard/barberos")
 suspend fun obtenerEstadisticasBarberos(): Response<List<EstadisticasBarbero>>

 @GET("/dashboard/barbero/{idBarbero}")
 suspend fun obtenerEstadisticasBarbero(@Path("idBarbero") idBarbero: Long): Response<EstadisticasBarbero>

 // Configuración de comisiones
 @GET("/configuracion/comisiones")
 suspend fun obtenerComisiones(): Response<ComisionesConfig>

 @retrofit2.http.PUT("/configuracion/comisiones")
 suspend fun actualizarComisiones(@Body comisiones: ComisionesConfig): Response<Map<String, String>>

 // Cupones
 @GET("/cupones")
 suspend fun listarCupones(
  @Query("idAdministrador") idAdministrador: Long,
  @Query("filtro") filtro: String = "todos"
 ): Response<List<Cupon>>

 @PATCH("/cupones/{id}")
 suspend fun actualizarCupon(
  @Path("id") id: Long,
  @Body cupon: Cupon,
  @Query("idAdministrador") idAdministrador: Long
 ): Response<Cupon>

 @DELETE("/cupones/{id}")
 suspend fun eliminarCupon(
  @Path("id") id: Long,
  @Query("idAdministrador") idAdministrador: Long
 ): Response<Void>

 @GET("/cupones/validar/{codigo}")
 suspend fun validarCupon(
  @Path("codigo") codigo: String
 ): Response<Map<String, Any>>

 // Correos
 @POST("/api/administradores/correos/informativo")
 suspend fun enviarCorreosInformativos(
  @Query("idAdministrador") idAdministrador: Long,
  @Body request: Map<String, String>
 ): Response<Map<String, Any>>

 @POST("/api/administradores/correos/promocion")
 suspend fun enviarCorreosPromocion(
  @Query("idAdministrador") idAdministrador: Long,
  @Body request: Map<String, Any>
 ): Response<Map<String, Any>>

}


