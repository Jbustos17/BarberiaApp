package com.example.barberia.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.barberia.R


@Composable
fun InicioScreen(navController: NavHostController) {
    // Define los colores personalizados
        val azulBarberia = Color(red = 0, green = 74, blue = 147)
    val grisBarberia = Color(0xFFBBB9B9) // Gris claro, puedes ajustar el tono

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF232526), // Color superior del gradiente
                        Color(0xFF414345)  // Color inferior del gradiente
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
                .shadow(8.dp, RoundedCornerShape(24.dp))
                .background(Color.White.copy(alpha = 0.97f), RoundedCornerShape(24.dp))
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_barberia),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(300.dp)
                    .padding(bottom = 12.dp)
            )

            Text(
                "Bienvenido a Kalu Barberia",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = azulBarberia,
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
                    color = Color(0xFF404040), // Gris oscuro recomendado[3]
                    fontSize = 18.sp
                ),
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Button(
                onClick = { navController.navigate("login") },
                colors = ButtonDefaults.buttonColors(containerColor = grisBarberia),
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
                colors = ButtonDefaults.buttonColors(containerColor = azulBarberia),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Soy Cliente", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}
