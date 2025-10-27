package com.example.barberia.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import java.net.URLDecoder

@Composable
fun Navegacion(navController: NavHostController) {
    // ViewModel compartido a nivel de navegación
    val carritoViewModel: com.example.barberia.viewmodel.CarritoViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // Splash Screen - Verifica sesión al inicio
        composable("splash") { SplashScreen(navController) }
        
        // Pantalla de inicio
        composable("inicio") { InicioScreen(navController) }
        
        // Autenticación de administradores
        composable("login") { LoginScreen(navController) }
        composable("adminPanel") { AdminPanelScreen(navController) }
        
        // Autenticación de clientes
        composable("clienteLogin") { ClienteLoginScreen(navController) }
        composable("clienteRegistro") { ClienteRegistroScreen(navController) }
        composable("perfil") { PerfilScreen(navController) }
        
        // Pantallas principales (requieren autenticación de cliente)
        composable("servicios") { ServicioScreen(navController) }
        composable("barberos/{servicioId}") { backStackEntry ->
            val servicioId = backStackEntry.arguments?.getString("servicioId")?.toLongOrNull()
            if (servicioId != null) {
                BarberoScreen(navController, servicioId)
            }
        }


        composable("barberoLogin") { BarberoLoginScreen(navController) }


        composable("barberoPanel/{idBarbero}") { backStackEntry ->
            val idBarbero = backStackEntry.arguments?.getString("idBarbero")?.toLongOrNull()
            if (idBarbero != null) {
                BarberoPanelScreen(idBarbero = idBarbero, navController = navController)
            }
        }

        composable("horarios/{idBarbero}/{servicioId}") { backStackEntry ->
            val idBarbero = backStackEntry.arguments?.getString("idBarbero")?.toLongOrNull()
            val servicioId = backStackEntry.arguments?.getString("servicioId")?.toLongOrNull()
            if (idBarbero != null && servicioId != null) {
                HorarioDisponibleScreen(
                    idBarbero = idBarbero, 
                    servicioId = servicioId,
                    navController = navController,
                    carritoViewModel = carritoViewModel
                )
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
        
        // Carrito de compras
        composable("carrito") { 
            CarritoScreen(
                navController = navController,
                carritoViewModel = carritoViewModel
            )
        }
        
        // Pantalla de pago
        composable("pago") { 
            PagoScreen(
                navController = navController,
                carritoViewModel = carritoViewModel
            )
        }
    }
}
