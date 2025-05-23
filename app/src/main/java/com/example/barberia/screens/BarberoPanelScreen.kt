
@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.barberia.screens

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Person
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.rememberDatePickerState
import com.example.barberia.model.Servicio
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import com.example.barberia.viewmodel.ServicioViewModel
import com.example.barberia.screens.ReservacardTotalextrasbarbero
import java.util.Calendar


@Composable
fun BarberoPanelScreen(
    idBarbero: Long,
    idAdministrador: Long,
    navController: NavHostController,
    reservaViewModel: ReservaViewModel = viewModel(),
    barberoViewModel: BarberoViewModel = viewModel(),
    horarioDisponibleViewModel: HorarioDisponibleViewModel = viewModel(),
    servicioViewModel: ServicioViewModel = viewModel()
) {
    val reservas by reservaViewModel.reservas.collectAsState()
    val barberos by barberoViewModel.barberos.collectAsState()
    val servicios by servicioViewModel.servicios.collectAsState()
    val horarios by horarioDisponibleViewModel.horarios.collectAsState()

    var fechaSeleccionada by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    val barbero = barberos.find { it.idBarbero == idBarbero }

    // Recarga todos los datos necesarios
    LaunchedEffect(idBarbero, fechaSeleccionada) {
        servicioViewModel.cargarServicios(idAdministrador)
        horarioDisponibleViewModel.cargarTodosLosHorarios()
        reservaViewModel.cargarReservasPorBarberoYFecha(
            idBarbero,
            fechaSeleccionada.toString(),
            null
        )
    }

    // Mientras servicios u horarios están vacíos, muestra loading
    if (servicios.isEmpty() || horarios.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // Calcula el total de reservas CONFIRMADAS
    val totalDia = reservas
        .filter { it.estado == "CONFIRMADA" }
        .sumOf { reserva ->
            val servicio = servicios.find { it.idServicio == reserva.servicio.idServicio }
            servicio?.precio ?: 0.0
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrisClaro)
    ) {
        // Fondo decorativo (opcional)
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
                    colors = listOf(
                        AzulClaroBarberia,
                        AzulBarberi,
                        DoradoBarberia,
                        AmarilloBarberia
                    )
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
                modifier = Modifier.fillMaxWidth()
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
                Text(
                    text = "Panel de ${barbero?.nombre ?: "Barbero"}",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = AzulBarberi,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selector de fecha
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Fecha: ${fechaSeleccionada.format(DateTimeFormatter.ISO_DATE)}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = { showDatePicker = true },
                    colors = ButtonDefaults.buttonColors(containerColor = AzulBarberi)
                ) {
                    Text("Cambiar fecha")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Total del día
            Text(
                text = "Total confirmado: $${"%.2f".format(totalDia)}",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF388E3C) // Verde
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de reservas
            if (reservas.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        "No hay reservas para esta fecha",
                        style = MaterialTheme.typography.titleMedium.copy(color = Color.Gray)
                    )
                }
            } else {
                LazyColumn {
                    items(reservas) { reserva ->
                        ReservacardTotalextrasbarbero(
                            reserva = reserva,
                            servicios = servicios,
                            horarios = horarios,
                            onConfirmar = {
                                reservaViewModel.actualizarEstadoReserva(
                                    reserva.idReserva!!,
                                    "CONFIRMADA",
                                    idAdministrador,
                                    idBarbero,
                                    fechaSeleccionada.toString()
                                )
                            },
                            onCancelar = {
                                reservaViewModel.actualizarEstadoReserva(
                                    reserva.idReserva!!,
                                    "CANCELADA",
                                    idAdministrador,
                                    idBarbero,
                                    fechaSeleccionada.toString()
                                )
                            }
                        )
                    }
                }
            }

            // DatePicker
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = fechaSeleccionada
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            )

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        Button(
                            onClick = {
                                showDatePicker = false
                                datePickerState.selectedDateMillis?.let { millis ->
                                    fechaSeleccionada = Instant.ofEpochMilli(millis)
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                                }
                                // LaunchedEffect recarga automáticamente
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AzulBarberi)
                        ) { Text("OK") }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
        }
    }
}

@Composable
fun ReservacardTotalextrasbarbero(
    reserva: Reserva,
    servicios: List<Servicio>,
    horarios: List<HorarioDisponible>,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {

    val servicio = servicios.find { it.idServicio == reserva.servicio.idServicio }
    val precio = servicio?.precio ?: 0.0
    val nombreServicio = servicio?.nombre ?: "Servicio"

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
        // Canvas decorativo en la tarjeta
        Canvas(
            modifier = Modifier.matchParentSize()
        ) {
            drawRect(
                color = AzulBarberi.copy(alpha = 0.07f),
                topLeft = Offset(0f, size.height * 0.30f),
                size = androidx.compose.ui.geometry.Size(
                    size.width,
                    size.height * 0.40f
                )
            )
            drawCircle(
                color = AzulBarberi.copy(alpha = 0.13f),
                radius = size.minDimension * 0.16f,
                center = Offset(
                    x = size.minDimension * 0.14f,
                    y = size.height * 0.20f
                )
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
                // Cliente
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
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(Modifier.height(8.dp))

                // Servicio y precio
                Text(
                    text = "Servicio: $nombreServicio",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Precio: $${"%.2f".format(precio)}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(Modifier.height(8.dp))

                // Horario
                Text(
                    text = textoHorario,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(Modifier.height(8.dp))

                // Estado de la reserva
                Text(
                    text = "Estado: ${reserva.estado}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = when (reserva.estado) {
                        "CONFIRMADA" -> Color(0xFF388E3C)
                        "CANCELADA" -> Color.Red
                        else -> Color.Gray
                    }
                )

                // Botones solo si la reserva está pendiente
                if (reserva.estado == "PENDIENTE") {
                    Spacer(Modifier.height(10.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = onConfirmar,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(
                                    0xFF388E3C
                                )
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(40.dp)
                        ) {
                            Text(
                                "Confirmar",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Button(
                            onClick = onCancelar,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(40.dp)
                        ) {
                            Text(
                                "Cancelar",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

}


