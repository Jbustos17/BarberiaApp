package com.example.barberia.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DisponibilidadScreen(navController: NavController, nombreBarbero: String) {
    val diasDisponibles = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")
    val horasDisponiblesPorDia = mapOf(
        "Lunes" to listOf("9:00 AM", "10:00 AM", "11:00 AM"),
        "Martes" to listOf("1:00 PM", "2:00 PM", "3:00 PM"),
        "Miércoles" to listOf("10:00 AM", "12:00 PM", "4:00 PM"),
        "Jueves" to listOf("9:00 AM", "11:00 AM", "5:00 PM"),
        "Viernes" to listOf("8:00 AM", "12:00 PM", "2:00 PM"),
        "Sábado" to listOf("10:00 AM", "1:00 PM", "3:00 PM")
    )

    var diaSeleccionado by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
            .padding(16.dp)
    ) {
        Text(
            text = "Disponibilidad de $nombreBarbero",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(diasDisponibles) { dia ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { diaSeleccionado = dia },
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(text = dia, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        diaSeleccionado?.let { dia ->
            Text(
                text = "Horas disponibles para $dia:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            horasDisponiblesPorDia[dia]?.forEach { hora ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            navController.navigate("reserva/$nombreBarbero/$dia/$hora")
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(text = hora, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
