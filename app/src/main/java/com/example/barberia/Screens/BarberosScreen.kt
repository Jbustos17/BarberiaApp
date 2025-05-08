package com.example.barberia.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.barberia.ViewModel.ServicioViewModel

@Composable
fun ServicioScreen(viewModel: ServicioViewModel = viewModel(), navController: NavController) {
    val servicios by viewModel.servicios.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarServicios()
    }

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
                        Text(text = "Nombre: ${servicio.nombre}")
                        Text(text = "Descripción: ${servicio.descripcion}")
                    }
                }
            }
        }
    }
}
