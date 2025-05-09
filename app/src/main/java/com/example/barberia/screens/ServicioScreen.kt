package com.example.barberia.screens

import ServicioViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun ServicioScreen(
    navController: NavHostController,
    viewModel: ServicioViewModel = viewModel()
) {
    // Obtenemos la lista de servicios desde el ViewModel (asegurándose de usar StateFlow)
    val servicios by viewModel.servicios.collectAsState()

    // Llamamos a cargar los servicios, asegurándonos de que se pase un idAdministrador correcto
    LaunchedEffect(Unit) {
        viewModel.cargarServicios(idAdministrador = 1L)  // Pasa el id correcto del administrador
    }

    // Mostrar los servicios en la UI
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Servicios", style = MaterialTheme.typography.headlineSmall)

        LazyColumn {
            items(servicios) { servicio ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            navController.navigate("barberos")
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Nombre: ${servicio.nombre ?: "No disponible"}")
                        Text(text = "Descripción: ${servicio.descripcion ?: "No disponible"}")
                    }
                }
            }
        }
    }
}
