package com.example.barberia.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.barberia.model.Barbero
import com.example.barberia.model.Servicio
import com.example.barberia.viewmodel.BarberoViewModel
import com.example.barberia.viewmodel.ServicioViewModel
import com.example.barberia.R
import com.example.barberia.model.HorarioDisponible
import com.example.barberia.viewmodel.ReservaViewModel
import com.example.barberia.model.Reserva
import com.example.barberia.viewmodel.HorarioDisponibleViewModel
import kotlinx.coroutines.launch
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.barberia.model.Cliente
import com.example.barberia.model.*
import kotlinx.coroutines.coroutineScope

fun transformarDriveUrl(url: String?): String? {
    if (url == null) return null
    val regex = Regex("https://drive\\.google\\.com/file/d/([a-zA-Z0-9_-]+)")
    val match = regex.find(url)
    return if (match != null) {
        val id = match.groupValues[1]
        "https://drive.google.com/uc?export=download&id=$id"
    } else {
        url
    }
}



@Composable
fun AdminPanelScreen(
    navController: NavHostController,
    barberoViewModel: BarberoViewModel = viewModel(),
    servicioViewModel: ServicioViewModel = viewModel(),
    idAdministrador: Long = 1L
) {
    val tabTitles = listOf("Barberos", "Servicios", "Reservas")
    var selectedTab by remember { mutableStateOf(0) }

    var showBarberoDialog by remember { mutableStateOf(false) }
    var barberoToEdit by remember { mutableStateOf<Barbero?>(null) }

    var showServicioDialog by remember { mutableStateOf(false) }
    var servicioToEdit by remember { mutableStateOf<Servicio?>(null) }

    var barberoToDelete by remember { mutableStateOf<Barbero?>(null) }
    var servicioToDelete by remember { mutableStateOf<Servicio?>(null) }

    val barberos by barberoViewModel.barberos.collectAsState()
    val servicios by servicioViewModel.servicios.collectAsState()

    val reservaViewModel: ReservaViewModel = viewModel()
    val reservas by reservaViewModel.reservas.collectAsState()

    var reservaToEdit by remember { mutableStateOf<Reserva?>(null) }
    var reservaToDelete by remember { mutableStateOf<Reserva?>(null) }
    var showReservaDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val horarioDisponibleViewModel: HorarioDisponibleViewModel = viewModel()
    val horarios by horarioDisponibleViewModel.horarios.collectAsState()



    println("IDs horarios: ${horarios.map { it.idHorario }}")


    LaunchedEffect(Unit) {
        barberoViewModel.obtenerBarberos()
        servicioViewModel.cargarServicios(idAdministrador)
        reservaViewModel.cargarReservas()
        horarioDisponibleViewModel.cargarTodosLosHorarios()

    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            if (selectedTab != 2) {
                FloatingActionButton(
                    onClick = {
                        if (selectedTab == 0) {
                            barberoToEdit = null
                            showBarberoDialog = true
                        } else {
                            servicioToEdit = null
                            showServicioDialog = true
                        }
                    },
                    containerColor = Color(0xFF004A93)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(innerPadding)
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
            Text(
                "Panel de Administracion",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold,fontSize = 34.sp),
                color = Color(0xFF004A93),
                modifier = Modifier
                    .padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 10.dp)
                    .align(Alignment.CenterHorizontally)
            )

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = Color(0xFF004A93)
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, fontWeight = FontWeight.Medium, fontSize = 24.sp)  }
                    )
                }
            }

            when (selectedTab) {
                0 -> BarberosTab(
                    barberos = barberos,
                    onEdit = { barberoToEdit = it; showBarberoDialog = true },
                    onDelete = { barberoToDelete = it }
                )
                1 -> ServiciosTab(
                    servicios = servicios,
                    onEdit = { servicioToEdit = it; showServicioDialog = true },
                    onDelete = { servicioToDelete = it }
                )
                2 -> ReservasTab(
                    reservas = reservas,
                    barberos = barberos,
                    servicios = servicios,
                    horarios = horarios,
                    onEdit = { reservaToEdit = it; showReservaDialog = true },
                    onDelete = { reservaToDelete = it }
                )
            }


        }
        }


    val idAdministrador = 1L // O el valor real

    if (showBarberoDialog) {
        BarberoDialog(
            initialBarbero = barberoToEdit,
            idAdministrador = idAdministrador,
            onDismiss = { showBarberoDialog = false },
            onSave = { barbero ->
                if (barberoToEdit == null) {
                    barberoViewModel.guardarBarbero(barbero, idAdministrador)
                } else {
                    barberoViewModel.guardarBarbero(
                        barbero.copy(
                            idBarbero = barberoToEdit!!.idBarbero,
                            idAdministrador = idAdministrador
                        ),
                        idAdministrador
                    )
                }
                showBarberoDialog = false
            }
        )
    }


        if (showServicioDialog) {
            ServicioDialog(
                initialServicio = servicioToEdit,
                onDismiss = { showServicioDialog = false },
                onSave = { servicio ->
                    if (servicioToEdit == null) {
                        servicioViewModel.guardarServicio(servicio, idAdministrador)
                    } else {
                        servicioViewModel.guardarServicio(
                            servicio.copy(idServicio = servicioToEdit!!.idServicio),
                            idAdministrador
                        )
                    }
                    showServicioDialog = false
                }
            )
        }

    if (barberoToDelete != null) {
        AlertDialog(
            onDismissRequest = { barberoToDelete = null },
            title = { Text("Eliminar barbero") },
            text = { Text("¿Estás seguro de que quieres eliminar a ${barberoToDelete?.nombre}?") },
            confirmButton = {
                TextButton(onClick = {
                    barberoToDelete?.let {
                        barberoViewModel.eliminarBarbero(it.idBarbero!!, idAdministrador)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Barbero eliminado correctamente")
                        }
                    }
                    barberoToDelete = null
                }) { Text("Eliminar", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { barberoToDelete = null }) { Text("Cancelar") }
            }
        )
    }


    if (servicioToDelete != null) {
        AlertDialog(
            onDismissRequest = { servicioToDelete = null },
            title = { Text("Eliminar servicio") },
            text = { Text("¿Estás seguro de que quieres eliminar el servicio \"${servicioToDelete?.nombre}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    servicioToDelete?.let {
                        servicioViewModel.eliminarServicio(it.idServicio!!, idAdministrador)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Servicio eliminado correctamente")
                        }
                    }
                    servicioToDelete = null
                }) { Text("Eliminar", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { servicioToDelete = null }) { Text("Cancelar") }
            }
        )
    }

    if (reservaToDelete != null) {
        AlertDialog(
            onDismissRequest = { reservaToDelete = null },
            title = { Text("Eliminar reserva") },
            text = { Text("¿Estás seguro de que quieres eliminar la reserva de ${reservaToDelete?.nombreCliente}?") },
            confirmButton = {
                TextButton(onClick = {
                    reservaToDelete?.let {
                        reservaViewModel.eliminarReserva(it.idReserva!!, idAdministrador)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Reserva eliminada correctamente")
                        }
                    }
                    reservaToDelete = null
                }) { Text("Eliminar", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { reservaToDelete = null }) { Text("Cancelar") }
            }
        )
    }

    if (showReservaDialog) {
        ReservaDialog(
            initialReserva = reservaToEdit,
            barberos = barberos,
            servicios = servicios,
            onDismiss = { showReservaDialog = false },
            onSave = { reserva ->
                if (reservaToEdit == null) {
                    reservaViewModel.guardarReserva(reserva, idAdministrador)
                } else {
                    reservaViewModel.guardarReserva(reserva.copy(idReserva = reservaToEdit!!.idReserva), idAdministrador)
                }
                showReservaDialog = false
            }
        )
    }
}




