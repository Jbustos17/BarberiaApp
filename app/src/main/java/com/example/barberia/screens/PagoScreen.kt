package com.example.barberia.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
    
    val coroutineScope = rememberCoroutineScope()
    
    LaunchedEffect(Unit) {
        servicioViewModel.cargarServicios(idAdministrador = 1L)
        barberoViewModel.obtenerBarberos()
    }
    
    fun procesarPago() {
        if (carritoItems.isEmpty() || cliente == null) {
            errorPago = "Error: Información de sesión no disponible"
            return
        }
        
        procesandoPago = true
        errorPago = null
        
        // Simular procesamiento de pago y crear múltiples reservas
        coroutineScope.launch {
            delay(2000) // Simular tiempo de procesamiento
            
            try {
                // Crear una reserva por cada item del carrito
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
                
                pagoExitoso = true
                delay(2000)
                carritoViewModel.limpiarCarrito()
                navController.navigate("inicio") {
                    popUpTo("inicio") { inclusive = false }
                }
            } catch (e: Exception) {
                errorPago = "Error al procesar el pago: ${e.message}"
                procesandoPago = false
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
            
            if (pagoExitoso) {
                // Animación de pago exitoso
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4CAF50)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(80.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            "¡Pago Exitoso!",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF4CAF50)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Tu reserva ha sido confirmada",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
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
                                "Datos de la tarjeta",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = AzulBarberi
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            OutlinedTextField(
                                value = numeroTarjeta,
                                onValueChange = { if (it.length <= 16) numeroTarjeta = it },
                                label = { Text("Número de tarjeta") },
                                placeholder = { Text("1234 5678 9012 3456") },
                                leadingIcon = {
                                    Icon(Icons.Default.CreditCard, contentDescription = null)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            OutlinedTextField(
                                value = nombreTitular,
                                onValueChange = { nombreTitular = it },
                                label = { Text("Nombre del titular") },
                                placeholder = { Text("Como aparece en la tarjeta") },
                                leadingIcon = {
                                    Icon(Icons.Default.Person, contentDescription = null)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedTextField(
                                    value = fechaVencimiento,
                                    onValueChange = { if (it.length <= 5) fechaVencimiento = it },
                                    label = { Text("Vencimiento") },
                                    placeholder = { Text("MM/AA") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                
                                OutlinedTextField(
                                    value = cvv,
                                    onValueChange = { if (it.length <= 3) cvv = it },
                                    label = { Text("CVV") },
                                    placeholder = { Text("123") },
                                    visualTransformation = PasswordVisualTransformation(),
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
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
                    enabled = !procesandoPago,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    if (procesandoPago) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Procesando...")
                    } else {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Confirmar Pago",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
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
        border = androidx.compose.foundation.BorderStroke(
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

