package com.example.barberia.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.barberia.model.Barbero
import com.example.barberia.model.Servicio
import com.example.barberia.viewmodel.BarberoViewModel
import com.example.barberia.viewmodel.ServicioViewModel
import com.example.barberia.R
import com.example.barberia.model.BarberoIdOnly
import com.example.barberia.model.ClienteIdOnly
import com.example.barberia.model.HorarioIdOnly
import com.example.barberia.viewmodel.ReservaViewModel
import com.example.barberia.model.Reserva
import com.example.barberia.model.ServicioIdOnly
import kotlinx.coroutines.launch

@Composable
fun AdminPanelScreen(
    navController: NavHostController,
    barberoViewModel: BarberoViewModel = viewModel(),
    servicioViewModel: ServicioViewModel = viewModel(),
    idAdministrador: Long = 1L
) {
    val tabTitles = listOf("Barberos", "Servicios","Reservas")
    var selectedTab by remember { mutableStateOf(0) }

    var showBarberoDialog by remember { mutableStateOf(false) }
    var barberoToEdit by remember { mutableStateOf<Barbero?>(null) }

    var showServicioDialog by remember { mutableStateOf(false) }
    var servicioToEdit by remember { mutableStateOf<Servicio?>(null) }

    // Para el cuadro de confirmación de eliminación
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




    LaunchedEffect(Unit) {
        barberoViewModel.obtenerBarberos()
        servicioViewModel.cargarServicios(idAdministrador)
        reservaViewModel.cargarReservas()

    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            if (selectedTab != 2) { // Solo muestra el FAB si NO estás en la pestaña de Reservas
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
                onClick = { navController.navigate("inicio") }, // Cambia el nombre si tu ruta es diferente
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = AzulBarberi // Usa tu color principal o Color.Black si prefieres
                )
            }
            Text(
                "Panel de Administración",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF004A93),
                modifier = Modifier
                    .padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 8.dp)
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
                        text = { Text(title, fontWeight = FontWeight.Medium) }
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
                    onEdit = { reservaToEdit = it; showReservaDialog = true },
                    onDelete = { reservaToDelete = it }
                )
            }


        }
        }

        // Diálogo de agregar/editar barbero
        if (showBarberoDialog) {
            BarberoDialog(
                initialBarbero = barberoToEdit,
                onDismiss = { showBarberoDialog = false },
                onSave = { barbero ->
                    if (barberoToEdit == null) {
                        barberoViewModel.guardarBarbero(barbero, idAdministrador)
                    } else {
                        barberoViewModel.guardarBarbero(
                            barbero.copy(idBarbero = barberoToEdit!!.idBarbero),
                            idAdministrador
                        )
                    }
                    showBarberoDialog = false
                }
            )
        }

        // Diálogo de agregar/editar servicio
        if (showServicioDialog) {
            ServicioDialog(
                initialServicio = servicioToEdit,
                onDismiss = { showServicioDialog = false },
                onSave = { servicio ->
                    if (servicioToEdit == null) {
                        servicioViewModel.guardarServicio(servicio, idAdministrador)
                    } else {
                        servicioViewModel.guardarServicio(
                            servicio.copy(id = servicioToEdit!!.id),
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

// Diálogo de confirmación para eliminar servicio
    if (servicioToDelete != null) {
        AlertDialog(
            onDismissRequest = { servicioToDelete = null },
            title = { Text("Eliminar servicio") },
            text = { Text("¿Estás seguro de que quieres eliminar el servicio \"${servicioToDelete?.nombre}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    servicioToDelete?.let {
                        servicioViewModel.eliminarServicio(it.id!!, idAdministrador)
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
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val nombreBarbero = barberos.find { it.idBarbero == reserva.barbero.idBarbero }?.nombre ?: "N/A"
    val nombreServicio = servicios.find { it.id == reserva.servicio.idServicio }?.nombre ?: "N/A"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Cliente: ${reserva.nombreCliente}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Barbero: $nombreBarbero",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Servicio: $nombreServicio",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Horario ID: ${reserva.horarioDisponible.idHorario}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Celular: ${reserva.celularCliente}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Correo: ${reserva.correoCliente}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
            }
        }
    }
}
@Composable
fun ReservasTab(
    reservas: List<Reserva>,
    barberos: List<Barbero>,
    servicios: List<Servicio>,
    onEdit: (Reserva) -> Unit,
    onDelete: (Reserva) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(reservas) { reserva ->
            ReservaCardAdmin(
                reserva = reserva,
                barberos = barberos,
                servicios = servicios,
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
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            // Foto dinámica según el nombre
            Image(
                painter = painterResource(id = barbero.fotoResId()),
                contentDescription = "Foto barbero",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(barbero.nombre, fontWeight = FontWeight.Bold)
                Text(barbero.especialidad ?: "", color = Color.Gray)
                Text(barbero.telefono ?: "", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            // Puedes cambiar el icono si tienes uno específico por servicio
            Image(
                painter = painterResource(id = R.drawable.ic_barbero),
                contentDescription = "Icono servicio",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(servicio.nombre ?: "", fontWeight = FontWeight.Bold)
                Text(servicio.descripcion ?: "", color = Color.Gray)
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
            }
        }
    }
}

@Composable
fun BarberoDialog(
    initialBarbero: Barbero?,
    onDismiss: () -> Unit,
    onSave: (Barbero) -> Unit
) {
    var nombre by remember { mutableStateOf(initialBarbero?.nombre ?: "") }
    var telefono by remember { mutableStateOf(initialBarbero?.telefono ?: "") }
    var especialidad by remember { mutableStateOf(initialBarbero?.especialidad ?: "") }
    var usuario by remember { mutableStateOf(initialBarbero?.usuario ?: "") }
    var contrasenia by remember { mutableStateOf(initialBarbero?.contrasenia ?: "") }



    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialBarbero == null) "Agregar Barbero" else "Editar Barbero") },
        text = {
            Column {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") })
                OutlinedTextField(value = especialidad, onValueChange = { especialidad = it }, label = { Text("Especialidad") })
                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it },
                    label = { Text("Usuario") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                OutlinedTextField(
                    value = contrasenia,
                    onValueChange = { contrasenia = it },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

            }
        },
        confirmButton = {
            Button(onClick = {
                if (nombre.isNotBlank() && telefono.isNotBlank() && especialidad.isNotBlank() && usuario.isNotBlank() && contrasenia.isNotBlank()) {
                    onSave(
                        Barbero(
                            idBarbero = initialBarbero?.idBarbero,
                            nombre = nombre,
                            telefono = telefono,
                            especialidad = especialidad,
                            usuario = usuario,
                            contrasenia = contrasenia
                        )
                    )
                }
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun ServicioDialog(
    initialServicio: Servicio?,
    onDismiss: () -> Unit,
    onSave: (Servicio) -> Unit
) {
    var nombre by remember { mutableStateOf(initialServicio?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(initialServicio?.descripcion ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialServicio == null) "Agregar Servicio" else "Editar Servicio") },
        text = {
            Column {
                OutlinedTextField(value = nombre ?: "", onValueChange = { nombre = it }, label = { Text("Nombre") })
                OutlinedTextField(value = descripcion ?: "", onValueChange = { descripcion = it }, label = { Text("Descripción") })
            }
        },
        confirmButton = {
            Button(onClick = {
                if (!nombre.isNullOrBlank() && !descripcion.isNullOrBlank()) {
                    onSave(
                        Servicio(
                            id = initialServicio?.id,
                            nombre = nombre,
                            descripcion = descripcion
                        )
                    )
                }
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancelar") }
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
    var servicioSeleccionado by remember { mutableStateOf(initialReserva?.servicio?.idServicio ?: servicios.firstOrNull()?.id) }
    // Puedes agregar más campos según tu modelo

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
                // Selector de barbero
                DropdownMenuBarbero(
                    barberos = barberos,
                    seleccionado = barberoSeleccionado,
                    onSeleccionado = { barberoSeleccionado = it }
                )
                // Selector de servicio
                DropdownMenuServicio(
                    servicios = servicios,
                    seleccionado = servicioSeleccionado,
                    onSeleccionado = { servicioSeleccionado = it }
                )
                // Agrega aquí más campos si es necesario
            }
        },
        confirmButton = {
            TextButton(onClick = {
                // Crea el objeto Reserva y llama a onSave
                val reserva = Reserva(
                    idReserva = initialReserva?.idReserva,
                    nombreCliente = nombreCliente,
                    celularCliente = celularCliente,
                    correoCliente = correoCliente,
                    barbero = BarberoIdOnly(barberoSeleccionado!!),
                    servicio = ServicioIdOnly(servicioSeleccionado!!),
                    horarioDisponible = initialReserva?.horarioDisponible ?: HorarioIdOnly(0), // Ajusta esto según tu lógica
                    cliente = initialReserva?.cliente ?: ClienteIdOnly(0) // Ajusta esto según tu lógica
                )
                onSave(reserva)
            }) { Text("Guardar") }
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
            Text(nombreSeleccionado)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            barberos.forEach { barbero ->
                DropdownMenuItem(
                    text = { Text(barbero.nombre) },
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
    val nombreSeleccionado = servicios.find { it.id == seleccionado }?.nombre ?: "Selecciona Servicio"
    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(nombreSeleccionado ?: "")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            servicios.forEach { servicio ->
                DropdownMenuItem(
                    text = { Text(servicio.nombre ?: "") },
                    onClick = {
                        onSeleccionado(servicio.id!!)
                        expanded = false
                    }
                )
            }
        }
    }
}

