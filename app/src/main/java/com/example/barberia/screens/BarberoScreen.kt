package com.example.barberia.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext

fun toDirectDriveUrl(url: String?): String? {
    if (url.isNullOrBlank()) return null
    val regex = Regex("https://drive\\.google\\.com/file/d/([a-zA-Z0-9_-]+)")
    val match = regex.find(url)
    return if (match != null) {
        val id = match.groupValues[1]
        "https://drive.google.com/uc?export=download&id=$id"
    } else {
        url
    }
}




@Composable
fun BarberoScreen(
    navController: NavHostController,
    viewModel: BarberoViewModel = viewModel()
) {
    val barberos by viewModel.barberos.collectAsState()

    LaunchedEffect(Unit) { viewModel.obtenerBarberos() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrisClaro)
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {

            drawCircle(
                color = DoradoBarberia.copy(alpha = 0.18f),
                center = Offset(size.width * 0.85f, size.height * 0.97f),
                radius = size.width * 0.11f
            )
            drawCircle(
                color = AzulClaroBarberia.copy(alpha = 0.13f),
                center = Offset(size.width * 0.18f, size.height * 0.92f),
                radius = size.width * 0.08f
            )
            // Línea diagonal abajo
            drawLine(
                color = AzulBarberi,
                start = Offset(0f, size.height),
                end = Offset(size.width * 0.8f, size.height * 0.82f),
                strokeWidth = 38f
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                IconButton(
                    onClick = { navController.navigate("servicios") },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = AzulBarberi // O el color que prefieras
                    )
                }
            }

            Text(
                text = "Selecciona a un profesional del equipo",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = AzulBarberi,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {

                item {
                    BarberoCardEspecial(
                        nombre = "Cualquier profesional",
                        descripcion = "Máxima disponibilidad",
                        iconRes = R.drawable.ic_random, // Usa tu icono de "aleatorio"
                        onClick = { navController.navigate("horarios/0") }
                    )
                }

                items(
                    barberos,
                    key = { it.idBarbero!! }
                ) { barbero ->
                    BarberoCardPersonalizado(
                        barbero = barbero,
                        onClick = { navController.navigate("horarios/${barbero.idBarbero}") }
                    )
                }
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
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(getDriveDirectUrl(barbero.fotoUrl))
                    .crossfade(true)
                    .build(),
                contentDescription = barbero.nombre,
                placeholder = painterResource(R.drawable.ic_barbero_placeholder),
                error = painterResource(R.drawable.ic_barbero_placeholder),
                fallback = painterResource(R.drawable.ic_barbero_placeholder),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )

            Spacer(Modifier.height(8.dp))
            Text(
                barbero.nombre ?: "Sin nombre",
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Text(
                text = barbero.telefono ?: "",
                color = Color.Gray,
                fontSize = 15.sp,
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
