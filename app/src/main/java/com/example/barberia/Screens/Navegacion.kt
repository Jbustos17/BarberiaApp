package com.example.barberia.Screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType



@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "servicios") {

        // Pantalla 1: Servicios
        composable("servicios") {
            ServiciosScreen(navController)
        }

        // Pantalla 2: Barberos
        composable("barberos") {
            BarberosScreen(navController)
        }

        // Pantalla 3: Disponibilidad de Barbero
        composable(
            route = "disponibilidad/{nombreBarbero}",
            arguments = listOf(
                navArgument("nombreBarbero") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val nombreBarbero = backStackEntry.arguments?.getString("nombreBarbero") ?: ""
            DisponibilidadScreen(navController, nombreBarbero)
        }

        // Pantalla 4: Reserva
        composable(
            route = "reserva/{nombreBarbero}/{dia}/{hora}",
            arguments = listOf(
                navArgument("nombreBarbero") { type = NavType.StringType },
                navArgument("dia") { type = NavType.StringType },
                navArgument("hora") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nombreBarbero = backStackEntry.arguments?.getString("nombreBarbero") ?: ""
            val dia = backStackEntry.arguments?.getString("dia") ?: ""
            val hora = backStackEntry.arguments?.getString("hora") ?: ""
            ReservaScreen(navController, nombreBarbero, dia, hora)
        }
    }
}
