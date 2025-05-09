package com.example.barberia.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.barberia.viewmodel.HorarioDisponibleViewModel
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import kotlinx.datetime.Clock
import kotlinx.datetime.toLocalDateTime


@Composable
fun HorarioDisponibleScreen(
    barberoId: Long,
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
    var horaSeleccionada by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(barberoId, fechaSeleccionada) {
        viewModel.cargarHorasDisponibles(barberoId, fechaSeleccionada)
        horaSeleccionada = null
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text("Selecciona un día:", style = MaterialTheme.typography.headlineSmall)
        SelectableCalendar(
            calendarState = calendarState,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Horas disponibles:", style = MaterialTheme.typography.headlineSmall)
        if (horasDisponibles.isEmpty()) {
            Text("No hay horarios disponibles para este día.")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
            ) {
                items(horasDisponibles) { hora: String ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                horaSeleccionada = hora
                            },
                        colors = if (horaSeleccionada == hora)
                            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        else
                            CardDefaults.cardColors()
                    ) {
                        Text(
                            text = hora,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (horaSeleccionada != null) {
            Text(
                "Has seleccionado: $fechaSeleccionada a las $horaSeleccionada",
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            Text(
                "Por favor selecciona una hora.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("reserva/$barberoId/$fechaSeleccionada/$horaSeleccionada")
            },
            enabled = horaSeleccionada != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirmar")
        }
    }
}
