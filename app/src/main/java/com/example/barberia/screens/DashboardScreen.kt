package com.example.barberia.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.barberia.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavHostController,
    dashboardViewModel: DashboardViewModel = viewModel()
) {
    val estadisticas by dashboardViewModel.estadisticas.collectAsState()
    val isLoading by dashboardViewModel.isLoading.collectAsState()
    val error by dashboardViewModel.error.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        dashboardViewModel.cargarEstadisticas()
    }

    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
            dashboardViewModel.limpiarError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Dashboard,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Dashboard Financiero", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulBarberi,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(GrisClaro)
                .padding(paddingValues)
        ) {
            if (isLoading && estadisticas == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AzulBarberi)
                }
            } else if (estadisticas != null) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Métricas principales
                    item {
                        Text(
                            "Resumen General",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = AzulBarberi
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            MetricCard(
                                title = "Reservas",
                                value = estadisticas!!.totalReservas.toString(),
                                icon = Icons.Default.CalendarToday,
                                color = Color(0xFF2196F3),
                                modifier = Modifier.weight(1f)
                            )
                            MetricCard(
                                title = "Barberos",
                                value = estadisticas!!.totalBarberos.toString(),
                                icon = Icons.Default.Person,
                                color = Color(0xFF4CAF50),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            MetricCard(
                                title = "Clientes",
                                value = estadisticas!!.totalClientes.toString(),
                                icon = Icons.Default.People,
                                color = Color(0xFFFF9800),
                                modifier = Modifier.weight(1f)
                            )
                            MetricCard(
                                title = "Servicios",
                                value = estadisticas!!.totalServicios.toString(),
                                icon = Icons.Default.Build,
                                color = Color(0xFF9C27B0),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Ingresos y comisiones
                    item {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Finanzas",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = AzulBarberi
                        )
                    }

                    item {
                        IngresosCard(
                            ingresosTotales = estadisticas!!.ingresosTotales,
                            comisionAdmin = estadisticas!!.comisionAdministrador,
                            comisionBarberos = estadisticas!!.comisionBarberos,
                            porcentajeAdmin = estadisticas!!.porcentajeComisionAdmin,
                            porcentajeBarbero = estadisticas!!.porcentajeComisionBarbero
                        )
                    }

                    // Estadísticas por barbero
                    item {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Rendimiento por Barbero",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = AzulBarberi
                        )
                    }

                    items(estadisticas!!.estadisticasPorBarbero) { estadistica ->
                        BarberoEstadisticaCard(estadistica)
                    }

                    // Reservas por mes
                    if (estadisticas!!.reservasPorMes.isNotEmpty()) {
                        item {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Reservas Mensuales",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = AzulBarberi
                            )
                        }

                        items(estadisticas!!.reservasPorMes) { reservaMes ->
                            ReservaMesCard(reservaMes)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun IngresosCard(
    ingresosTotales: Double,
    comisionAdmin: Double,
    comisionBarberos: Double,
    porcentajeAdmin: Double,
    porcentajeBarbero: Double
) {
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
            // Ingresos totales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        "Ingresos Totales",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = Color.DarkGray
                    )
                }
                Text(
                    formatearPrecio(ingresosTotales),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF4CAF50)
                )
            }

            Spacer(Modifier.height(20.dp))
            HorizontalDivider(color = Color.LightGray)
            Spacer(Modifier.height(20.dp))

            // Comisión administrador
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
                        "${porcentajeAdmin}%",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
                Text(
                    formatearPrecio(comisionAdmin),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = DoradoBarberia
                )
            }

            Spacer(Modifier.height(16.dp))

            // Comisión barberos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Comisión Barberos",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Text(
                        "${porcentajeBarbero}%",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
                Text(
                    formatearPrecio(comisionBarberos),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = AzulBarberi
                )
            }
        }
    }
}

@Composable
fun BarberoEstadisticaCard(estadistica: com.example.barberia.model.EstadisticasBarbero) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = AzulBarberi,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            estadistica.nombreBarbero,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            "${estadistica.totalCortes} cortes",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
                Text(
                    formatearPrecio(estadistica.ingresosGenerados),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF4CAF50)
                )
            }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Su comisión", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    Text(
                        formatearPrecio(estadistica.comisionBarbero),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = AzulBarberi
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Comisión admin", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    Text(
                        formatearPrecio(estadistica.comisionAdmin),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = DoradoBarberia
                    )
                }
            }
        }
    }
}

@Composable
fun ReservaMesCard(reservaMes: com.example.barberia.model.ReservaPorMes) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = AzulBarberi,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        reservaMes.mes,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        "${reservaMes.cantidad} reservas",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
            Text(
                formatearPrecio(reservaMes.ingresos),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF4CAF50)
            )
        }
    }
}

