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
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.outlined.Campaign
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.rememberDatePickerState



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BarberoPanelScreen(
    idBarbero: Long,
    navController: NavHostController,
    reservaViewModel: ReservaViewModel = viewModel(),
    barberoViewModel: BarberoViewModel = viewModel(),
    horarioDisponibleViewModel: HorarioDisponibleViewModel = viewModel()
) {

    val reservas by reservaViewModel.reservas.collectAsState()
    val barberos by barberoViewModel.barberos.collectAsState()
    val barbero = barberos.find { it.idBarbero == idBarbero }
    val horarios by horarioDisponibleViewModel.horarios.collectAsState()

    // Estado para la fecha seleccionada
    var fechaSeleccionada by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Recarga reservas filtradas por barbero y fecha
    LaunchedEffect(idBarbero, fechaSeleccionada) {
        barberoViewModel.obtenerBarberos()
        reservaViewModel.cargarReservasPorBarberoYFecha(
            idBarbero,
            fechaSeleccionada.toString(),
            null // o "TERMINADA" si solo quieres terminadas
        )
        horarioDisponibleViewModel.cargarTodosLosHorarios()
    }

    // Sumar el total de los cortes terminados ese día
    val totalDia = reservas
        .filter { it.estado == "TERMINADA" }
        .sumOf { it.servicio.precio ?: 0.0 }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrisClaro)
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
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    ),
                    color = AzulBarberi,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Fecha y botón para cambiarla
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Fecha: ${fechaSeleccionada.format(DateTimeFormatter.ISO_DATE)}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = { showDatePicker = true }) {
                    Text("Cambiar fecha")
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "Total del día: $${"%.2f".format(totalDia)}",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = AzulBarberi
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                            onTerminar = {
                                reservaViewModel.actualizarEstadoReserva(
                                    reserva.idReserva!!, "TERMINADA", idBarbero
                                )
                            },
                            onCancelar = {
                                reservaViewModel.actualizarEstadoReserva(
                                    reserva.idReserva!!, "CANCELADA", idBarbero
                                )
                            }
                        )
                    }
                }
            }
        }

    }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = fechaSeleccionada
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    )

    // DatePickerDialog (usa el de tu framework Compose preferido)
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            fechaSeleccionada = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        }
                        showDatePicker = false
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun ReservaCard(
    reserva: Reserva,
    horarios: List<HorarioDisponible>,
    onTerminar: (() -> Unit)? = null,
    onCancelar: (() -> Unit)? = null,

    ) {
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
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        ),
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
                        text = "Servicio: ${reserva.servicio.id}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 20.sp,
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
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Medium
                        ),
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
                Spacer(Modifier.height(10.dp))

                // Estado de la reserva
                Text(
                    text = "Estado: ${reserva.estado}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = when (reserva.estado) {
                        "TERMINADA" -> Color(0xFF388E3C)
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
                        onTerminar?.let {
                            Button(
                                onClick = it,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFF388E3C
                                    )
                                )
                            ) { Text("Terminar") }
                        }
                        Spacer(Modifier.width(12.dp))
                        onCancelar?.let {
                            Button(
                                onClick = it,
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) { Text("Cancelar") }
                        }
                    }
                }
            }
        }
    }
}