package com.example.barberia.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.animateFloatAsState
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
fun ClienteRegistroScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(LocalContext.current)
    )
) {
    var nombre by remember { mutableStateOf("") }
    var celular by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var showForm by remember { mutableStateOf(false) }

    val isLoading by authViewModel.isLoading.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(Unit) {
        showForm = true
    }

    // Navegar si el registro es exitoso
    LaunchedEffect(authState) {
        when (authState) {
            is AuthViewModel.AuthState.Authenticated -> {
                navController.navigate("servicios") {
                    popUpTo("clienteRegistro") { inclusive = true }
                }
            }
            else -> {}
        }
    }

    // Función de validación
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPhone(phone: String): Boolean {
        return phone.matches(Regex("^[+]?[0-9]{10,15}$"))
    }

    fun validateForm(): String? {
        return when {
            nombre.isBlank() -> "El nombre es obligatorio"
            celular.isBlank() -> "El celular es obligatorio"
            !isValidPhone(celular) -> "Ingresa un número de celular válido"
            email.isBlank() -> "El correo es obligatorio"
            !isValidEmail(email) -> "Ingresa un correo electrónico válido"
            direccion.isBlank() -> "La dirección es obligatoria"
            password.isBlank() -> "La contraseña es obligatoria"
            password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            confirmPassword != password -> "Las contraseñas no coinciden"
            else -> null
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
                        size.width * 0.5f, size.height * 0.08f,
                        size.width, 0f
                    )
                    lineTo(size.width, size.height * 0.10f)
                    quadraticBezierTo(
                        size.width * 0.7f, size.height * 0.15f,
                        0f, size.height * 0.18f
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
                        size.width * 0.4f, size.height * 0.95f,
                        size.width, size.height
                    )
                    lineTo(size.width, size.height * 0.90f)
                    quadraticBezierTo(
                        size.width * 0.7f, size.height * 0.85f,
                        0f, size.height * 0.82f
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
                        .fillMaxWidth(0.95f)
                        .widthIn(max = 450.dp)
                        .heightIn(max = 800.dp),
                    shape = RoundedCornerShape(30.dp),
                    elevation = CardDefaults.cardElevation(18.dp),
                    border = BorderStroke(2.dp, AzulBarberi.copy(alpha = 0.17f)),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.98f))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 24.dp)
                            .verticalScroll(rememberScrollState()),
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
                                onClick = { navController.navigate("clienteLogin") },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Volver",
                                    tint = AzulBarberi
                                )
                            }
                        }

                        Text(
                            "Crear Cuenta",
                            style = TextStyle(
                                fontWeight = FontWeight.ExtraBold,
                                color = AzulBarberi,
                                fontSize = 32.sp,
                                letterSpacing = 1.sp
                            ),
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            "Regístrate para acceder a nuestros servicios",
                            style = TextStyle(
                                color = Color.Gray,
                                fontSize = 16.sp
                            ),
                            modifier = Modifier
                                .padding(bottom = 24.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre completo", fontSize = 16.sp) },
                            singleLine = true,
                            textStyle = TextStyle(fontSize = 16.sp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        )

                        OutlinedTextField(
                            value = celular,
                            onValueChange = { celular = it },
                            label = { Text("Celular", fontSize = 16.sp) },
                            singleLine = true,
                            textStyle = TextStyle(fontSize = 16.sp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Correo electrónico", fontSize = 16.sp) },
                            singleLine = true,
                            textStyle = TextStyle(fontSize = 16.sp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        )

                        OutlinedTextField(
                            value = direccion,
                            onValueChange = { direccion = it },
                            label = { Text("Dirección", fontSize = 16.sp) },
                            singleLine = true,
                            textStyle = TextStyle(fontSize = 16.sp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Contraseña", fontSize = 16.sp) },
                            singleLine = true,
                            textStyle = TextStyle(fontSize = 16.sp),
                            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                val icon = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                                IconButton(onClick = { showPassword = !showPassword }) {
                                    Icon(imageVector = icon, contentDescription = if (showPassword) "Ocultar contraseña" else "Mostrar contraseña")
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        )

                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Confirmar contraseña", fontSize = 16.sp) },
                            singleLine = true,
                            textStyle = TextStyle(fontSize = 16.sp),
                            visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                val icon = if (showConfirmPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                                IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                                    Icon(imageVector = icon, contentDescription = if (showConfirmPassword) "Ocultar contraseña" else "Mostrar contraseña")
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp)
                        )

                        Button(
                            onClick = {
                                val validationError = validateForm()
                                if (validationError == null) {
                                    authViewModel.registrarCliente(
                                        com.example.barberia.model.ClienteRegistro(
                                            nombre = nombre,
                                            celular = celular,
                                            correo = email,
                                            contraseña = password,
                                            direccion = direccion
                                        )
                                    )
                                } else {
                                    authViewModel.setErrorMessage(validationError)
                                }
                            },
                            enabled = !isLoading,
                            colors = ButtonDefaults.buttonColors(containerColor = AzulBarberi),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else {
                                Text(
                                    text = "Crear cuenta", 
                                    fontSize = 18.sp, 
                                    fontWeight = FontWeight.Bold, 
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Botón para ir a login
                        TextButton(
                            onClick = { navController.navigate("clienteLogin") }
                        ) {
                            Text(
                                "¿Ya tienes cuenta? Inicia sesión aquí",
                                color = AzulBarberi,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Mostrar mensaje de error
                        AnimatedVisibility(
                            visible = errorMessage != null,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            if (errorMessage != null) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    errorMessage!!,
                                    color = Color(0xFFD32F2F),
                                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
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
}
