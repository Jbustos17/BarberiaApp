package com.example.barberia.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.barberia.viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.example.barberia.factory.AuthViewModelFactory

@Composable
fun RecuperarContraseñaScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(LocalContext.current)
    )
) {
    var email by remember { mutableStateOf("") }
    var showForm by remember { mutableStateOf(false) }

    val isLoading by authViewModel.isLoading.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()
    val recuperacionMessage by authViewModel.recuperacionMessage.collectAsState()

    LaunchedEffect(Unit) {
        showForm = true
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
                // Card del formulario
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
                                onClick = { navController.popBackStack() },
                                modifier = Modifier.size(46.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Volver",
                                    tint = AzulBarberi
                                )
                            }
                        }

                        // Icono de email
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = null,
                            tint = AzulBarberi,
                            modifier = Modifier
                                .size(64.dp)
                                .padding(bottom = 16.dp)
                        )

                        Text(
                            "Recuperar Contraseña",
                            style = TextStyle(
                                fontWeight = FontWeight.ExtraBold,
                                color = AzulBarberi,
                                fontSize = 28.sp,
                                letterSpacing = 1.sp
                            ),
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            "Ingresa tu correo electrónico y te enviaremos una nueva contraseña",
                            style = TextStyle(
                                color = Color.Gray,
                                fontSize = 15.sp
                            ),
                            modifier = Modifier
                                .padding(bottom = 32.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Correo electrónico", fontSize = 18.sp) },
                            singleLine = true,
                            textStyle = TextStyle(fontSize = 18.sp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Email,
                                    contentDescription = null,
                                    tint = AzulBarberi
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp)
                        )

                        Button(
                            onClick = {
                                if (email.isNotBlank()) {
                                    authViewModel.recuperarContraseña(email)
                                }
                            },
                            enabled = !isLoading && email.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = AzulBarberi),
                            shape = RoundedCornerShape(26.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Text(
                                    text = "Enviar nueva contraseña",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Mostrar mensaje de éxito
                        AnimatedVisibility(
                            visible = recuperacionMessage != null,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            if (recuperacionMessage != null) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)),
                                    border = BorderStroke(1.dp, Color(0xFF4CAF50))
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Text(
                                            recuperacionMessage!!,
                                            color = Color(0xFF2E7D32),
                                            style = TextStyle(
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 15.sp
                                            ),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        TextButton(
                                            onClick = {
                                                authViewModel.clearRecuperacionMessage()
                                                navController.popBackStack()
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                "Volver al login",
                                                color = Color(0xFF2E7D32),
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Mostrar mensaje de error
                        AnimatedVisibility(
                            visible = errorMessage != null && recuperacionMessage == null,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            if (errorMessage != null) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFD32F2F).copy(alpha = 0.1f)),
                                    border = BorderStroke(1.dp, Color(0xFFD32F2F))
                                ) {
                                    Text(
                                        errorMessage!!,
                                        color = Color(0xFFD32F2F),
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        ),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



