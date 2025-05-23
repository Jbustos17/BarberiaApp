package com.example.barberia.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import java.net.URLDecoder

// Define el ID de administrador por defecto (ajústalo según tu base de datos)
const val ADMIN_ID_DEFAULT = 1L

@Composable
fun Navegacion(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {
        composable("inicio") { InicioScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("adminPanel") { AdminPanelScreen(navController) }
        composable("servicios") { ServicioScreen(navController) }
        composable("barberos") { BarberoScreen(navController) }
        composable("barberoLogin") { BarberoLoginScreen(navController) }

        composable("barberoPanel/{idBarbero}/{idAdministrador}") { backStackEntry ->
            val idBarbero = backStackEntry.arguments?.getString("idBarbero")?.toLongOrNull()
            // Si el valor es nulo o 0, usa el valor por defecto
            val idAdministrador = backStackEntry.arguments?.getString("idAdministrador")?.toLongOrNull()
                ?.takeIf { it != 0L } ?: ADMIN_ID_DEFAULT
            if (idBarbero != null) {
                BarberoPanelScreen(
                    idBarbero = idBarbero,
                    idAdministrador = idAdministrador,
                    navController = navController
                )
            }
        }

        composable("horarios/{idBarbero}") { backStackEntry ->
            val idBarbero = backStackEntry.arguments?.getString("idBarbero")?.toLongOrNull()
            if (idBarbero != null) {
                HorarioDisponibleScreen(idBarbero = idBarbero, navController = navController)
            }
        }

        composable(
            route = "reserva/{idBarbero}/{fecha}/{hora}/{servicioId}/{horarioDisponibleId}/{idAdministrador}"
        ) { backStackEntry ->
            val idBarbero = backStackEntry.arguments?.getString("idBarbero")?.toLongOrNull()
            val fecha = backStackEntry.arguments?.getString("fecha")
            val hora = backStackEntry.arguments?.getString("hora")?.let { URLDecoder.decode(it, "UTF-8") }
            val servicioId = backStackEntry.arguments?.getString("servicioId")?.toLongOrNull()
            val horarioDisponibleId = backStackEntry.arguments?.getString("horarioDisponibleId")?.toLongOrNull()
            val idAdministrador = backStackEntry.arguments?.getString("idAdministrador")?.toLongOrNull()
                ?.takeIf { it != 0L } ?: ADMIN_ID_DEFAULT

            if (
                idBarbero != null && fecha != null && hora != null &&
                servicioId != null && horarioDisponibleId != null && idAdministrador != null
            ) {
                ReservaScreen(
                    idBarbero = idBarbero,
                    fecha = fecha,
                    hora = hora,
                    servicioId = servicioId,
                    horarioDisponibleId = horarioDisponibleId,
                    idAdministrador = idAdministrador,
                    navController = navController
                )
            }
        }
    }
}
