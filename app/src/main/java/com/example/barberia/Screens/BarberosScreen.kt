package com.example.barberia.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

data class Barbero(
    val nombre: String,
    val fotoUrl: String
)

@Composable
fun BarberosScreen(navController: NavController) {
    val barberos = listOf(
        Barbero("Juan", "Barbero1.jpg"),
        Barbero("Andrés", "Barbero2.jpg"),
        Barbero("Camilo", "Barbero3.jpg"),
        Barbero("Felipe", "Barbero4.jpg"),
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
            .padding(16.dp)
    ) {
        items(barberos) { barbero ->
            BarberoCard(barbero = barbero) {
                navController.navigate("disponibilidad/${barbero.nombre}")
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun BarberoCard(barbero: Barbero, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(barbero.fotoUrl),
                contentDescription = "Foto de ${barbero.nombre}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = barbero.nombre,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
