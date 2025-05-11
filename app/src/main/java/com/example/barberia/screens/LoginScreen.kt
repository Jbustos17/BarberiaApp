package com.example.barberia.screens


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun LoginScreen (navController: NavController) {
    var usuario by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    var showForm by remember { mutableStateOf(false) }
    var pressed by remember { mutableStateOf(false) }


    val azulBarberia = Color(0, 74, 147)
    val grisOscuro = Color(0xFF404040)

    // Fade-in del formulario al cargar
    LaunchedEffect(Unit) {
        showForm = true
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFC4D0D7),
                        Color(0xFFFFFFFF)
                    )
                )
            )
    ) {
        AnimatedVisibility(
            visible = showForm,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(24.dp))
                    .shadow(8.dp, RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.97f), RoundedCornerShape(24.dp))
                    .padding(horizontal = 32.dp, vertical = 40.dp)
                    .fillMaxWidth(0.88f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Acceso Administrador",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = azulBarberia,
                        fontSize = 26.sp
                    ),
                    modifier = Modifier
                        .padding(bottom = 24.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it },
                    label = { Text("Usuario") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                OutlinedTextField(
                    value = contraseña,
                    onValueChange = { contraseña = it },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(imageVector = icon, contentDescription = if (showPassword) "Ocultar contraseña" else "Mostrar contraseña")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )

                // Animación de escala al hacer click en el botón
                var pressed by remember { mutableStateOf(false) }
                val scale by animateFloatAsState(if (pressed) 0.97f else 1f, label = "")

                Button(
                    onClick = {
                        pressed = true
                        if (usuario == "admin" && contraseña == "1234") {
                            navController.navigate("adminPanel") {
                                popUpTo("login") { inclusive = true } // Opcional: evita volver atrás al login
                            }
                        } else {
                            error = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = azulBarberia),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .scale(scale)
                ) {
                    Text("Iniciar sesión", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                }

// Este LaunchedEffect va en el cuerpo del composable, no dentro del onClick
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
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Credenciales incorrectas",
                        color = Color(0xFFD32F2F),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