@Composable
fun ReservaCardAdmin(
    reserva: Reserva,
    barberos: List<Barbero>,
    servicios: List<Servicio>,
    horarios: List<HorarioDisponible>,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val nombreBarbero = barberos.find { it.idBarbero == reserva.barbero.idBarbero }?.nombre ?: "N/A"
    val nombreServicio = servicios.find { it.idServicio == reserva.servicio.idServicio }?.nombre ?: "N/A"
    val horario = horarios.find { it.idHorario == reserva.horarioDisponible.idHorario }
    val textoHorario = if (horario != null) {
        "Fecha: ${horario.fecha} - Hora: ${horario.horaInicio} a ${horario.horaFin}"
    } else {
        "Horario no encontrado"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(20.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
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
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            ),
                            color = AzulBarberi
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Barbero",
                            tint = AzulBarberi,
                            modifier = Modifier.size(36.dp)
                        )

                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Barbero: $nombreBarbero",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 19.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Color.DarkGray
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Build,
                            contentDescription = "Servicio",
                            tint = AzulBarberi,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Servicio: $nombreServicio",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 19.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
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
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color.Black
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Divider(color = AzulBarberi.copy(alpha = 0.13f), thickness = 1.dp)
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "Celular: ${reserva.celularCliente}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 17.sp),
                        color = AzulBarberi.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "Correo: ${reserva.correoCliente}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 17.sp),
                        color = AzulBarberi.copy(alpha = 0.8f)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReservasTab(
    reservas: List<Reserva>,
    barberos: List<Barbero>,
    servicios: List<Servicio>,
    horarios: List<HorarioDisponible>,
    onEdit: (Reserva) -> Unit,
    onDelete: (Reserva) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        items(reservas) { reserva ->
            ReservaCardAdmin(
                reserva = reserva,
                barberos = barberos,
                servicios = servicios,
                horarios = horarios,
                onEdit = { onEdit(reserva) },
                onDelete = { onDelete(reserva) }
            )
        }
    }
}

