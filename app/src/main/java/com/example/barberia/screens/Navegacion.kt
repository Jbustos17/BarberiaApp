package com.example.barberia.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun Navegacion(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {
        composable("inicio") { InicioScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("servicios") { ServicioScreen(navController) }
        composable("barberos") { BarberoScreen(navController) }
        composable("horarios/{barberoId}") { backStackEntry ->
            val barberoId = backStackEntry.arguments?.getString("barberoId")?.toLongOrNull()
            if (barberoId != null) {
                HorarioDisponibleScreen(barberoId = barberoId, navController = navController)
            }
        }
        composable("reserva/{barberoId}/{fecha}/{hora}") { backStackEntry ->
            val barberoId = backStackEntry.arguments?.getString("barberoId")?.toLongOrNull()
            val fecha = backStackEntry.arguments?.getString("fecha")
            val hora = backStackEntry.arguments?.getString("hora")
            if (barberoId != null && fecha != null && hora != null) {
                ReservaScreen(barberoId = barberoId, fecha = fecha, hora = hora, navController = navController)
            }
        }
    }
}



