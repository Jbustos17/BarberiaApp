package com.example.barberia.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.barberia.R

val AzulBarberi = Color(0xFF004A93)
val AmarilloBarberia = Color(0xFFF3CF54)
val DoradoBarberia = Color(0xFFFABA2D)
val AzulClaroBarberia = Color(0xFF147EE0)
val AzulOscuroBarberia = Color(0xFF004A93)
val GrisClaro = Color(0xFFF3F4F8)

@Composable
fun InicioScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrisClaro)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawPath(
                path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(size.width * 0.2f, 0f)
                    cubicTo(
                        size.width * 0.05f, size.height * 0.18f,
                        size.width * 0.18f, size.height * 0.13f,
                        0f, size.height * 0.25f
                    )
                    close()
                },
                brush = Brush.linearGradient(
                    colors = listOf(AmarilloBarberia, DoradoBarberia)
                )
            )

            drawPath(
                path = Path().apply {
                    moveTo(0f, size.height)
                    lineTo(0f, size.height * 0.8f)
                    cubicTo(
                        size.width * 0.18f, size.height * 0.95f,
                        size.width * 0.13f, size.height * 0.82f,
                        size.width * 0.25f, size.height
                    )
                    close()
                },
                brush = Brush.linearGradient(
                    colors = listOf(AzulClaroBarberia, AzulOscuroBarberia)
                )
            )

            drawPath(
                path = Path().apply {
                    moveTo(size.width, 0f)
                    lineTo(size.width * 0.8f, 0f)
                    cubicTo(
                        size.width * 0.95f, size.height * 0.18f,
                        size.width * 0.82f, size.height * 0.13f,
                        size.width, size.height * 0.25f
                    )
                    close()
                },
                brush = Brush.linearGradient(
                    colors = listOf(AzulClaroBarberia,AzulOscuroBarberia )
                )
            )

            drawPath(
                path = Path().apply {
                    moveTo(size.width, size.height)
                    lineTo(size.width, size.height * 0.8f)
                    cubicTo(
                        size.width * 0.95f, size.height * 0.82f,
                        size.width * 0.82f, size.height * 0.95f,
                        size.width * 0.75f, size.height
                    )
                    close()
                },
                brush = Brush.linearGradient(
                    colors = listOf(AmarilloBarberia.copy(alpha = 0.7f), DoradoBarberia.copy(alpha = 0.7f))
                )
            )
        }


        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White.copy(alpha = 0.97f), RoundedCornerShape(24.dp))
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_barberia),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(320.dp)
                    .padding(bottom = 12.dp)
            )

            Text(
                "Bienvenido a Kalu Barberia",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = AzulBarberi,
                    fontSize = 32.sp
                ),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Text(
                "¡Reserva tu corte o administra tu negocio fácil y rápido!",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF2F2F2F),
                    fontSize = 18.sp
                ),
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Button(
                onClick = { navController.navigate("login") },
                colors = ButtonDefaults.buttonColors(containerColor = AmarilloBarberia),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Soy Administrador", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("servicios") },
                colors = ButtonDefaults.buttonColors(containerColor = AzulBarberi),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Soy Cliente", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("barberoLogin") },
                colors = ButtonDefaults.buttonColors(containerColor = AzulClaroBarberia),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Soy Barbero", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}
