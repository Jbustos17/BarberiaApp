package com.example.barberia.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.barberia.viewmodel.HorarioDisponibleViewModel
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import kotlinx.datetime.Clock
import kotlinx.datetime.toLocalDateTime
import java.net.URLEncoder

@Composable
fun HorarioDisponibleScreen(
    idBarbero: Long,
    navController: NavHostController,
    viewModel: HorarioDisponibleViewModel = viewModel()
) {
    val calendarState = rememberSelectableCalendarState(
        initialSelectionMode = SelectionMode.Single
    )
    val selectedDay = calendarState.selectionState.selection.firstOrNull()
        ?: Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date
    val fechaSeleccionada = selectedDay.toString()
    val horasDisponibles by viewModel.horasDisponibles.collectAsState()
    var horarioSeleccionado by remember { mutableStateOf<com.example.barberia.model.HorarioUi?>(null) }

    // Recarga horas cada vez que cambia el barbero o el día
    LaunchedEffect(idBarbero, fechaSeleccionada) {
        viewModel.cargarHorasDisponibles(idBarbero, fechaSeleccionada)
        horarioSeleccionado = null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Text(
            text = "Reservar horario",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Selecciona un día",
            style = MaterialTheme.typography.titleMedium,
            color = AzulBarberi,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            SelectableCalendar(
                calendarState = calendarState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }

        Text(
            text = "Horas disponibles",
            style = MaterialTheme.typography.titleMedium,
            color = AzulBarberi,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        if (horasDisponibles.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay horarios disponibles para este día.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
            ) {
                items(horasDisponibles) { horarioUi ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .border(
                                width = if (horarioSeleccionado == horarioUi) 2.dp else 1.dp,
                                color = if (horarioSeleccionado == horarioUi) AzulBarberi else MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { horarioSeleccionado = horarioUi },
                        colors = CardDefaults.cardColors(
                            containerColor = if (horarioSeleccionado == horarioUi)
                                AzulBarberi.copy(alpha = 0.12f)
                            else
                                MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = if (horarioSeleccionado == horarioUi) AzulBarberi else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = horarioUi.horaInicio,
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (horarioSeleccionado == horarioUi) AzulBarberi else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedVisibility(visible = horarioSeleccionado != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = AzulBarberi,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Seleccionado: $fechaSeleccionada a las ${horarioSeleccionado?.horaInicio.orEmpty()}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Button(
            onClick = {
                val horaCodificada = URLEncoder.encode(horarioSeleccionado?.horaInicio ?: "", "UTF-8")
                val servicioId = 1L // Reemplaza con el valor real
                val horarioDisponibleId = horarioSeleccionado?.idHorario ?: 0L
                val idAdministrador = 1L // Reemplaza con el valor real

                navController.navigate(
                    "reserva/$idBarbero/$fechaSeleccionada/$horaCodificada/$servicioId/$horarioDisponibleId/$idAdministrador"
                )
            },
            enabled = horarioSeleccionado != null,
            colors = ButtonDefaults.buttonColors(containerColor = AzulBarberi),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Confirmar reserva", style = MaterialTheme.typography.titleMedium, color = Color.White)
        }
    }
}
