package com.example.barberia.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.barberia.viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.example.barberia.factory.AuthViewModelFactory

@Composable
fun EditarPerfilScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(LocalContext.current)
    )
) {
    val currentCliente by authViewModel.currentCliente.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()
    val recuperacionMessage by authViewModel.recuperacionMessage.collectAsState()
    val successMessage by authViewModel.successMessage.collectAsState()
    
    var nombre by remember { mutableStateOf(currentCliente?.nombre ?: "") }
    var celular by remember { mutableStateOf(currentCliente?.celular ?: "") }
    var direccion by remember { mutableStateOf(currentCliente?.direccion ?: "") }
    
    var mostrarCambiarContraseña by remember { mutableStateOf(false) }
    var contraseñaActual by remember { mutableStateOf("") }
    var nuevaContraseña by remember { mutableStateOf("") }
    var confirmarContraseña by remember { mutableStateOf("") }
    var showPasswordActual by remember { mutableStateOf(false) }
    var showPasswordNueva by remember { mutableStateOf(false) }
    var showPasswordConfirmar by remember { mutableStateOf(false) }
    
    // Actualizar campos cuando se carga el cliente
    LaunchedEffect(currentCliente) {
        currentCliente?.let {
            nombre = it.nombre
            celular = it.celular ?: ""
            direccion = it.direccion ?: ""
        }
    }
    
    // Navegar de vuelta si se actualiza exitosamente
    LaunchedEffect(successMessage) {
        if (successMessage != null) {
            kotlinx.coroutines.delay(1500) // Esperar 1.5 segundos para mostrar el mensaje
            navController.popBackStack()
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
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
                Text(
                    "Editar Perfil",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 24.sp
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Card del formulario
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        "Información Personal",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = AzulBarberi
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        leadingIcon = {
                            Icon(Icons.Filled.Person, null, tint = AzulBarberi)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = celular,
                        onValueChange = { celular = it },
                        label = { Text("Celular") },
                        leadingIcon = {
                            Icon(Icons.Filled.Phone, null, tint = AzulBarberi)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = currentCliente?.correo ?: "",
                        onValueChange = { },
                        label = { Text("Correo") },
                        leadingIcon = {
                            Icon(Icons.Filled.Email, null, tint = AzulBarberi)
                        },
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = direccion,
                        onValueChange = { direccion = it },
                        label = { Text("Dirección") },
                        leadingIcon = {
                            Icon(Icons.Filled.LocationOn, null, tint = AzulBarberi)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        maxLines = 3
                    )

                    // Botón para cambiar contraseña
                    OutlinedButton(
                        onClick = { mostrarCambiarContraseña = !mostrarCambiarContraseña },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, AzulBarberi)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = null,
                            tint = AzulBarberi,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (mostrarCambiarContraseña) "Ocultar cambio de contraseña" else "Cambiar Contraseña",
                            color = AzulBarberi,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Sección de cambio de contraseña
                    AnimatedVisibility(
                        visible = mostrarCambiarContraseña,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Column {
                            Divider(
                                modifier = Modifier.padding(vertical = 16.dp),
                                color = Color.Gray.copy(alpha = 0.3f)
                            )
                            
                            Text(
                                "Cambiar Contraseña",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = AzulBarberi
                                ),
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            OutlinedTextField(
                                value = contraseñaActual,
                                onValueChange = { contraseñaActual = it },
                                label = { Text("Contraseña Actual") },
                                leadingIcon = {
                                    Icon(Icons.Filled.Lock, null, tint = AzulBarberi)
                                },
                                visualTransformation = if (showPasswordActual) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { showPasswordActual = !showPasswordActual }) {
                                        Icon(
                                            if (showPasswordActual) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                            null,
                                            tint = AzulBarberi
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = nuevaContraseña,
                                onValueChange = { nuevaContraseña = it },
                                label = { Text("Nueva Contraseña") },
                                leadingIcon = {
                                    Icon(Icons.Filled.Lock, null, tint = AzulBarberi)
                                },
                                visualTransformation = if (showPasswordNueva) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { showPasswordNueva = !showPasswordNueva }) {
                                        Icon(
                                            if (showPasswordNueva) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                            null,
                                            tint = AzulBarberi
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = confirmarContraseña,
                                onValueChange = { confirmarContraseña = it },
                                label = { Text("Confirmar Nueva Contraseña") },
                                leadingIcon = {
                                    Icon(Icons.Filled.Lock, null, tint = AzulBarberi)
                                },
                                visualTransformation = if (showPasswordConfirmar) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { showPasswordConfirmar = !showPasswordConfirmar }) {
                                        Icon(
                                            if (showPasswordConfirmar) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                            null,
                                            tint = AzulBarberi
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                singleLine = true
                            )

                            Button(
                                onClick = {
                                    if (nuevaContraseña == confirmarContraseña) {
                                        if (nuevaContraseña.length >= 6) {
                                            authViewModel.cambiarContraseña(contraseñaActual, nuevaContraseña)
                                        } else {
                                            authViewModel.setErrorMessage("La contraseña debe tener al menos 6 caracteres")
                                        }
                                    } else {
                                        authViewModel.setErrorMessage("Las contraseñas no coinciden")
                                    }
                                },
                                enabled = !isLoading && contraseñaActual.isNotBlank() && nuevaContraseña.isNotBlank() && confirmarContraseña.isNotBlank(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AzulBarberi),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else {
                                    Text(
                                        "Cambiar Contraseña",
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    // Mensaje de éxito de cambio de contraseña
                    AnimatedVisibility(
                        visible = recuperacionMessage != null && mostrarCambiarContraseña,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        if (recuperacionMessage != null) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)),
                                border = BorderStroke(1.dp, Color(0xFF4CAF50))
                            ) {
                                Text(
                                    recuperacionMessage!!,
                                    color = Color(0xFF2E7D32),
                                    style = TextStyle(fontWeight = FontWeight.Medium),
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                    
                    // Mensaje de éxito de actualización de perfil
                    AnimatedVisibility(
                        visible = successMessage != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        if (successMessage != null) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)),
                                border = BorderStroke(1.dp, Color(0xFF4CAF50))
                            ) {
                                Text(
                                    successMessage!!,
                                    color = Color(0xFF2E7D32),
                                    style = TextStyle(fontWeight = FontWeight.Medium),
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }

                    // Botón de guardar cambios
                    Button(
                        onClick = {
                            authViewModel.actualizarPerfil(nombre.trim(), celular.trim(), direccion.trim())
                        },
                        enabled = !isLoading && nombre.isNotBlank() && celular.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AzulBarberi),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                "Guardar Cambios",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Mostrar mensaje de error
                    AnimatedVisibility(
                        visible = errorMessage != null,
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
                                    style = TextStyle(fontWeight = FontWeight.Bold),
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