@Composable
fun BarberosTab(
    barberos: List<Barbero>,
    onEdit: (Barbero) -> Unit,
    onDelete: (Barbero) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        items(barberos) { barbero ->
            BarberoCardAdmin(
                barbero = barbero,
                onEdit = { onEdit(barbero) },
                onDelete = { onDelete(barbero) }
            )
        }
    }
}

@Composable
fun ServiciosTab(
    servicios: List<Servicio>,
    onEdit: (Servicio) -> Unit,
    onDelete: (Servicio) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        items(servicios) { servicio ->
            ServicioCardAdmin(
                servicio = servicio,
                onEdit = { onEdit(servicio) },
                onDelete = { onDelete(servicio) }
            )
        }
    }
}



@Composable
fun BarberoCardAdmin(
    barbero: Barbero,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {

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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(20.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(transformarDriveUrl(barbero.fotoUrl))
                        .crossfade(true)
                        .build(),
                    contentDescription = "Foto barbero",
                    placeholder = painterResource(R.drawable.ic_barbero_placeholder),
                    error = painterResource(R.drawable.ic_barbero_placeholder),
                    fallback = painterResource(R.drawable.ic_barbero_placeholder),
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                )


                Spacer(Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        barbero.nombre ?: "Sin nombre",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 26.sp),
                        color = AzulBarberi
                    )
                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Usuario: ${barbero.usuario}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 17.sp),
                        color = Color.Gray
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                    }
                }
            }
        }
    }
}


@Composable
fun ServicioCardAdmin(
    servicio: Servicio,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(20.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(transformarDriveUrl(servicio.fotoUrl))
                        .crossfade(true)
                        .build(),
                    contentDescription = "Imagen servicio",
                    placeholder = painterResource(R.drawable.ic_placeholder_servicio),
                    error = painterResource(R.drawable.ic_placeholder_servicio),
                    fallback = painterResource(R.drawable.ic_placeholder_servicio),
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                )


                Spacer(Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = servicio.nombre!!,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 26.sp),
                        color = AzulBarberi
                    )
                    Spacer(Modifier.height(8.dp))

                }
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                    }
                }
            }
        }
    }
}


