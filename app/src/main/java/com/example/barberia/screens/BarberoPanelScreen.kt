package com.example.barberia.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.barberia.viewmodel.ReservaViewModel

@Composable
fun BarberoPanelScreen(
    idBarbero: Long,
    navController: NavHostController,
    reservaViewModel: ReservaViewModel = viewModel()
) {
    val reservas by reservaViewModel.reservasPorBarbero(idBarbero).collectAsState(initial = emptyList())

    LaunchedEffect(idBarbero) {
        reservaViewModel.cargarReservasPorBarbero(idBarbero)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrisClaro)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Mis Reservas",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = AzulBarberi,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            if (reservas.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No tienes reservas asignadas.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(reservas) { reserva ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            elevation = CardDefaults.cardElevation(6.dp)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text("Cliente: ${reserva.nombreCliente}", fontWeight = FontWeight.Bold)
                                Text("Fecha: ${reserva.fecha}")
                                Text("Hora: ${reserva.hora}")
                                Text("Servicio: ${reserva.servicio?.nombre ?: "N/A"}")
                            }
                        }
                    }
                }
            }
        }
    }
}
