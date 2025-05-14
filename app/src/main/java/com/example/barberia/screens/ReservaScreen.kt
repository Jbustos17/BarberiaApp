package com.example.barberia.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.barberia.model.*
import com.example.barberia.viewmodel.ClienteViewModel
import com.example.barberia.viewmodel.ReservaViewModel
import kotlinx.coroutines.launch

@Composable
fun ReservaScreen(
    idBarbero: Long,
    fecha: String,
    hora: String,
    servicioId: Long,
    horarioDisponibleId: Long,
    idAdministrador: Long,
    total: Double = 30000.0,
    navController: NavHostController,
    reservaViewModel: ReservaViewModel = viewModel(),
    clienteViewModel: ClienteViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var celular by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrisClaro)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val height = size.height
            val width = size.width


            drawPath(
                path = Path().apply {
                    moveTo(width, height)
                    lineTo(width, height * 0.75f)
                    cubicTo(
                        width * 0.85f, height * 0.93f,
                        width * 0.65f, height * 0.87f,
                        width * 0.55f, height
                    )
                    close()
                },
                brush = Brush.horizontalGradient(
                    colors = listOf(AzulBarberi, AzulClaroBarberia, DoradoBarberia, AmarilloBarberia)
                )
            )

            drawCircle(
                color = AmarilloBarberia.copy(alpha = 0.18f),
                center = Offset(width * 0.88f, height * 0.96f),
                radius = width * 0.08f
            )
            drawCircle(
                color = AzulClaroBarberia.copy(alpha = 0.12f),
                center = Offset(width * 0.68f, height * 0.98f),
                radius = width * 0.06f
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                IconButton(
                    onClick = { navController.navigate("horarios/$idBarbero") },
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
                "Reserva tu turno",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = AzulBarberi,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = AzulBarberi)
                        Spacer(Modifier.width(8.dp))
                        Text("Barbero: #$idBarbero", style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Event, contentDescription = null, tint = AzulBarberi)
                        Spacer(Modifier.width(8.dp))
                        Text("Fecha: $fecha", style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = AzulBarberi)
                        Spacer(Modifier.width(8.dp))
                        Text("Hora: $hora", style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AttachMoney, contentDescription = null, tint = AzulBarberi)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Total: $${"%,.0f".format(total)} COP",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Text(
                "Tus datos",
                style = MaterialTheme.typography.titleMedium,
                color = AzulBarberi,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = celular,
                onValueChange = { celular = it },
                label = { Text("Celular") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo electrónico") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (nombre.isBlank() || celular.isBlank() || correo.isBlank()) {
                        showError = true
                        errorMessage = "Por favor completa todos los campos."
                    } else {
                        showError = false
                        isSaving = true
                        scope.launch {
                            try {
                                val cliente = Cliente(
                                    id_cliente = null,
                                    nombre = nombre,
                                    celular = celular,
                                    correo = correo
                                )
                                val clienteGuardado = clienteViewModel.guardarCliente(cliente, idAdministrador)

                                clienteGuardado.id_cliente?.let { idCliente ->
                                    val reserva = Reserva(
                                        idReserva = null,
                                        servicio = ServicioIdOnly(servicioId),
                                        barbero = BarberoIdOnly(idBarbero),
                                        horarioDisponible = HorarioIdOnly(horarioDisponibleId),
                                        cliente = ClienteIdOnly(idCliente),
                                        nombreCliente = nombre,
                                        celularCliente = celular,
                                        correoCliente = correo
                                    )
                                    reservaViewModel.guardarReserva(reserva, idAdministrador)
                                    showSuccess = true
                                    isSaving = false
                                    nombre = ""
                                    celular = ""
                                    correo = ""
                                } ?: run {
                                    showError = true
                                    isSaving = false
                                    errorMessage = "Error al obtener ID del cliente"
                                }
                            } catch (e: Exception) {
                                showError = true
                                isSaving = false
                                errorMessage = "Error al guardar la reserva: ${e.message}"
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !isSaving,
                colors = ButtonDefaults.buttonColors(containerColor = AzulBarberi)
            ) {
                Text(if (isSaving) "Guardando..." else "Confirmar Reserva", style = MaterialTheme.typography.titleMedium, color = Color.White)
            }

            if (showError) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (showSuccess) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "¡Reserva realizada con éxito!",
                    color = AzulBarberi,
                    style = MaterialTheme.typography.bodyMedium
                )
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(1500)
                    navController.navigate("inicio") {
                        popUpTo("inicio") { inclusive = true }
                    }
                }
            }
        }
    }
}
