package com.example.barberia.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.barberia.model.HorarioDisponible
import com.example.barberia.viewmodel.ReservaViewModel
import com.example.barberia.model.Reserva
import com.example.barberia.viewmodel.BarberoViewModel
<<<<<<< HEAD
import androidx.compose.ui.graphics.Path

=======
import com.example.barberia.viewmodel.HorarioDisponibleViewModel
import com.example.barberia.viewmodel.ServicioViewModel
>>>>>>> eadb3a93f48991feabb7fc055e9d937df4339d76

@Composable
fun BarberoPanelScreen(
    idBarbero: Long,
    navController: NavHostController,
    reservaViewModel: ReservaViewModel = viewModel(),
    barberoViewModel: BarberoViewModel = viewModel()
) {
    val reservas by reservaViewModel.reservas.collectAsState()
    val barberos by barberoViewModel.barberos.collectAsState()
    val barbero = barberos.find { it.idBarbero == idBarbero }
    val horarioDisponibleViewModel: HorarioDisponibleViewModel = viewModel()
    val horarios by horarioDisponibleViewModel.horarios.collectAsState()


    LaunchedEffect(idBarbero) {
        barberoViewModel.obtenerBarberos()
        reservaViewModel.cargarReservasPorBarbero(idBarbero)
        horarioDisponibleViewModel.cargarTodosLosHorarios()// <--- carga todos los horarios
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrisClaro)
    ) {
        // --- Canvas de fondo decorativo ---
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .matchParentSize()
        ) {
            // Círculo grande azul en la esquina superior izquierda
            drawCircle(
                color = AzulBarberi.copy(alpha = 0.18f),
                radius = size.minDimension * 0.45f,
                center = Offset(x = size.width * 0.1f, y = size.height * 0.05f)
            )
            // Círculo pequeño azul en la esquina inferior derecha
            drawCircle(
                color = AzulBarberi.copy(alpha = 0.12f),
                radius = size.minDimension * 0.20f,
                center = Offset(x = size.width * 0.95f, y = size.height * 0.95f)
            )
            // Línea curva decorativa
            drawPath(
                path = Path().apply {
                    moveTo(0f, size.height * 0.25f)
                    cubicTo(
                        size.width * 0.25f, size.height * 0.18f,
                        size.width * 0.75f, size.height * 0.32f,
                        size.width, size.height * 0.15f
                    )
                },
                color = AzulBarberi.copy(alpha = 0.10f),
                style = Stroke(width = 18f, cap = StrokeCap.Round)
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
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = AzulBarberi,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (!barbero?.especialidad.isNullOrBlank()) {
                Text(
                    text = barbero?.especialidad ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Lista de reservas
            if (reservas.isEmpty()) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No tienes reservas asignadas.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(reservas) { reserva ->
                        ReservaCard(reserva, horarios)
                    }
                }
            }
        }
    }
}


@Composable
fun ReservaCard(reserva: Reserva, horarios: List<HorarioDisponible>) {
    val horario = horarios.find { it.idHorario == reserva.horarioDisponible.idHorario }
    val textoHorario = if (horario != null) {
        "Fecha: ${horario.fecha} - Hora: ${horario.horaInicio} a ${horario.horaFin}"
    } else {
        "Horario no encontrado"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "Cliente: ${reserva.nombreCliente}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Servicio: ${reserva.servicio.idServicio}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = textoHorario, // <--- aquí el texto plano
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Celular: ${reserva.celularCliente}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Text(
                text = "Correo: ${reserva.correoCliente}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}


// Utilidad para mostrar la fecha y hora legible
fun obtenerFechaHora(reserva: Reserva): String {
    // Aquí deberías obtener la fecha y hora real desde el objeto HorarioDisponible si lo necesitas
    return "ID Horario: ${reserva.horarioDisponible.idHorario}"
}