@Composable
fun BarberoDialog(
    initialBarbero: Barbero? = null,
    idAdministrador: Long,
    onDismiss: () -> Unit,
    onSave: (Barbero) -> Unit
) {
    var nombre by remember { mutableStateOf(initialBarbero?.nombre ?: "") }
    var telefono by remember { mutableStateOf(initialBarbero?.telefono ?: "") }
    var usuario by remember { mutableStateOf(initialBarbero?.usuario ?: "") }
    var contrasenia by remember { mutableStateOf(initialBarbero?.contrasenia ?: "") }
    var fotoUrl by remember { mutableStateOf(initialBarbero?.fotoUrl ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialBarbero == null) "Agregar Barbero" else "Editar Barbero") },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it },
                    label = { Text("Usuario") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = contrasenia,
                    onValueChange = { contrasenia = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = fotoUrl,
                    onValueChange = { fotoUrl = it },
                    label = { Text("URL de la foto (Drive o web)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(transformarDriveUrl(fotoUrl))
                        .crossfade(true)
                        .build(),
                    contentDescription = "Foto barbero",
                    placeholder = painterResource(R.drawable.ic_barbero_placeholder),
                    error = painterResource(R.drawable.ic_barbero_placeholder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(
                        Barbero(
                            idBarbero = initialBarbero?.idBarbero,
                            nombre = nombre,
                            telefono = telefono,
                            usuario = usuario,
                            fotoUrl = fotoUrl,
                            contrasenia = contrasenia,
                            idAdministrador = idAdministrador
                        )
                    )
                }
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}


@Composable
fun ServicioDialog(
    initialServicio: Servicio? = null,
    onDismiss: () -> Unit,
    onSave: (Servicio) -> Unit
) {
    var nombre by remember { mutableStateOf(initialServicio?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(initialServicio?.descripcion ?: "") }
    var fotoUrl by remember { mutableStateOf(initialServicio?.fotoUrl ?: "") }
    // Nuevo: campo de precio como texto para validación
    var precioText by remember { mutableStateOf(initialServicio?.precio?.toString() ?: "") }
    var precioError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialServicio == null) "Agregar Servicio" else "Editar Servicio") },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del servicio") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = fotoUrl,
                    onValueChange = { fotoUrl = it },
                    label = { Text("URL de la imagen (Drive o web)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = precioText,
                    onValueChange = {

                        if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*\$"))) {
                            precioText = it
                            precioError = false
                        } else {
                            precioError = true
                        }
                    },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = precioError,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                if (precioError) {
                    Text("Solo se permiten números y punto decimal", color = Color.Red, fontSize = 12.sp)
                }
                Spacer(Modifier.height(8.dp))

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(transformarDriveUrl(fotoUrl))
                        .crossfade(true)
                        .build(),
                    contentDescription = "Imagen del servicio",
                    placeholder = painterResource(R.drawable.ic_placeholder_servicio),
                    error = painterResource(R.drawable.ic_placeholder_servicio),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {

                    val precioDouble = precioText.toDoubleOrNull()
                    if (precioDouble == null) {
                        precioError = true
                        return@TextButton
                    }
                    onSave(
                        Servicio(
                            idServicio = initialServicio?.idServicio,
                            nombre = nombre,
                            descripcion = descripcion,
                            fotoUrl = fotoUrl,
                            precio = precioDouble
                        )

                    )
                }
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun ReservaDialog(
    initialReserva: Reserva?,
    barberos: List<Barbero>,
    servicios: List<Servicio>,
    onDismiss: () -> Unit,
    onSave: (Reserva) -> Unit
) {
    var nombreCliente by remember { mutableStateOf(initialReserva?.nombreCliente ?: "") }
    var celularCliente by remember { mutableStateOf(initialReserva?.celularCliente ?: "") }
    var correoCliente by remember { mutableStateOf(initialReserva?.correoCliente ?: "") }
    var barberoSeleccionado by remember { mutableStateOf(initialReserva?.barbero?.idBarbero ?: barberos.firstOrNull()?.idBarbero) }
    var servicioSeleccionado by remember { mutableStateOf(initialReserva?.servicio?.idServicio ?: servicios.firstOrNull()?.idServicio) }
    var selectedServicioId by remember { mutableStateOf<Long?>(null) }
    var selectedBarberoId by remember { mutableStateOf<Long?>(null) }
    var selectedHorarioId by remember { mutableStateOf<Long?>(null) }
    var selectedClienteId by remember { mutableStateOf<Long?>(null) }

    var nombre by remember { mutableStateOf("") }
    var celular by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }



    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialReserva == null) "Crear Reserva" else "Editar Reserva") },
        text = {
            Column {
                OutlinedTextField(
                    value = nombreCliente,
                    onValueChange = { nombreCliente = it },
                    label = { Text("Nombre Cliente") }
                )
                OutlinedTextField(
                    value = celularCliente,
                    onValueChange = { celularCliente = it },
                    label = { Text("Celular Cliente") }
                )
                OutlinedTextField(
                    value = correoCliente,
                    onValueChange = { correoCliente = it },
                    label = { Text("Correo Cliente") }
                )

                DropdownMenuBarbero(
                    barberos = barberos,
                    seleccionado = barberoSeleccionado,
                    onSeleccionado = { barberoSeleccionado = it }
                )

                DropdownMenuServicio(
                    servicios = servicios,
                    seleccionado = servicioSeleccionado,
                    onSeleccionado = { servicioSeleccionado = it }
                )

            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (barberoSeleccionado != null && servicioSeleccionado != null
                    && nombreCliente.isNotBlank() && celularCliente.isNotBlank() && correoCliente.isNotBlank()
                ) {
                    val reserva = Reserva(
                        idReserva = initialReserva?.idReserva,
                        servicio = Servicio(idServicio = servicioSeleccionado!!),
                        barbero = Barbero(idBarbero = barberoSeleccionado!!),
                        horarioDisponible = initialReserva?.horarioDisponible ?: HorarioDisponible(idHorario = 0L),
                        cliente = initialReserva?.cliente ?: Cliente(idCliente = 0L),
                        nombreCliente = nombreCliente,
                        celularCliente = celularCliente,
                        correoCliente = correoCliente
                    )
                    onSave(reserva)
                } else {

                }
            }) {
                Text("Guardar reserva")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun DropdownMenuBarbero(
    barberos: List<Barbero>,
    seleccionado: Long?,
    onSeleccionado: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val nombreSeleccionado = barberos.find { it.idBarbero == seleccionado }?.nombre ?: "Selecciona Barbero"

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(
                nombreSeleccionado,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            barberos.forEach { barbero ->
                DropdownMenuItem(
                    text = {
                        Text(
                            barbero.nombre ?: "Sin nombre",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    onClick = {
                        onSeleccionado(barbero.idBarbero!!)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun DropdownMenuServicio(
    servicios: List<Servicio>,
    seleccionado: Long?,
    onSeleccionado: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val nombreSeleccionado = servicios.find { it.idServicio == seleccionado }?.nombre ?: "Selecciona Servicio"
    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(
                nombreSeleccionado,
                fontSize = 20.sp, // Letra más grande
                fontWeight = FontWeight.SemiBold
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            servicios.forEach { servicio ->
                DropdownMenuItem(
                    text = {
                        Text(
                            servicio.nombre!!,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    onClick = {
                        onSeleccionado(servicio.idServicio!!)
                        expanded = false
                    }
                )
            }
        }
    }
}

