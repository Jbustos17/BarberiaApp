package com.example.barberia.interfaces

import com.example.barberia.model.Administrador
import com.example.barberia.model.Barbero
import com.example.barberia.model.Cliente
import com.example.barberia.model.HorarioDisponible
import com.example.barberia.model.Reserva
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.barberia.model.Servicio
import retrofit2.http.*

interface ApiService {
    @GET("/api/barberos")
    suspend fun obtenerBarberos(): List<Barbero>

    @GET("/api/barberos/{id}")
    suspend fun obtenerBarbero(@Path("id") id: Long): Barbero

    @POST("/api/barberos")
    suspend fun guardarBarbero(@Body barbero: Barbero): Barbero

    @DELETE("/api/barberos/{id}")
    suspend fun eliminarBarbero(@Path("id") id: Long)


   //Servicios
    @GET("/api/servicios")
    suspend fun obtenerServicios(): List<Servicio>

    @GET("/api/servicios/{id}")
    suspend fun obtenerServicio(@Path("id") id: Long): Servicio

    @POST("/api/servicios")
    suspend fun guardarServicio(@Body servicio: Servicio): Servicio

    @DELETE("/api/servicios/{id}")
    suspend fun eliminarServicio(@Path("id") id: Long)


    //horariodisponible
    @GET("/api/horarios")
    suspend fun obtenerHorarios(): List<HorarioDisponible>

    @GET("/api/horarios/{id}")
    suspend fun obtenerHorario(@Path("id") id: Long): HorarioDisponible

    @POST("/api/horarios")
    suspend fun guardarHorario(@Body horario: HorarioDisponible): HorarioDisponible

    @DELETE("/api/horarios/{id}")
    suspend fun eliminarHorario(@Path("id") id: Long)


//cliente

    @GET("/api/clientes")
    suspend fun obtenerClientes(): List<Cliente>

    @GET("/api/clientes/{id}")
    suspend fun obtenerCliente(@Path("id") id: Long): Cliente

    @POST("/api/clientes")
    suspend fun guardarCliente(@Body cliente: Cliente): Cliente

    @DELETE("/api/clientes/{id}")
    suspend fun eliminarCliente(@Path("id") id: Long)


//Reserva

    @GET("/api/reservas")
    suspend fun obtenerReservas(): List<Reserva>

    @GET("/api/reservas/{id}")
    suspend fun obtenerReserva(@Path("id") id: Long): Reserva

    @POST("/api/reservas")
    suspend fun guardarReserva(@Body reserva: Reserva): Reserva

    @DELETE("/api/reservas/{id}")
    suspend fun eliminarReserva(@Path("id") id: Long)


    //administrador
    @GET("/api/administradores")
    suspend fun obtenerAdministradores(): List<Administrador>

    @GET("/api/administradores/{id}")
    suspend fun obtenerAdministrador(@Path("id") id: Long): Administrador

    @POST("/api/administradores")
    suspend fun guardarAdministrador(@Body administrador: Administrador): Administrador

    @DELETE("/api/administradores/{id}")
    suspend fun eliminarAdministrador(@Path("id") id: Long)


}



