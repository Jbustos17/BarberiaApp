package com.example.barberia.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.barberia.model.Barbero
import com.example.barberia.viewmodel.BarberoViewModel
import com.example.barberia.R


@Composable
fun BarberoScreen(
    navController: NavHostController,
    viewModel: BarberoViewModel = viewModel()
) {
    val barberos by viewModel.barberos.collectAsState()

    LaunchedEffect(Unit) { viewModel.obtenerBarberos() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        Text(
            text = "Selecciona a un profesional del equipo",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Opción especial: Cualquier profesional
            item {
                BarberoCardEspecial(
                    nombre = "Cualquier profesional",
                    descripcion = "Máxima disponibilidad",
                    iconRes = R.drawable.ic_random, // Usa tu icono de "aleatorio"
                    onClick = { navController.navigate("horarios/0") }
                )
            }
            // Barberos reales (con clave única)
            items(
                barberos,
                key = { it.idBarbero }
            ) { barbero ->
                BarberoCardPersonalizado(
                    barbero = barbero,
                    onClick = { navController.navigate("horarios/${barbero.idBarbero}") }
                )
            }
        }
    }
}

@Composable
fun BarberoCardPersonalizado(
    barbero: Barbero,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Image(
                painter = painterResource(id = barbero.fotoResId()),
                contentDescription = barbero.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = barbero.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Text(
                text = barbero.especialidad ?: "",
                color = Color.Gray,
                fontSize = 15.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Text(
                text = barbero.telefono ?: "",
                color = Color.Gray,
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
fun BarberoCardEspecial(
    nombre: String,
    descripcion: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.height(16.dp))
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = nombre,
                tint = Color(0xFF004A93),
                modifier = Modifier.size(56.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = descripcion,
                color = Color.Gray,
                fontSize = 15.sp
            )
        }
    }
}
