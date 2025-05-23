package com.example.barberia.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.barberia.viewmodel.AdministradorViewModel
import kotlinx.coroutines.delay


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
                            "Acceso Administrador",
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
                            value = contraseña,
                            onValueChange = { contraseña = it },
                            label = { Text("Contraseña", fontSize = 22.sp) },
                            singleLine = true,
                            textStyle = TextStyle(fontSize = 22.sp),
                            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                val icon = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                                IconButton(onClick = { showPassword = !showPassword }) {
                                    Icon(imageVector = icon, contentDescription = if (showPassword) "Ocultar contraseña" else "Mostrar contraseña")
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp)
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
                                .height(60.dp)
                                .scale(scale)
                        ) {
                            Text("Iniciar sesión", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
                            Spacer(modifier = Modifier.height(16.dp))
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
    }
}
