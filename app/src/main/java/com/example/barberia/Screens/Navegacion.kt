package com.example.barberia.Screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType


@Composable
fun Navegacion(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "servicios") {
        composable("servicios") {
            ServicioScreen()
        }

        // Ejemplo de otra pantalla:
        // composable("barberos") {
        //     BarberoScreen()
        // }
    }
}
