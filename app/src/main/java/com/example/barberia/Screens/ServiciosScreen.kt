package com.example.barberia.Screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.items
import com.example.barberia.Components.ServicioCard


@Composable
fun ServiciosScreen(navController: NavController) {
    val servicios = listOf(
        Servicio("Transformación", "110 min", 70000, "Cambio de look completo."),
        Servicio("Corte", "55 min", 20000, "Corte tradicional o moderno."),
        Servicio("Corte y Barba", "80 min", 30000, "Corte de cabello más arreglo de barba."),
        Servicio("Servicio VIP", "100 min", 60000, "Servicio completo VIP.")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(servicios) { servicio ->
            ServicioCard(servicio = servicio) {
                navController.navigate("barberos")
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}


data class Servicio(
    val nombre: String,
    val duracion: String,
    val precio: Int,
    val descripcion: String
)
