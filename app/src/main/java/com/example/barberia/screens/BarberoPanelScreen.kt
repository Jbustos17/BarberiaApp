package com.example.barberia.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.barberia.model.HorarioDisponible
import com.example.barberia.viewmodel.ReservaViewModel
import com.example.barberia.model.Reserva
import com.example.barberia.viewmodel.BarberoViewModel
import com.example.barberia.viewmodel.HorarioDisponibleViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Brush
import coil.compose.AsyncImage
import com.example.barberia.viewmodel.GaleriaViewModel
import com.example.barberia.viewmodel.DashboardViewModel
import kotlinx.coroutines.launch


@Composable
fun BarberoPanelScreen(
    idBarbero: Long,
    navController: NavHostController,
    reservaViewModel: ReservaViewModel = viewModel(),
    barberoViewModel: BarberoViewModel = viewModel(),
    galeriaViewModel: GaleriaViewModel = viewModel(),
    dashboardViewModel: DashboardViewModel = viewModel()
) {
    val reservas by reservaViewModel.reservas.collectAsState()
    val barberos by barberoViewModel.barberos.collectAsState()
    val barbero = barberos.find { it.idBarbero == idBarbero }
    val horarioDisponibleViewModel: HorarioDisponibleViewModel = viewModel()
    val horarios by horarioDisponibleViewModel.horarios.collectAsState()
    val error by reservaViewModel.error.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    
    var selectedTab by remember { mutableStateOf(0) }

    LaunchedEffect(idBarbero) {
        barberoViewModel.obtenerBarberos()
        reservaViewModel.cargarReservasPorBarbero(idBarbero)
        horarioDisponibleViewModel.cargarTodosLosHorarios()
        galeriaViewModel.cargarGaleria(idBarbero)
        dashboardViewModel.cargarEstadisticasBarberos()
    }
    
    // Mostrar mensajes de error
    LaunchedEffect(error) {
        error?.let { errorMsg ->
            snackbarHostState.showSnackbar(
                message = errorMsg,
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrisClaro)
                .padding(paddingValues)
    ) {
        // Canvas decorativo de fondo
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .matchParentSize()
        ) {
            // Círculo azul grande arriba a la izquierda
            drawCircle(
                color = AzulBarberi.copy(alpha = 0.14f),
                radius = size.minDimension * 0.55f,
                center = Offset(x = size.width * -0.18f, y = size.height * -0.1f)
            )
            // Círculo azul pequeño abajo a la derecha
            drawCircle(
                color = AzulBarberi.copy(alpha = 0.10f),
                radius = size.minDimension * 0.28f,
                center = Offset(x = size.width * 1.13f, y = size.height * 1.1f)
            )
            // Onda azul suave
            val path = Path().apply {
                moveTo(0f, size.height * 0.34f)
                cubicTo(
                    size.width * 0.20f, size.height * 0.30f,
                    size.width * 0.80f, size.height * 0.38f,
                    size.width, size.height * 0.23f
                )
                lineTo(size.width, 0f)
                lineTo(0f, 0f)
                close()
            }
            drawPath(
                path = path,
                color = AzulBarberi.copy(alpha = 0.09f),
                style = Fill
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Fila con botón de volver y título
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { navController.navigate("inicio") },
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
                    text = "Panel de ${barbero?.nombre ?: "Barbero"}",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, fontSize = 28.sp),
                    color = AzulBarberi,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tabs para Reservas, Ganancias y Galería
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = AzulBarberi
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Reservas") },
                    icon = { Icon(Icons.Default.Schedule, null) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Mis Ganancias") },
                    icon = { Icon(Icons.Default.AttachMoney, null) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Mi Galería") },
                    icon = { Icon(Icons.Default.PhotoLibrary, null) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                0 -> {
                    // Tab de Reservas
            if (reservas.isEmpty()) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No tienes reservas asignadas.",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(reservas) { reserva ->
                                ReservaCard(
                                    reserva = reserva, 
                                    horarios = horarios,
                                    onEliminar = { reservaAEliminar ->
                                        reservaAEliminar.idReserva?.let { id ->
                                            reservaViewModel.eliminarReserva(
                                                id = id,
                                                idAdministrador = 1L,
                                                idBarbero = idBarbero
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
                1 -> {
                    // Tab de Mis Ganancias
                    MisGananciasTab(
                        idBarbero = idBarbero,
                        nombreBarbero = barbero?.nombre ?: "Barbero",
                        dashboardViewModel = dashboardViewModel,
                        snackbarHostState = snackbarHostState
                    )
                }
                2 -> {
                    // Tab de Galería
                    GestionGaleriaTab(
                        idBarbero = idBarbero,
                        galeriaViewModel = galeriaViewModel,
                        snackbarHostState = snackbarHostState
                    )
                    }
                }
            }
        }
    }
}

@Composable
fun ReservaCard(
    reserva: Reserva, 
    horarios: List<HorarioDisponible>,
    onEliminar: (Reserva) -> Unit
) {
    val horario = horarios.find { it.idHorario == reserva.horarioDisponible.idHorario }
    val textoHorario = if (horario != null) {
        "Fecha: ${horario.fecha} - Hora: ${horario.horaInicio} a ${horario.horaFin}"
    } else {
        "Horario no encontrado"
    }
    
    var showDeleteDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        // Canvas decorativo en la tarjeta
        Canvas(
            modifier = Modifier.matchParentSize()
        ) {
            drawRect(
                color = AzulBarberi.copy(alpha = 0.07f),
                topLeft = Offset(0f, size.height * 0.30f),
                size = androidx.compose.ui.geometry.Size(size.width, size.height * 0.40f)
            )
            drawCircle(
                color = AzulBarberi.copy(alpha = 0.13f),
                radius = size.minDimension * 0.16f,
                center = Offset(x = size.minDimension * 0.14f, y = size.height * 0.20f)
            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(22.dp),
            elevation = CardDefaults.cardElevation(12.dp),
            border = BorderStroke(2.dp, AzulBarberi.copy(alpha = 0.14f)),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.98f))
        ) {
            Column(
                Modifier.padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                // Usuario
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Cliente",
                        tint = AzulBarberi,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = reserva.nombreCliente,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 22.sp),
                        color = AzulBarberi
                    )
                }
                Spacer(Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Campaign,
                        contentDescription = "Servicio",
                        tint = AzulBarberi,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Servicio: ${reserva.servicio.idServicio}",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
                        color = Color.DarkGray
                    )
                }
                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Schedule,
                        contentDescription = "Fecha y hora",
                        tint = AzulBarberi,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = textoHorario,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 19.sp, fontWeight = FontWeight.Medium),
                        color = Color.Black
                    )
                }
                Spacer(Modifier.height(12.dp))
                Divider(color = AzulBarberi.copy(alpha = 0.13f), thickness = 1.dp)
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "Celular: ${reserva.celularCliente}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                    color = AzulBarberi.copy(alpha = 0.8f)
                )
                Text(
                    text = "Correo: ${reserva.correoCliente}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                    color = AzulBarberi.copy(alpha = 0.8f)
                )
                
                Spacer(Modifier.height(16.dp))
                
                // Botón de eliminar (solo si idReserva no es null)
                if (reserva.idReserva != null) {
                    Button(
                        onClick = { showDeleteDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Eliminar Reserva",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
    
    // Diálogo de confirmación
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = "Confirmar eliminación",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = AzulBarberi
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas eliminar la reserva de ${reserva.nombreCliente}?\n\nEsta acción no se puede deshacer.",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onEliminar(reserva)
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Eliminar", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancelar", color = AzulBarberi)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun GestionGaleriaTab(
    idBarbero: Long,
    galeriaViewModel: GaleriaViewModel,
    snackbarHostState: SnackbarHostState
) {
    val galeria by galeriaViewModel.galeria.collectAsState()
    val isLoading by galeriaViewModel.isLoading.collectAsState()
    val error by galeriaViewModel.error.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    
    var mostrarDialogoAgregar by remember { mutableStateOf(false) }
    
    // Mostrar errores
    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
            galeriaViewModel.limpiarError()
        }
    }
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Botón para agregar foto
        Button(
            onClick = { mostrarDialogoAgregar = true },
            colors = ButtonDefaults.buttonColors(containerColor = AzulBarberi),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Agregar foto",
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text("Agregar Foto a mi Galería")
        }
        
        Spacer(Modifier.height(16.dp))
        
        if (isLoading && galeria.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AzulBarberi)
            }
        } else if (galeria.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No tienes fotos en tu galería",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Agrega fotos de tus mejores cortes",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(galeria) { foto ->
                    FotoGaleriaCard(
                        foto = foto,
                        onEliminar = {
                            foto.id?.let { id ->
                                coroutineScope.launch {
                                    galeriaViewModel.eliminarFoto(id, idBarbero)
                                    snackbarHostState.showSnackbar("Foto eliminada")
                                }
                            }
                        }
                    )
                }
            }
        }
    }
    
    // Diálogo para agregar foto
    if (mostrarDialogoAgregar) {
        AgregarFotoDialog(
            onDismiss = { mostrarDialogoAgregar = false },
            onConfirm = { fotoUrl, descripcion ->
                coroutineScope.launch {
                    galeriaViewModel.subirFoto(idBarbero, fotoUrl, descripcion)
                    mostrarDialogoAgregar = false
                    snackbarHostState.showSnackbar("Foto agregada exitosamente")
                }
            }
        )
    }
}

