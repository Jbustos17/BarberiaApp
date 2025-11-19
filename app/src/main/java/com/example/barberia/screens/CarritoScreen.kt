package com.example.barberia.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.ui.platform.LocalContext
import com.example.barberia.viewmodel.CarritoViewModel
import com.example.barberia.viewmodel.ServicioViewModel
import com.example.barberia.viewmodel.BarberoViewModel
import com.example.barberia.model.CarritoItem
import com.example.barberia.model.Servicio
import com.example.barberia.model.Barbero
import com.example.barberia.utils.SessionManager

@Composable
fun CarritoScreen(
    navController: NavHostController,
    carritoViewModel: CarritoViewModel,
    servicioViewModel: ServicioViewModel = viewModel(),
    barberoViewModel: BarberoViewModel = viewModel()
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val cliente = sessionManager.getCliente()
    
    val carritoItems by carritoViewModel.carritoItems.collectAsState()
    val servicios by servicioViewModel.servicios.collectAsState()
    val barberos by barberoViewModel.barberos.collectAsState()
    
    LaunchedEffect(Unit) {
        servicioViewModel.cargarServicios(idAdministrador = 1L)
        barberoViewModel.obtenerBarberos()
    }
    
    val total = carritoViewModel.getTotal()
    
    // Función para navegar a servicios
    val navegarAServicios: () -> Unit = {
        val idCliente = cliente?.id
        if (idCliente != null) {
            // Si hay items en el carrito, usar la modalidad de esos items
            if (carritoItems.isNotEmpty()) {
                // Determinar la modalidad basándose en los items del carrito
                // Si algún item es a domicilio, usar DOMICILIO, si no PRESENCIAL
                val modalidad = if (carritoItems.any { it.esADomicilio }) {
                    "DOMICILIO"
                } else {
                    "PRESENCIAL"
                }
                // Navegar directamente a la pantalla de servicios con la modalidad
                navController.navigate("servicio/$idCliente/$modalidad")
            } else {
                // Si el carrito está vacío, ir a seleccionar modalidad
                navController.navigate("modalidadServicio/$idCliente")
            }
        } else {
            // Si no hay cliente, navegar al inicio
            navController.navigate("inicio") {
                popUpTo("inicio") { inclusive = false }
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
            drawPath(
                path = Path().apply {
                    moveTo(0f, size.height * 0.85f)
                    cubicTo(
                        size.width * 0.25f, size.height * 0.95f,
                        size.width * 0.75f, size.height * 0.75f,
                        size.width, size.height * 0.9f
                    )
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                },
                brush = Brush.horizontalGradient(
                    colors = listOf(AzulClaroBarberia, AzulBarberi, DoradoBarberia, AmarilloBarberia)
                )
            )
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header con botón de volver
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
                    "Carrito de Compra",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = AzulBarberi
                )
            }
            
            if (carritoItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Tu carrito está vacío",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = navegarAServicios,
                            colors = ButtonDefaults.buttonColors(containerColor = AzulBarberi)
                        ) {
                            Text("Agregar Servicios")
                        }
                    }
                }
            } else {
                // Lista de items del carrito
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(carritoItems) { item ->
                        ItemCarritoCard(
                            item = item,
                            servicios = servicios,
                            barberos = barberos,
                            onEliminar = { carritoViewModel.eliminarItem(item) }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Botón para agregar más servicios
                OutlinedButton(
                    onClick = navegarAServicios,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = AzulBarberi
                    ),
                    border = BorderStroke(2.dp, AzulBarberi)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agregar Más Servicios")
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Card de resumen total
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AzulBarberi),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "Total a pagar",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                                Text(
                                    "${carritoItems.size} ${if(carritoItems.size == 1) "servicio" else "servicios"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                            Text(
                                formatearPrecio(total),
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF4CAF50) // Verde
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Botón de continuar al pago
                Button(
                    onClick = {
                        navController.navigate("pago")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AzulBarberi),
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
                        "Proceder al Pago",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun ItemCarritoCard(
    item: CarritoItem,
    servicios: List<Servicio>,
    barberos: List<Barbero>,
    onEliminar: () -> Unit
) {
    val servicio = servicios.find { it.id == item.servicio.id }
    val barbero = barberos.find { it.idBarbero == item.barberoId }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Servicio
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = null,
                            tint = AzulBarberi,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            servicio?.nombre ?: "N/A",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Barbero
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            barbero?.nombre ?: "Cualquier profesional",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Fecha y hora
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "${item.fecha} - ${item.hora}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Precio - mostrar desglose si es a domicilio
                    if (item.esADomicilio) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Servicio:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                                Text(
                                    formatearPrecio(servicio?.precio ?: 0.0),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Home,
                                        contentDescription = null,
                                        tint = Color(0xFF4CAF50),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "Domicilio:",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray
                                    )
                                }
                                Text(
                                    "+${formatearPrecio(item.precioAdicionalDomicilio)}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF4CAF50)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Total:",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color.Black
                                )
                                Text(
                                    formatearPrecio(item.calcularTotal()),
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFF4CAF50)
                                )
                            }
                        }
                    } else {
                        // Precio normal sin desglose
                        Text(
                            formatearPrecio(servicio?.precio ?: 0.0),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF4CAF50)
                        )
                    }
                }
                
                // Botón eliminar
                IconButton(onClick = onEliminar) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

