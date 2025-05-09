package com.example.barberia.screens

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
import com.example.barberia.viewmodel.BarberoViewModel
import com.example.barberia.model.Barbero

@Composable
fun BarberoScreen(
    navController: NavHostController, // Asegúrate de que esta línea esté presente
    viewModel: BarberoViewModel = viewModel()
) {
    val barberos by viewModel.barberos.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.obtenerBarberos()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Text(
            text = "Nuestros Barberos",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(barberos) { barbero: Barbero ->
                BarberoCard(barbero, navController)  // Pasamos el navController aquí
            }
        }
    }
}


@Composable
fun BarberoCard(barbero: Barbero, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                // Navegar a la pantalla de horarios, pasando el ID del barbero
                navController.navigate("horarios/${barbero.id}")
            },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nombre: ${barbero.nombre}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Correo: ${barbero.correo}")
            Text(text = "Teléfono: ${barbero.telefono}")
            Text(text = "Especialidad: ${barbero.especialidad}")
        }
    }
}

