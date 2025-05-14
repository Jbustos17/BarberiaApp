package com.example.barberia.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.barberia.viewmodel.BarberoViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.foundation.Canvas
import com.example.barberia.R
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Fill

@Composable
fun BarberoLoginScreen(
    navController: NavHostController,
    barberoViewModel: BarberoViewModel = viewModel()
) {
    var usuario by remember { mutableStateOf("") }
    var contrasenia by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    var pressed by remember { mutableStateOf(false) }

    val barberos by barberoViewModel.barberos.collectAsState()

    LaunchedEffect(Unit) {
        barberoViewModel.obtenerBarberos()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrisClaro)
    ) {
        // Canvas decorativo más detallado
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .matchParentSize()
        ) {
            // Círculo azul grande arriba a la izquierda
            drawCircle(
                color = AzulBarberi.copy(alpha = 0.14f),
                radius = size.minDimension * 0.55f,
                center = Offset(x = size.width * -0.2f, y = size.height * -0.1f)
            )
            // Círculo azul pequeño abajo a la derecha
            drawCircle(
                color = AzulBarberi.copy(alpha = 0.10f),
                radius = size.minDimension * 0.28f,
                center = Offset(x = size.width * 1.15f, y = size.height * 1.1f)
            )
            // Círculo gris claro decorativo arriba a la derecha
            drawCircle(
                color = GrisClaro.copy(alpha = 0.30f),
                radius = size.minDimension * 0.18f,
                center = Offset(x = size.width * 1.15f, y = size.height * 0.10f)
            )
            // Onda azul suave
            val path = Path().apply {
                moveTo(0f, size.height * 0.27f)
                cubicTo(
                    size.width * 0.25f, size.height * 0.23f,
                    size.width * 0.75f, size.height * 0.34f,
                    size.width, size.height * 0.19f
                )
                lineTo(size.width, 0f)
                lineTo(0f, 0f)
                close()
            }
            drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        AzulBarberi.copy(alpha = 0.10f),
                        Color.Transparent
                    )
                ),
                style = Fill
            )
        }

        // Card del formulario con sombra y borde azul
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.92f)
                .widthIn(max = 400.dp),
            shape = RoundedCornerShape(38.dp),
            elevation = CardDefaults.cardElevation(16.dp),
            border = BorderStroke(2.dp, AzulBarberi.copy(alpha = 0.15f)),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.97f))
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 36.dp, vertical = 44.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    IconButton(
                        onClick = { navController.navigate("inicio") },
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = AzulBarberi
                        )
                    }
                }

                Text(
                    "Acceso Barbero",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = AzulBarberi,
                        fontSize = 32.sp,
                        letterSpacing = 1.sp
                    ),
                    modifier = Modifier
                        .padding(bottom = 28.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it },
                    label = { Text("Usuario", fontSize = 20.sp) },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = contrasenia,
                    onValueChange = { contrasenia = it },
                    label = { Text("Contraseña", fontSize = 20.sp) },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(imageVector = icon, contentDescription = null)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 26.dp)
                )

                val scale by animateFloatAsState(if (pressed) 0.97f else 1f, label = "")

                Button(
                    onClick = {
                        pressed = true
                        val barberoMatch = barberos.find {
                            it.usuario?.trim() == usuario.trim() && it.contrasenia?.trim() == contrasenia.trim()
                        }
                        if (barberoMatch != null) {
                            navController.navigate("barberoPanel/${barberoMatch.idBarbero}") {
                                popUpTo("barberoLogin") { inclusive = true }
                            }
                        } else {
                            error = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AzulBarberi),
                    shape = RoundedCornerShape(26.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .scale(scale),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp)
                ) {
                    Text(
                        "Iniciar sesión",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                }

                LaunchedEffect(pressed) {
                    if (pressed) {
                        kotlinx.coroutines.delay(120)
                        pressed = false
                    }
                }

                AnimatedVisibility(
                    visible = error,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        "Credenciales incorrectas",
                        color = Color(0xFFD32F2F),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
