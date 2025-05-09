package com.example.barberia.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


@Composable
fun ReservaScreen(
    barberoId: Long,
    fecha: String,
    hora: String,
    total: Double = 30000.0, // Puedes pasar el total como argumento si lo necesitas dinámico
    navController: NavHostController
) {
    var nombre by remember { mutableStateOf("") }
    var celular by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
    ) {
        Text("Resumen de la Reserva", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Barbero: $barberoId") // Puedes mostrar el nombre si lo tienes
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
                } else {
                    showError = false
                    showSuccess = true
                    // Aquí puedes llamar a tu ViewModel para guardar la reserva
                    // y luego navegar, por ejemplo:
                    // navController.navigate("inicio") { popUpTo("inicio") { inclusive = true } }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirmar Reserva")
        }

        if (showError) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Por favor completa todos los campos.",
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
        }
    }
}
