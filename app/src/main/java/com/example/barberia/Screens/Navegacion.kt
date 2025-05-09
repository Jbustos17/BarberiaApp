package com.example.barberia.Screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.barberia.screens.AdminPanelScreen
import com.example.barberia.ui.screens.BarberoScreen

@Composable
fun Navegacion(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "inicio") {
        composable("inicio") { InicioScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("adminPanel") { AdminPanelScreen(navController) }
        composable("servicios") { ServicioScreen(navController) }
        composable("barberos") { BarberoScreen(navController) }
        composable("reserva") { ReservaScreen(navController) }
    }
}


