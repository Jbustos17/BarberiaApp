package com.example.barberia.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
                            .clickable { navController.navigate("barberos") },
                        shape = RoundedCornerShape(22.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp)
                        ) {
                            Image(
                                painter = painterResource(id = servicio.iconoResId()),
                                contentDescription = servicio.nombre ?: "Servicio",
                                modifier = Modifier
                                    .size(96.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE0E0E0))
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
                                onClick = { navController.navigate("barberos") },
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