@Composable
fun FotoGaleriaCard(
    foto: com.example.barberia.model.GaleriaCorte,
    onEliminar: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = foto.fotoUrl,
                contentDescription = foto.descripcion,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            
            // Botón de eliminar en la esquina superior derecha
            IconButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(36.dp)
                    .background(Color.Red.copy(alpha = 0.8f), shape = RoundedCornerShape(8.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            // Overlay con descripción si existe
            foto.descripcion?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                            )
                        )
                        .padding(8.dp)
                ) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        maxLines = 2
                    )
                }
            }
        }
    }
    
    // Diálogo de confirmación de eliminación
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar esta foto de tu galería?") },
            confirmButton = {
                Button(
                    onClick = {
                        onEliminar()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Eliminar", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar", color = AzulBarberi)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun AgregarFotoDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String?) -> Unit
) {
    var fotoUrl by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Agregar Foto",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = AzulBarberi
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = fotoUrl,
                    onValueChange = { fotoUrl = it },
                    label = { Text("URL de la foto") },
                    placeholder = { Text("https://ejemplo.com/foto.jpg") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción (opcional)") },
                    placeholder = { Text("Corte degradado...") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (fotoUrl.isNotBlank()) {
                        onConfirm(fotoUrl, descripcion.ifBlank { null })
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AzulBarberi),
                enabled = fotoUrl.isNotBlank()
            ) {
                Text("Agregar")
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

@Composable
fun MisGananciasTab(
    idBarbero: Long,
    nombreBarbero: String,
    dashboardViewModel: DashboardViewModel,
    snackbarHostState: SnackbarHostState
) {
    val estadisticasBarberos by dashboardViewModel.estadisticasBarberos.collectAsState()
    val isLoading by dashboardViewModel.isLoading.collectAsState()
    val error by dashboardViewModel.error.collectAsState()
    
    // Encontrar las estadísticas de este barbero
    val miEstadistica = estadisticasBarberos.find { it.idBarbero == idBarbero }
    
    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
            dashboardViewModel.limpiarError()
        }
    }

    if (isLoading && miEstadistica == null) {
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
            // Header con nombre del barbero
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AzulBarberi),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            nombreBarbero,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            "Panel de Ganancias",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            miEstadistica?.let { stats ->
                // Total de cortes
                item {
                    StatCardBarbero(
                        title = "Total de Cortes Realizados",
                        value = "${stats.totalCortes}",
                        icon = Icons.Default.Build,
                        color = Color(0xFF2196F3)
                    )
                }

                // Ingresos generados
                item {
                    StatCardBarbero(
                        title = "Ingresos Totales Generados",
                        value = formatearPrecio(stats.ingresosGenerados),
                        icon = Icons.Default.AttachMoney,
                        color = Color(0xFF4CAF50),
                        subtitle = "Total generado para la barbería"
                    )
                }

                // Mis ganancias (comisión del barbero)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DoradoBarberia),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AttachMoney,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(Modifier.width(16.dp))
                                Column {
                                    Text(
                                        "Mis Ganancias",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                    Text(
                                        "Tu comisión",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White.copy(alpha = 0.7f)
                                    )
                                }
                            }
                            
                            Spacer(Modifier.height(20.dp))
                            
                            Text(
                                formatearPrecio(stats.comisionBarbero),
                                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            
                            Spacer(Modifier.height(8.dp))
                            
                            // Calcular el porcentaje
                            val porcentaje = if (stats.ingresosGenerados > 0) {
                                (stats.comisionBarbero / stats.ingresosGenerados * 100).toInt()
                            } else 0
                            
                            Text(
                                "Tu porcentaje: ${porcentaje}%",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }

                // Comisión de la barbería (referencia)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "Comisión de la Barbería",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Gray
                                )
                                
                                val porcentajeAdmin = if (stats.ingresosGenerados > 0) {
                                    (stats.comisionAdmin / stats.ingresosGenerados * 100).toInt()
                                } else 0
                                
                                Text(
                                    "$porcentajeAdmin%",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.Gray
                                )
                            }
                            Text(
                                formatearPrecio(stats.comisionAdmin),
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = Color.Gray
                            )
                        }
                    }
                }

                // Promedio por corte
                item {
                    val promedioPorCorte = if (stats.totalCortes > 0) {
                        stats.comisionBarbero / stats.totalCortes
                    } else 0.0
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "Promedio por Corte",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = AzulBarberi
                                )
                                Text(
                                    "Tus ganancias promedio",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                            }
                            Text(
                                formatearPrecio(promedioPorCorte),
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = AzulBarberi
                            )
                        }
                    }
                }
            } ?: item {
                // Si no hay estadísticas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "No hay estadísticas disponibles",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Gray
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Realiza tu primer corte para ver tus ganancias",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatCardBarbero(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    subtitle: String? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    value,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                subtitle?.let {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        it,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

