package com.example.barberia.model

import com.google.gson.annotations.SerializedName

data class DashboardEstadisticas(
    @SerializedName("totalReservas") val totalReservas: Long = 0,
    @SerializedName("totalBarberos") val totalBarberos: Long = 0,
    @SerializedName("totalClientes") val totalClientes: Long = 0,
    @SerializedName("totalServicios") val totalServicios: Long = 0,
    @SerializedName("ingresosTotales") val ingresosTotales: Double = 0.0,
    @SerializedName("comisionAdministrador") val comisionAdministrador: Double = 0.0,
    @SerializedName("comisionBarberos") val comisionBarberos: Double = 0.0,
    @SerializedName("porcentajeComisionAdmin") val porcentajeComisionAdmin: Double = 40.0,
    @SerializedName("porcentajeComisionBarbero") val porcentajeComisionBarbero: Double = 60.0,
    @SerializedName("estadisticasPorBarbero") val estadisticasPorBarbero: List<EstadisticasBarbero> = emptyList(),
    @SerializedName("reservasPorMes") val reservasPorMes: List<ReservaPorMes> = emptyList(),
    // Estadísticas segmentadas por modalidad
    @SerializedName("reservasPresenciales") val reservasPresenciales: Long = 0,
    @SerializedName("reservasDomicilio") val reservasDomicilio: Long = 0,
    @SerializedName("ingresosPresenciales") val ingresosPresenciales: Double = 0.0,
    @SerializedName("ingresosDomicilio") val ingresosDomicilio: Double = 0.0
)

data class EstadisticasBarbero(
    @SerializedName("idBarbero") val idBarbero: Long,
    @SerializedName("nombreBarbero") val nombreBarbero: String,
    @SerializedName("totalCortes") val totalCortes: Long,
    @SerializedName("ingresosGenerados") val ingresosGenerados: Double,
    @SerializedName("comisionBarbero") val comisionBarbero: Double,
    @SerializedName("comisionAdmin") val comisionAdmin: Double,
    // Estadísticas segmentadas por modalidad
    @SerializedName("cortesPresenciales") val cortesPresenciales: Long = 0,
    @SerializedName("cortesDomicilio") val cortesDomicilio: Long = 0,
    @SerializedName("ingresosPresenciales") val ingresosPresenciales: Double = 0.0,
    @SerializedName("ingresosDomicilio") val ingresosDomicilio: Double = 0.0
)

data class ReservaPorMes(
    @SerializedName("mes") val mes: String,
    @SerializedName("cantidad") val cantidad: Long,
    @SerializedName("ingresos") val ingresos: Double
)

data class ComisionesConfig(
    @SerializedName("comisionAdmin") val comisionAdmin: Double,
    @SerializedName("comisionBarbero") val comisionBarbero: Double
)

