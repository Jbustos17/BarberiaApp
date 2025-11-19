package com.example.barberia.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
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
    var aceptaTerminos by remember { mutableStateOf(false) }
    var showTerminosDialog by remember { mutableStateOf(false) }

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
                // Obtener el ID del cliente autenticado
                val cliente = (authState as AuthViewModel.AuthState.Authenticated).cliente
                if (cliente != null) {
                    navController.navigate("modalidadServicio/${cliente.id}") {
                        popUpTo("clienteRegistro") { inclusive = true }
                    }
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
        // Acepta números colombianos: 10 dígitos (celular) o números internacionales
        val cleaned = phone.replace(Regex("[^0-9]"), "")
        return cleaned.length >= 10 && cleaned.length <= 15
    }

    fun validateForm(): String? {
        val error = when {
            nombre.isBlank() -> "El nombre es obligatorio"
            celular.isBlank() -> "El celular es obligatorio"
            !isValidPhone(celular) -> "Ingresa un número de celular válido (mínimo 10 dígitos)"
            email.isBlank() -> "El correo es obligatorio"
            !isValidEmail(email) -> "Ingresa un correo electrónico válido"
            direccion.isBlank() -> "La dirección es obligatoria"
            password.isBlank() -> "La contraseña es obligatoria"
            password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            confirmPassword != password -> "Las contraseñas no coinciden"
            !aceptaTerminos -> "Debes aceptar los términos y condiciones"
            else -> null
        }
        // Limpiar mensaje de error anterior si hay un nuevo error
        if (error != null) {
            authViewModel.setErrorMessage(error)
        }
        return error
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
                                .padding(bottom = 12.dp)
                        )

                        // Checkbox de términos y condiciones
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = aceptaTerminos,
                                onCheckedChange = { aceptaTerminos = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = AzulBarberi,
                                    uncheckedColor = Color.Gray
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Acepto los ",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "términos y condiciones",
                                fontSize = 14.sp,
                                color = AzulBarberi,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { showTerminosDialog = true }
                            )
                        }

                        Button(
                            onClick = {
                                // Limpiar error anterior
                                authViewModel.clearError()
                                
                                val validationError = validateForm()
                                if (validationError == null) {
                                    // Si la validación pasa, proceder con el registro
                                    authViewModel.registrarCliente(
                                        com.example.barberia.model.ClienteRegistro(
                                            nombre = nombre.trim(),
                                            celular = celular.trim(),
                                            correo = email.trim(),
                                            contraseña = password,
                                            direccion = direccion.trim()
                                        )
                                    )
                                }
                            },
                            enabled = !isLoading && aceptaTerminos,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AzulBarberi,
                                disabledContainerColor = Color.Gray.copy(alpha = 0.5f)
                            ),
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
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFFFFEBEE)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        errorMessage!!,
                                        color = Color(0xFFD32F2F),
                                        style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp),
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
        
        // Diálogo de términos y condiciones
        if (showTerminosDialog) {
            TerminosCondicionesDialog(
                onDismiss = { showTerminosDialog = false }
            )
        }
    }
}

@Composable
fun TerminosCondicionesDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Términos y Condiciones",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = AzulBarberi
                    )
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = Color.Gray
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "BarberApp Kalu Estilo",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = AzulBarberi
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Última actualización: 2025",
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color.Gray
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "Bienvenido a BarberApp, la aplicación oficial de Kalu Barbería Estilo, ubicada en Zipaquirá, Colombia.\n\nAl utilizar la app, aceptas los siguientes términos:",
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "1. Uso de la aplicación",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = AzulBarberi
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "La app permite:\n• Crear una cuenta\n• Reservar servicios de barbería\n• Realizar pagos digitales\n• Solicitar servicios a domicilio\n• Gestionar información personal y de reservas\n\nEl usuario debe utilizar la aplicación de manera responsable y con información verídica.",
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "2. Requisitos del usuario",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = AzulBarberi
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "El usuario es responsable de:\n• Mantener sus credenciales seguras\n• Verificar la disponibilidad de servicios\n• Usar la app con fines legítimos",
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "3. Disponibilidad del servicio",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = AzulBarberi
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Kalu Estilo puede suspender temporalmente la aplicación por mantenimiento, fallos técnicos o actualizaciones.",
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "4. Prohibiciones",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = AzulBarberi
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Está prohibido:\n• Intentar manipular la app o sus bases de datos\n• Usar la plataforma con fines fraudulentos\n• Copiar, distribuir o modificar la aplicación sin autorización",
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "5. Limitación de responsabilidad",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = AzulBarberi
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Kalu Estilo no responde por:\n• Fallos del dispositivo del usuario\n• Problemas de red o conectividad\n• Daños derivados del uso o imposibilidad de uso de la app",
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "6. Modificaciones",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = AzulBarberi
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Los términos podrán actualizarse. La app notificará los cambios relevantes.",
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                
                Text(
                    text = "🔐 Política de Privacidad",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = AzulBarberi
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Esta política explica cómo tratamos tus datos personales en cumplimiento de las leyes colombianas.",
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "1. Datos que recolectamos",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = AzulBarberi
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "La app puede solicitar:\n• Nombre, correo y teléfono\n• Ubicación (solo para servicios a domicilio)\n• Historial de reservas\n• Información necesaria para pagos (a través de una pasarela segura)",
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "2. Finalidad",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = AzulBarberi
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Tus datos se utilizan para:\n• Gestión de tu cuenta y reservas\n• Procesamiento de pagos\n• Enviar confirmaciones y notificaciones\n• Mejorar el servicio y la experiencia del usuario",
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "3. Almacenamiento y seguridad",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = AzulBarberi
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Utilizamos servidores y protocolos seguros. No vendemos ni compartimos tu información con terceros, excepto obligación legal.",
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "4. Ubicación",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = AzulBarberi
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "La app usa tu ubicación únicamente para servicios a domicilio. No se almacena de forma permanente sin tu autorización.",
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "5. Derechos del usuario",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = AzulBarberi
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Puedes:\n• Solicitar acceso a tus datos\n• Rectificarlos\n• Eliminarlos\n• Revocar autorización de uso\n\nPara ejercer estos derechos:\n📩 soporte@kaluestilo.com",
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "6. Eliminación de datos",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = AzulBarberi
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Si ya no deseas usar la app, puedes solicitar la eliminación de tu cuenta y tu información personal.",
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                
                Text(
                    text = "📞 Soporte",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = AzulBarberi
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Si tienes dudas o problemas con la app:\n\n📱 WhatsApp: 000 000 0000\n📧 Correo: soporte@kaluestilo.com\n\n🕘 Horario: lunes a sábado, 9:00 a.m. – 7:00 p.m.",
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = AzulBarberi),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Entendido", color = Color.White)
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.White
    )
}
