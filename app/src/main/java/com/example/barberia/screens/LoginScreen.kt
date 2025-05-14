package com.example.barberia.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.barberia.viewmodel.AdministradorViewModel


@Composable
fun LoginScreen(
    navController: NavController,
    adminViewModel: AdministradorViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var usuario by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    var showForm by remember { mutableStateOf(false) }
    var pressed by remember { mutableStateOf(false) }

    val administradores by adminViewModel.administradores.collectAsState()

    LaunchedEffect(Unit) {
        showForm = true
        adminViewModel.cargarAdministradores()
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
                    quadraticBezierTo(
                        size.width * 0.5f, size.height * 0.10f,
                        size.width, 0f
                    )
                    lineTo(size.width, size.height * 0.13f)
                    quadraticBezierTo(
                        size.width * 0.7f, size.height * 0.18f,
                        0f, size.height * 0.20f
                    )
                    close()
                },
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        AmarilloBarberia,
                        DoradoBarberia,
                        AzulClaroBarberia,
                        AzulBarberi
                    )
                )
            )

            drawPath(
                path = Path().apply {
                    moveTo(0f, size.height)
                    quadraticBezierTo(
                        size.width * 0.4f, size.height * 0.92f,
                        size.width, size.height
                    )
                    lineTo(size.width, size.height * 0.87f)
                    quadraticBezierTo(
                        size.width * 0.7f, size.height * 0.82f,
                        0f, size.height * 0.80f
                    )
                    close()
                },
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        AzulClaroBarberia,
                        AzulBarberi,
                        DoradoBarberia,
                        AmarilloBarberia
                    )
                )
            )
        }


        AnimatedVisibility(
            visible = showForm,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {

                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(36.dp))
                        .background(Color.White.copy(alpha = 0.97f), RoundedCornerShape(36.dp))
                        .padding(horizontal = 32.dp, vertical = 40.dp)
                        .fillMaxWidth(0.92f)
                        .widthIn(max = 400.dp),
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
                            modifier = Modifier.size(70.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = AzulBarberi // O el color que prefieras
                            )
                        }
                    }

                    Text(
                        "Acceso Administrador",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = AzulBarberi,
                            fontSize = 28.sp
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

                    val scale by animateFloatAsState(if (pressed) 0.97f else 1f, label = "")

                    Button(
                        onClick = {
                            pressed = true
                            val adminMatch = administradores.find {
                                it.usuario == usuario && it.contrasenia == contraseña
                            }
                            if (adminMatch != null) {
                                navController.navigate("adminPanel") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                error = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AzulBarberi),
                        shape = RoundedCornerShape(26.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .scale(scale)
                    ) {
                        Text("Iniciar sesión", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
}
