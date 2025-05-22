package com.example.barberia.screens


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.barberia.viewmodel.BarberoViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.delay

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
        // Canvas de fondo decorativo
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .matchParentSize()
        ) {
            // Círculo azul grande arriba a la izquierda
            drawCircle(
                color = AzulBarberi.copy(alpha = 0.16f),
                radius = size.minDimension * 0.55f,
                center = Offset(x = size.width * -0.18f, y = size.height * -0.1f)
            )
            // Círculo azul pequeño abajo a la derecha
            drawCircle(
                color = AzulBarberi.copy(alpha = 0.11f),
                radius = size.minDimension * 0.28f,
                center = Offset(x = size.width * 1.13f, y = size.height * 1.1f)
            )
            // Onda azul suave
            val path = Path().apply {
                moveTo(0f, size.height * 0.32f)
                cubicTo(
                    size.width * 0.20f, size.height * 0.28f,
                    size.width * 0.80f, size.height * 0.36f,
                    size.width, size.height * 0.22f
                )
                lineTo(size.width, 0f)
                lineTo(0f, 0f)
                close()
            }
            drawPath(
                path = path,
                color = AzulBarberi.copy(alpha = 0.10f),
                style = Fill
            )
        }

        // Card del formulario con sombra y borde azul
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.92f)
                .widthIn(max = 420.dp),
            shape = RoundedCornerShape(40.dp),
            elevation = CardDefaults.cardElevation(18.dp),
            border = BorderStroke(2.dp, AzulBarberi.copy(alpha = 0.17f)),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.98f))
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 38.dp, vertical = 48.dp),
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
                        modifier = Modifier.size(46.dp)
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
                    style = TextStyle(
                        fontWeight = FontWeight.ExtraBold,
                        color = AzulBarberi,
                        fontSize = 34.sp,
                        letterSpacing = 1.sp
                    ),
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it },
                    label = { Text("Usuario", fontSize = 22.sp) },
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 22.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = contrasenia,
                    onValueChange = { contrasenia = it },
                    label = { Text("Contraseña", fontSize = 22.sp) },
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 22.sp),
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(imageVector = icon, contentDescription = null)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 28.dp)
                )

                val scale by animateFloatAsState(if (pressed) 0.97f else 1f, label = "")

                Button(
                    onClick = {
                        pressed = true
                        val barberoMatch = barberos.find {
                            it.usuario?.trim() == usuario.trim() && it.contrasenia.trim() == contrasenia.trim()
                        }
                        if (barberoMatch != null) {
                            // Pasa idBarbero e idAdministrador correctamente
                            navController.navigate("barberoPanel/${barberoMatch.idBarbero}/${barberoMatch.idAdministrador}") {
                                popUpTo("barberoLogin") { inclusive = true }
                            }
                        } else {
                            error = true
                        }
                    }   ,
                    colors = ButtonDefaults.buttonColors(containerColor = AzulBarberi),
                    shape = RoundedCornerShape(26.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(62.dp)
                        .scale(scale),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 12.dp)
                ) {
                    Text(
                        "Iniciar sesión",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                }


                LaunchedEffect(pressed) {
                    if (pressed) {
                        delay(120)
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
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
