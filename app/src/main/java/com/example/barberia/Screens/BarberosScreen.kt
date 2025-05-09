package com.example.barberia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.barberia.ViewModel.BarberoViewModel
import com.example.barberia.model.Barbero

@Composable
fun BarberoScreen(
    navController: NavHostController,
    viewModel: BarberoViewModel = viewModel()
) {
    // Observa la lista de barberos
    val barberos by viewModel.barberos.collectAsState()

    // Llama a obtenerBarberos una sola vez
    LaunchedEffect(Unit) {
        viewModel.obtenerBarberos()
    }

    // UI
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
                BarberoCard(barbero)
            }
        }
    }
}

@Composable
fun BarberoCard(barbero: Barbero) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
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
