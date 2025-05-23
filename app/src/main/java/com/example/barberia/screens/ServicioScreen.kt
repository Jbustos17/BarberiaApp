package com.example.barberia.screens

import android.R.attr.shape
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.barberia.viewmodel.ServicioViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.barberia.R

fun getDriveDirectUrl(url: String?): String? {
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
fun ServicioScreen(
    navController: NavHostController,
    viewModel: ServicioViewModel = viewModel()
) {
    val servicios by viewModel.servicios.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarServicios(idAdministrador = 1L)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrisClaro)
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {

            drawPath(
                path = Path().apply {
                    moveTo(0f, size.height * 0.85f)
                    cubicTo(
                        size.width * 0.25f, size.height * 0.95f,
                        size.width * 0.75f, size.height * 0.75f,
                        size.width, size.height * 0.9f
                    )
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                },
                brush = Brush.horizontalGradient(
                    colors = listOf(AzulClaroBarberia, AzulBarberi, DoradoBarberia, AmarilloBarberia)
                )
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
                    onClick = { navController.navigate("inicio") },
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
                "Servicios",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = AzulBarberi,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(servicios) { servicio ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clickable {
                                servicio.idServicio?.let {
                                    navController.navigate("barberos/$it")
                                }
                            },
                        shape = RoundedCornerShape(22.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(getDriveDirectUrl(servicio.fotoUrl))
                                    .crossfade(true)
                                    .build(),
                                contentDescription = servicio.nombre ?: "Servicio",
                                placeholder = painterResource(com.example.barberia.R.drawable.ic_placeholder_servicio),
                                error = painterResource(com.example.barberia.R.drawable.ic_placeholder_servicio),
                                fallback = painterResource(R.drawable.ic_placeholder_servicio),
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE0E0E0))
                                    .align(Alignment.CenterVertically)
                            )


                            Spacer(modifier = Modifier.width(24.dp))
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    servicio.nombre ?: "No disponible",
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    maxLines = 2
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    servicio.descripcion ?: "No disponible",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.Gray,
                                    maxLines = 3
                                )
                            }
                            Button(
                                onClick = {
                                    servicio.idServicio?.let {
                                        navController.navigate("barberos/$it")}
                                          },
                                        colors = ButtonDefaults.buttonColors(containerColor = AzulBarberi),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .height(48.dp)
                            ) {
                                Text("Reservar", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
