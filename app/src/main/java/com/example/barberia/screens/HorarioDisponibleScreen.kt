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
import androidx.compose.material.icons.filled.ArrowBack
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
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun HorarioDisponibleScreen(
    idBarbero: Long,
    servicioId: Long,
    modalidad: String,
    navController: NavHostController,
    viewModel: HorarioDisponibleViewModel = viewModel(),
    carritoViewModel: com.example.barberia.viewmodel.CarritoViewModel,
    servicioViewModel: com.example.barberia.viewmodel.ServicioViewModel = viewModel(),
    barberoViewModel: com.example.barberia.viewmodel.BarberoViewModel = viewModel()
) {
    val calendarState = rememberSelectableCalendarState(
        initialSelectionMode = SelectionMode.Single
    )
    val selectedDay = calendarState.selectionState.selection.firstOrNull()
        ?: Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date
    val fechaSeleccionada = selectedDay.toString()
    val horasDisponibles by viewModel.horasDisponibles.collectAsState()
    val error by viewModel.error.collectAsState()
    var horarioSeleccionado by remember { mutableStateOf<com.example.barberia.model.HorarioUi?>(null) }
    
    val servicios by servicioViewModel.servicios.collectAsState()
    val barberos by barberoViewModel.barberos.collectAsState()
    
    // Filtrar horarios que ya pasaron si es el día de hoy
    val horasFiltradas = remember(horasDisponibles, fechaSeleccionada) {
        val fechaActual = LocalDate.now()
        val horaActual = LocalTime.now()
        val fechaSeleccionadaLocal = try {
            LocalDate.parse(fechaSeleccionada)
        } catch (e: Exception) {
            null
        }
        
        if (fechaSeleccionadaLocal != null && fechaSeleccionadaLocal.isEqual(fechaActual)) {
            // Es el día de hoy, filtrar horarios que ya pasaron
            horasDisponibles.filter { horarioUi ->
                try {
                    val horaInicio = LocalTime.parse(horarioUi.horaInicio, DateTimeFormatter.ofPattern("HH:mm"))
                    horaInicio.isAfter(horaActual)
                } catch (e: Exception) {
                    // Si hay error al parsear, mantener el horario
                    true
                }
            }
        } else {
            // No es hoy, mostrar todos los horarios
            horasDisponibles
        }
    }

    // Recarga datos necesarios
    LaunchedEffect(Unit) {
        servicioViewModel.cargarServicios(idAdministrador = 1L)
        barberoViewModel.obtenerBarberos()
    }

    // Recarga horas cada vez que cambia el barbero o el día
    LaunchedEffect(idBarbero, fechaSeleccionada) {
        android.util.Log.d("HorarioScreen", "LaunchedEffect: barbero=$idBarbero, fecha=$fechaSeleccionada")
        viewModel.cargarHorasDisponibles(idBarbero, fechaSeleccionada)
        horarioSeleccionado = null
    }
    
    // Resetear selección si el horario seleccionado ya no está en la lista filtrada
    LaunchedEffect(horasFiltradas) {
        if (horarioSeleccionado != null && !horasFiltradas.contains(horarioSeleccionado)) {
            horarioSeleccionado = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = AzulBarberi // O el color que prefieras
                )
            }
        }

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

        if (error != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = error ?: "Error desconocido",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.cargarHorasDisponibles(idBarbero, fechaSeleccionada) },
                        colors = ButtonDefaults.buttonColors(containerColor = AzulBarberi)
                    ) {
                        Text("Reintentar")
                    }
                }
            }
        } else if (horasFiltradas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (horasDisponibles.isEmpty()) 
                            "No hay horarios disponibles para este día."
                        else 
                            "No hay horarios disponibles. Todos los horarios de hoy ya pasaron.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
            ) {
                items(horasFiltradas) { horarioUi ->
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
                horarioSeleccionado?.let { horario ->
                    val servicio = servicios.find { it.id == servicioId }
                    val barbero = barberos.find { it.idBarbero == idBarbero }
                    
                    if (servicio != null) {
                        val carritoItem = com.example.barberia.model.CarritoItem(
                            servicio = servicio,
                            barberoId = idBarbero,
                            barberoNombre = barbero?.nombre ?: "Cualquier profesional",
                            fecha = fechaSeleccionada,
                            hora = horario.horaInicio,
                            horarioDisponibleId = horario.idHorario,
                            esADomicilio = (modalidad == "DOMICILIO"),
                            precioAdicionalDomicilio = if (modalidad == "DOMICILIO") {
                                barbero?.precioAdicionalDomicilio ?: 10000.0
                            } else {
                                0.0
                            }
                        )
                        
                        carritoViewModel.agregarItem(carritoItem)
                        navController.navigate("carrito")
                    }
                }
            },
            enabled = horarioSeleccionado != null,
            colors = ButtonDefaults.buttonColors(containerColor = AzulBarberi),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Agregar al Carrito", style = MaterialTheme.typography.titleMedium, color = Color.White)
        }
    }
}
