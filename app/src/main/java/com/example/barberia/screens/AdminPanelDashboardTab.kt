package com.example.barberia.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.barberia.viewmodel.DashboardViewModel
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun DashboardTab(
    dashboardViewModel: DashboardViewModel,
    navController: NavHostController
) {
    val estadisticas by dashboardViewModel.estadisticas.collectAsState()
    val comisiones by dashboardViewModel.comisiones.collectAsState()
    val isLoading by dashboardViewModel.isLoading.collectAsState()
    val error by dashboardViewModel.error.collectAsState()
    val mensaje by dashboardViewModel.mensaje.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    var showComisionDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        dashboardViewModel.cargarEstadisticas()
        dashboardViewModel.cargarComisiones()
    }

    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
            dashboardViewModel.limpiarError()
        }
    }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbarHostState.showSnackbar(it)
            dashboardViewModel.limpiarMensaje()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading && estadisticas == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AzulBarberi)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Botón para ir al dashboard completo
                item {
                    Button(
                        onClick = { navController.navigate("dashboard") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = AzulBarberi),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Dashboard, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Ver Dashboard Completo", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Configuración de comisiones
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
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
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = null,
                                        tint = AzulBarberi,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Text(
                                        "Configuración de Comisiones",
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                        color = AzulBarberi
                                    )
                                }
                                IconButton(onClick = { showComisionDialog = true }) {
                                    Icon(Icons.Default.Edit, "Editar", tint = AzulBarberi)
                                }
                            }

                            Spacer(Modifier.height(16.dp))
                            HorizontalDivider(color = Color.LightGray)
                            Spacer(Modifier.height(16.dp))

                            comisiones?.let { config ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            "Comisión Administrador",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.Gray
                                        )
                                        Text(
                                            "${config.comisionAdmin}%",
                                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                            color = DoradoBarberia
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            "Comisión Barbero",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.Gray
                                        )
                                        Text(
                                            "${config.comisionBarbero}%",
                                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                            color = AzulBarberi
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Resumen rápido de estadísticas
                estadisticas?.let { stats ->
                    item {
                        Text(
                            "Resumen Rápido",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = AzulBarberi
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            QuickStatCard(
                                title = "Reservas",
                                value = stats.totalReservas.toString(),
                                icon = Icons.Default.CalendarToday,
                                color = Color(0xFF2196F3),
                                modifier = Modifier.weight(1f)
                            )
                            QuickStatCard(
                                title = "Ingresos",
                                value = formatearPrecio(stats.ingresosTotales),
                                icon = Icons.Default.AttachMoney,
                                color = Color(0xFF4CAF50),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // Snackbar Host
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    // Diálogo para editar comisiones
    if (showComisionDialog) {
        ComisionDialog(
            comisionActual = comisiones,
            onDismiss = { showComisionDialog = false },
            onSave = { comisionAdmin, comisionBarbero ->
                dashboardViewModel.actualizarComisiones(comisionAdmin, comisionBarbero)
                showComisionDialog = false
            }
        )
    }
}

@Composable
fun QuickStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                    maxLines = 1
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun ComisionDialog(
    comisionActual: com.example.barberia.model.ComisionesConfig?,
    onDismiss: () -> Unit,
    onSave: (Double, Double) -> Unit
) {
    var comisionAdmin by remember { mutableStateOf(comisionActual?.comisionAdmin?.toString() ?: "40") }
    var comisionBarbero by remember { mutableStateOf(comisionActual?.comisionBarbero?.toString() ?: "60") }
    var errorTexto by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Configurar Comisiones",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = AzulBarberi
            )
        },
        text = {
            Column {
                Text(
                    "Las comisiones deben sumar 100%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = comisionAdmin,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                            comisionAdmin = it
                            errorTexto = null
                        }
                    },
                    label = { Text("Comisión Administrador (%)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    }
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = comisionBarbero,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                            comisionBarbero = it
                            errorTexto = null
                        }
                    },
                    label = { Text("Comisión Barbero (%)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    leadingIcon = {
                        Icon(Icons.Default.Build, contentDescription = null)
                    }
                )

                errorTexto?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        it,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Red
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val admin = comisionAdmin.toDoubleOrNull()
                    val barbero = comisionBarbero.toDoubleOrNull()

                    if (admin == null || barbero == null) {
                        errorTexto = "Por favor ingresa valores numéricos válidos"
                        return@Button
                    }

                    if (admin + barbero != 100.0) {
                        errorTexto = "Las comisiones deben sumar exactamente 100%"
                        return@Button
                    }

                    onSave(admin, barbero)
                },
                colors = ButtonDefaults.buttonColors(containerColor = AzulBarberi)
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar", color = AzulBarberi)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}

