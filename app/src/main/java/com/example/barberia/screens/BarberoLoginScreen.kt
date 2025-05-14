package com.example.barberia.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.barberia.model.Barbero
import com.example.barberia.model.Servicio
import com.example.barberia.viewmodel.ServicioViewModel
import com.example.barberia.R

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
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = AzulBarberi // O el color que prefieras
                    )
                }
            }

            Text(
                "Acceso Barbero",
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
                value = contrasenia,
                onValueChange = { contrasenia = it },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(imageVector = icon, contentDescription = null)
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

                    // LOGS PARA DEPURAR
                    barberos.forEach {
                        Log.d("BarberoDebug", "Barbero cargado -> Usuario: ${it.usuario}, Contraseña: ${it.contrasenia}")
                    }
                    Log.d("BarberoDebug", "Ingresado -> Usuario: $usuario, Contraseña: $contrasenia")

                    val barberoMatch = barberos.find {
                        it.usuario?.trim() == usuario.trim() && it.contrasenia?.trim() == contrasenia.trim()
                    }
                    if (barberoMatch != null) {
                        Log.d("BarberoDebug", "Login exitoso para barbero ID: ${barberoMatch.idBarbero}")
                        navController.navigate("barberoPanel/${barberoMatch.idBarbero}") {
                            popUpTo("barberoLogin") { inclusive = true }
                        }
                    } else {
                        Log.d("BarberoDebug", "Credenciales incorrectas")
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