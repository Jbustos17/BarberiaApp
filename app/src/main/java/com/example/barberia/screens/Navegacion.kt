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
        composable("recuperarContraseña") { RecuperarContraseñaScreen(navController) }
        composable("perfil") { PerfilScreen(navController) }
        composable("editarPerfil") { EditarPerfilScreen(navController) }
        
        // Selección de modalidad (presencial o domicilio)
        composable("modalidadServicio/{idCliente}") { backStackEntry ->
            val idCliente = backStackEntry.arguments?.getString("idCliente")?.toLongOrNull()
            if (idCliente != null) {
                ModalidadServicioScreen(idCliente = idCliente, navController = navController)
            }
        }
        
        // Pantallas principales (requieren autenticación de cliente)
        composable("servicio/{idCliente}/{modalidad}") { backStackEntry ->
            val idCliente = backStackEntry.arguments?.getString("idCliente")?.toLongOrNull()
            val modalidad = backStackEntry.arguments?.getString("modalidad") ?: "PRESENCIAL"
            if (idCliente != null) {
                ServicioScreen(navController, idCliente, modalidad)
            }
        }
        
        composable("barberos/{servicioId}/{modalidad}") { backStackEntry ->
            val servicioId = backStackEntry.arguments?.getString("servicioId")?.toLongOrNull()
            val modalidad = backStackEntry.arguments?.getString("modalidad") ?: "PRESENCIAL"
            if (servicioId != null) {
                BarberoScreen(navController, servicioId, modalidad)
            }
        }


        composable("barberoLogin") { BarberoLoginScreen(navController) }


        composable("barberoPanel/{idBarbero}") { backStackEntry ->
            val idBarbero = backStackEntry.arguments?.getString("idBarbero")?.toLongOrNull()
            if (idBarbero != null) {
                BarberoPanelScreen(idBarbero = idBarbero, navController = navController)
            }
        }

        composable("horarios/{idBarbero}/{servicioId}/{modalidad}") { backStackEntry ->
            val idBarbero = backStackEntry.arguments?.getString("idBarbero")?.toLongOrNull()
            val servicioId = backStackEntry.arguments?.getString("servicioId")?.toLongOrNull()
            val modalidad = backStackEntry.arguments?.getString("modalidad") ?: "PRESENCIAL"
            if (idBarbero != null && servicioId != null) {
                HorarioDisponibleScreen(
                    idBarbero = idBarbero, 
                    servicioId = servicioId,
                    modalidad = modalidad,
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
        
        // Galería de cortes del barbero
        composable("galeria/{idBarbero}") { backStackEntry ->
            val idBarbero = backStackEntry.arguments?.getString("idBarbero")?.toLongOrNull()
            if (idBarbero != null) {
                GaleriaScreen(
                    navController = navController,
                    idBarbero = idBarbero
                )
            }
        }
        
        // Dashboard financiero
        composable("dashboard") {
            DashboardScreen(navController = navController)
        }
    }
}
