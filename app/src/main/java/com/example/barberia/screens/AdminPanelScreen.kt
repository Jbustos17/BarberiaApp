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
import com.example.barberia.viewmodel.ReservaViewModel
import com.example.barberia.model.Reserva

@Composable
fun AdminPanelScreen(
    navController: NavHostController,
    barberoViewModel: BarberoViewModel = viewModel(),
    servicioViewModel: ServicioViewModel = viewModel(),
    reservaViewModel: ReservaViewModel = viewModel(),
    idAdministrador: Long = 1L
) {
    val tabTitles = listOf("Barberos", "Servicios", "Reservas")
    var selectedTab by remember { mutableStateOf(0) }

    var showBarberoDialog by remember { mutableStateOf(false) }
    var barberoToEdit by remember { mutableStateOf<Barbero?>(null) }

    var showServicioDialog by remember { mutableStateOf(false) }
    var servicioToEdit by remember { mutableStateOf<Servicio?>(null) }

    val barberos by barberoViewModel.barberos.collectAsState()
    val servicios by servicioViewModel.servicios.collectAsState()
    val reservas by reservaViewModel.reservas.collectAsState()

    LaunchedEffect(Unit) {
        barberoViewModel.obtenerBarberos()
        servicioViewModel.cargarServicios(idAdministrador)
        reservaViewModel.cargarReservas()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (selectedTab == 0) {
                        barberoToEdit = null
                        showBarberoDialog = true
                    } else if (selectedTab == 1) {
                        servicioToEdit = null
                        showServicioDialog = true
                    }
                },
                containerColor = Color(0xFF004A93)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(innerPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
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
                    onEdit = {
                        barberoToEdit = it
                        showBarberoDialog = true
                    },
                    onDelete = { barberoViewModel.eliminarBarbero(it.idBarbero!!, idAdministrador) }
                )
                1 -> ServiciosTab(
                    servicios = servicios,
                    onEdit = {
                        servicioToEdit = it
                        showServicioDialog = true
                    },
                    onDelete = { servicioViewModel.eliminarServicio(it.id!!, idAdministrador) }
                )
                2 -> ReservasTab(
                    reservas = reservas,
                    barberos = barberos,
                    servicios = servicios
                )
            }
        }
    }
}



@Composable
fun ReservasTab(
    reservas: List<Reserva>,
    barberos: List<Barbero>,
    servicios: List<Servicio>
) {
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(reservas) { reserva ->
            ReservaCardAdmin(
                reserva = reserva,
                barberos = barberos,
                servicios = servicios
            )
        }
    }
}


@Composable
fun ReservaCardAdmin(
    reserva: Reserva,
    barberos: List<Barbero>,
    servicios: List<Servicio>
) {
    val nombreBarbero = barberos.find { it.idBarbero == reserva.barbero.idBarbero }?.nombre ?: "N/A"
    val nombreServicio = servicios.find { it.id == reserva.servicio.idServicio }?.nombre ?: "N/A"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
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
    }
}



@Composable
fun BarberosTab(
    barberos: List<Barbero>,
    onEdit: (Barbero) -> Unit,
    onDelete: (Barbero) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(barberos) { barbero ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(barbero.nombre ?: "Sin nombre", style = MaterialTheme.typography.bodyLarge)
                    Row {
                        IconButton(onClick = { onEdit(barbero) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = { onDelete(barbero) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                        }
                    }
                }
            }
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
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(servicios) { servicio ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(servicio.nombre ?: "Sin nombre", style = MaterialTheme.typography.bodyLarge)
                    Row {
                        IconButton(onClick = { onEdit(servicio) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = { onDelete(servicio) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                        }
                    }
                }
            }
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
            Image(
                painter = painterResource(id = R.drawable.ic_barbero),
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
