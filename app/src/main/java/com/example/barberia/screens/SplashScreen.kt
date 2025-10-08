package com.example.barberia.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.barberia.R
import com.example.barberia.viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.example.barberia.factory.AuthViewModelFactory
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(LocalContext.current)
    )
) {
    var startAnimation by remember { mutableStateOf(false) }
    val authState by authViewModel.authState.collectAsState()

    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(2000),
        label = "alpha"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(3000) // Mostrar splash por 3 segundos
        
        // Verificar estado de autenticación
        when (authState) {
            is AuthViewModel.AuthState.Authenticated -> {
                navController.navigate("servicios") {
                    popUpTo("splash") { inclusive = true }
                }
            }
            is AuthViewModel.AuthState.Unauthenticated -> {
                navController.navigate("inicio") {
                    popUpTo("splash") { inclusive = true }
                }
            }
            else -> {
                // Verificar token si existe
                authViewModel.verificarToken()
            }
        }
    }

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
                    moveTo(size.width, size.height)
                    lineTo(size.width * 0.8f, size.height)
                    cubicTo(
                        size.width * 0.95f, size.height * 0.82f,
                        size.width * 0.82f, size.height * 0.87f,
                        size.width, size.height * 0.75f
                    )
                    close()
                },
                brush = Brush.linearGradient(
                    colors = listOf(AzulBarberi, AzulClaroBarberia)
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .alpha(alphaAnim.value)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo o imagen de la barbería
            Card(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(20.dp)),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    // Aquí puedes poner el logo de tu barbería
                    // Por ahora uso un icono genérico
                    Text(
                        text = "✂️",
                        fontSize = 80.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Barbería App",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = AzulBarberi
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tu barbería de confianza",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.Gray
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Indicador de carga
            CircularProgressIndicator(
                color = AzulBarberi,
                modifier = Modifier.size(40.dp),
                strokeWidth = 4.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Cargando...",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = AzulBarberi,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}
