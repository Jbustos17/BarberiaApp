package com.example.barberia.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import com.example.barberia.model.Reserva
import com.example.barberia.model.BarberoIdOnly
import com.example.barberia.model.ServicioIdOnly
import com.example.barberia.model.HorarioIdOnly
import com.example.barberia.model.ClienteIdOnly
import com.example.barberia.utils.SessionManager
import com.example.barberia.viewmodel.CarritoViewModel
import com.example.barberia.viewmodel.ReservaViewModel
import com.example.barberia.viewmodel.ServicioViewModel
import com.example.barberia.viewmodel.BarberoViewModel
import androidx.compose.ui.platform.LocalContext
import com.example.barberia.screens.formatearPrecio

@Composable
fun PagoScreen(
    navController: NavHostController,
    carritoViewModel: CarritoViewModel,
    reservaViewModel: ReservaViewModel = viewModel(),
    servicioViewModel: ServicioViewModel = viewModel(),
    barberoViewModel: BarberoViewModel = viewModel()
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val cliente = sessionManager.getCliente()
    
    val carritoItems by carritoViewModel.carritoItems.collectAsState()
    val servicios by servicioViewModel.servicios.collectAsState()
    val barberos by barberoViewModel.barberos.collectAsState()
    val total = carritoViewModel.getTotal()
    
    var metodoPago by remember { mutableStateOf("tarjeta") }
    var numeroTarjeta by remember { mutableStateOf("") }
    var nombreTitular by remember { mutableStateOf("") }
    var fechaVencimiento by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var procesandoPago by remember { mutableStateOf(false) }
    var pagoExitoso by remember { mutableStateOf(false) }
    var errorPago by remember { mutableStateOf<String?>(null) }
    var mensajeProcesamiento by remember { mutableStateOf("") }
    var pasoProcesamiento by remember { mutableStateOf(0) }
    
    val coroutineScope = rememberCoroutineScope()
    
    LaunchedEffect(Unit) {
        servicioViewModel.cargarServicios(idAdministrador = 1L)
        barberoViewModel.obtenerBarberos()
    }
    
    // Función para formatear número de tarjeta (agregar espacios cada 4 dígitos)
    fun formatearNumeroTarjeta(input: String): String {
        val digitsOnly = input.filter { it.isDigit() }
        return digitsOnly.chunked(4).joinToString(" ").take(19) // Máximo 16 dígitos + 3 espacios
    }
    
    // Función para formatear fecha MM/YY
    fun formatearFechaVencimiento(input: String): String {
        val digitsOnly = input.filter { it.isDigit() }
        return when {
            digitsOnly.length <= 2 -> digitsOnly
            else -> "${digitsOnly.take(2)}/${digitsOnly.drop(2).take(2)}"
        }
    }
    
    // Validar formulario de tarjeta
    fun validarFormularioTarjeta(): String? {
        val numeroLimpio = numeroTarjeta.replace(" ", "")
        if (numeroLimpio.length < 16) {
            return "El número de tarjeta debe tener 16 dígitos"
        }
        if (nombreTitular.trim().isEmpty()) {
            return "El nombre del titular es requerido"
        }
        if (fechaVencimiento.length < 5) {
            return "La fecha de vencimiento es requerida"
        }
        if (cvv.length < 3) {
            return "El código CVV es requerido"
        }
        return null
    }
    
    fun procesarPago() {
        if (carritoItems.isEmpty() || cliente == null) {
            errorPago = "Error: Información de sesión no disponible"
            return
        }
        
        // Validar formulario si es pago con tarjeta
        if (metodoPago == "tarjeta") {
            val errorValidacion = validarFormularioTarjeta()
            if (errorValidacion != null) {
                errorPago = errorValidacion
                return
            }
        }
        
        procesandoPago = true
        errorPago = null
        pasoProcesamiento = 0
        
        // Simular procesamiento de pago con pasos
        coroutineScope.launch {
            try {
                // Paso 1: Validando información
                pasoProcesamiento = 1
                mensajeProcesamiento = "Validando información de pago..."
                delay(1500)
                
                // Paso 2: Verificando tarjeta (solo si es tarjeta)
                if (metodoPago == "tarjeta") {
                    pasoProcesamiento = 2
                    mensajeProcesamiento = "Verificando tarjeta de crédito..."
                    delay(2000)
                    
                    // Paso 3: Procesando pago
                    pasoProcesamiento = 3
                    mensajeProcesamiento = "Procesando pago..."
                    delay(2000)
                    
                    // Paso 4: Confirmando transacción
                    pasoProcesamiento = 4
                    mensajeProcesamiento = "Confirmando transacción..."
                    delay(1500)
                } else {
                    pasoProcesamiento = 2
                    mensajeProcesamiento = "Procesando reserva..."
                    delay(1500)
                }
                
                // Crear reservas
                pasoProcesamiento = 5
                mensajeProcesamiento = "Creando reservas..."
                for (item in carritoItems) {
                    val reserva = Reserva(
                        idReserva = null,
                        servicio = ServicioIdOnly(item.servicio.id ?: 0L),
                        barbero = BarberoIdOnly(item.barberoId),
                        horarioDisponible = HorarioIdOnly(item.horarioDisponibleId),
                        cliente = ClienteIdOnly(cliente.id),
                        nombreCliente = cliente.nombre,
                        celularCliente = cliente.celular ?: "",
                        correoCliente = cliente.correo,
                        esADomicilio = item.esADomicilio
                    )
                    reservaViewModel.guardarReserva(reserva, 1L)
                }
                
                delay(1000)
                procesandoPago = false
                pagoExitoso = true
                delay(5000) // Mostrar confirmación por 5 segundos
                carritoViewModel.limpiarCarrito()
                navController.navigate("inicio") {
                    popUpTo("inicio") { inclusive = false }
                }
            } catch (e: Exception) {
                errorPago = "Error al procesar el pago: ${e.message}"
                procesandoPago = false
                pasoProcesamiento = 0
            }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrisClaro)
    ) {
        // Canvas decorativo
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = AzulBarberi.copy(alpha = 0.1f),
                radius = size.minDimension * 0.4f,
                center = Offset(x = size.width * 0.2f, y = size.height * 0.1f)
            )
            drawCircle(
                color = DoradoBarberia.copy(alpha = 0.1f),
                radius = size.minDimension * 0.3f,
                center = Offset(x = size.width * 0.8f, y = size.height * 0.9f)
            )
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = AzulBarberi
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Realizar Pago",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = AzulBarberi
                )
            }
            
            // Pantalla de procesamiento
            if (procesandoPago) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Indicador de carga animado
                        CircularProgressIndicator(
                            modifier = Modifier.size(80.dp),
                            color = AzulBarberi,
                            strokeWidth = 6.dp
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        // Mensaje de procesamiento
                        Text(
                            mensajeProcesamiento,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = AzulBarberi
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Indicador de pasos
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(horizontal = 32.dp)
                        ) {
                            repeat(5) { index ->
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (index < pasoProcesamiento) Color(0xFF4CAF50)
                                            else Color.LightGray
                                        )
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Mensaje adicional
                        Text(
                            "Por favor, no cierres la aplicación",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            } else if (pagoExitoso) {
                // Pantalla de confirmación exitosa
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Animación de check con círculo
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFF4CAF50),
                                            Color(0xFF45A049)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(100.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        // Título principal
                        Text(
                            "¡Pago Verificado!",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF4CAF50)
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Mensaje de confirmación
                        Text(
                            "Tu pago ha sido procesado exitosamente",
                            style = MaterialTheme.typography.titleMedium,
                            color = AzulBarberi,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            "Reserva confirmada",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        // Card con detalles
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            "Total pagado",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.Gray
                                        )
                                        Text(
                                            formatearPrecio(total),
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = AzulBarberi
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.Payment,
                                        contentDescription = null,
                                        tint = Color(0xFF4CAF50),
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                HorizontalDivider()
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            "Reservas creadas",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.Gray
                                        )
                                        Text(
                                            "${carritoItems.size} ${if(carritoItems.size == 1) "reserva" else "reservas"}",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = AzulBarberi
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color(0xFF4CAF50),
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        // Mensaje informativo
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = AzulBarberi.copy(alpha = 0.1f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = AzulBarberi,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    "Recibirás un correo de confirmación con los detalles de tu reserva",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AzulBarberi,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Indicador de redirección
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color.Gray,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Redirigiendo a inicio...",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            } else {
                // Resumen del monto
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AzulBarberi)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Total a pagar",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Text(
                            "${carritoItems.size} ${if(carritoItems.size == 1) "servicio" else "servicios"}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            formatearPrecio(total),
                            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF4CAF50) // Verde
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Métodos de pago
                Text(
                    "Método de pago",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = AzulBarberi
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetodoPagoCard(
                        titulo = "Tarjeta",
                        icono = Icons.Default.CreditCard,
                        seleccionado = metodoPago == "tarjeta",
                        onClick = { metodoPago = "tarjeta" },
                        modifier = Modifier.weight(1f)
                    )
                    MetodoPagoCard(
                        titulo = "Efectivo",
                        icono = Icons.Default.Money,
                        seleccionado = metodoPago == "efectivo",
                        onClick = { metodoPago = "efectivo" },
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Formulario de tarjeta (solo si se selecciona tarjeta)
                AnimatedVisibility(visible = metodoPago == "tarjeta") {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Tarjeta visual
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(AzulBarberi, Color(0xFF0066CC))
                                    )
                                )
                                .then(Modifier.padding(2.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(AzulBarberi, Color(0xFF0066CC))
                                        )
                                    )
                            ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CreditCard,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(40.dp)
                                    )
                                    Text(
                                        "VISA",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 4.sp
                                        ),
                                        color = Color.White
                                    )
                                }
                                
                                Column {
                                    Text(
                                        numeroTarjeta.ifEmpty { "•••• •••• •••• ••••" },
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 2.sp
                                        ),
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                "TITULAR",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.White.copy(alpha = 0.7f)
                                            )
                                            Text(
                                                nombreTitular.ifEmpty { "NOMBRE COMPLETO" },
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                color = Color.White
                                            )
                                        }
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                "VENCE",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.White.copy(alpha = 0.7f)
                                            )
                                            Text(
                                                fechaVencimiento.ifEmpty { "MM/AA" },
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                            }
                        }
                        
                        // Formulario de datos
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                            ) {
                                Text(
                                    "Información de la tarjeta",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = AzulBarberi
                                )
                            
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                OutlinedTextField(
                                    value = numeroTarjeta,
                                    onValueChange = { 
                                        val formatted = formatearNumeroTarjeta(it)
                                        numeroTarjeta = formatted
                                    },
                                    label = { Text("Número de tarjeta") },
                                    placeholder = { Text("1234 5678 9012 3456") },
                                    leadingIcon = {
                                        Icon(Icons.Default.CreditCard, contentDescription = null, tint = AzulBarberi)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    isError = numeroTarjeta.isNotEmpty() && numeroTarjeta.replace(" ", "").length < 16
                                )
                                
                                if (numeroTarjeta.isNotEmpty() && numeroTarjeta.replace(" ", "").length < 16) {
                                    Text(
                                        "El número de tarjeta debe tener 16 dígitos",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Red,
                                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                OutlinedTextField(
                                    value = nombreTitular,
                                    onValueChange = { nombreTitular = it.uppercase() },
                                    label = { Text("Nombre del titular") },
                                    placeholder = { Text("Como aparece en la tarjeta") },
                                    leadingIcon = {
                                        Icon(Icons.Default.Person, contentDescription = null, tint = AzulBarberi)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    isError = nombreTitular.isNotEmpty() && nombreTitular.trim().isEmpty()
                                )
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    OutlinedTextField(
                                        value = fechaVencimiento,
                                        onValueChange = { 
                                            val formatted = formatearFechaVencimiento(it)
                                            fechaVencimiento = formatted
                                        },
                                        label = { Text("Vencimiento") },
                                        placeholder = { Text("MM/AA") },
                                        leadingIcon = {
                                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = AzulBarberi, modifier = Modifier.size(20.dp))
                                        },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        isError = fechaVencimiento.isNotEmpty() && fechaVencimiento.length < 5
                                    )
                                    
                                    OutlinedTextField(
                                        value = cvv,
                                        onValueChange = { 
                                            if (it.all { char -> char.isDigit() } && it.length <= 3) {
                                                cvv = it
                                            }
                                        },
                                        label = { Text("CVV") },
                                        placeholder = { Text("123") },
                                        leadingIcon = {
                                            Icon(Icons.Default.Lock, contentDescription = null, tint = AzulBarberi, modifier = Modifier.size(20.dp))
                                        },
                                        visualTransformation = PasswordVisualTransformation(),
                                        modifier = Modifier.weight(1f),
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        isError = cvv.isNotEmpty() && cvv.length < 3
                                    )
                                }
                            }
                        }
                    }
                }
                
                // Mensaje para efectivo
                AnimatedVisibility(visible = metodoPago == "efectivo") {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = AmarilloBarberia.copy(alpha = 0.2f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = AzulBarberi,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Pagarás en efectivo al momento de recibir el servicio.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = AzulBarberi
                            )
                        }
                    }
                }
                
                if (errorPago != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                tint = Color.Red
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                errorPago ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Red
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Botón de confirmar pago
                Button(
                    onClick = { procesarPago() },
                    enabled = !procesandoPago && (metodoPago == "efectivo" || validarFormularioTarjeta() == null),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Payment,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (metodoPago == "tarjeta") "Pagar con Tarjeta" else "Confirmar Reserva",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Mensaje de seguridad
                if (metodoPago == "tarjeta") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Pago seguro y encriptado",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MetodoPagoCard(
    titulo: String,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (seleccionado) AzulBarberi else Color.White
        ),
        border = BorderStroke(
            width = 2.dp,
            color = if (seleccionado) AzulBarberi else Color.LightGray
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = if (seleccionado) Color.White else AzulBarberi,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                titulo,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = if (seleccionado) Color.White else AzulBarberi
            )
        }
    }
}

