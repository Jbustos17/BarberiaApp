package com.example.barberia.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
    ) {
        Text("Resumen de la Reserva", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Barbero: $idBarbero")
        Text("Fecha: $fecha")
        Text("Hora: $hora")
        Text("Total a pagar: $${"%,.0f".format(total)} COP")
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = celular,
            onValueChange = { celular = it },
            label = { Text("Celular") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
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
                            // 1. Guarda el cliente con idAdministrador
                            val cliente = Cliente(
                                id_cliente = null,
                                nombre = nombre,
                                celular = celular,
                                correo = correo
                            )
                            val clienteGuardado = clienteViewModel.guardarCliente(cliente, idAdministrador)

                            clienteGuardado.id_cliente?.let { idCliente ->
                                // 2. Usa el id_cliente para crear la reserva con modelos auxiliares
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
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSaving
        ) {
            Text(if (isSaving) "Guardando..." else "Confirmar Reserva")
        }

        if (showError) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (showSuccess) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "¡Reserva realizada con éxito!",
                color = MaterialTheme.colorScheme.primary,
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
