package com.example.barberia.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ReservaScreen(
    navController: NavController,
    nombreBarbero: String,
    dia: String,
    hora: String
) {
    var nombreCliente by remember { mutableStateOf("") }
    var celularCliente by remember { mutableStateOf("") }
    var reservaExitosa by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Confirma tu Reserva",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Barbero: $nombreBarbero",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Día: $dia",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Hora: $hora",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = nombreCliente,
            onValueChange = { nombreCliente = it },
            label = { Text("Nombre Completo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = celularCliente,
            onValueChange = { celularCliente = it },
            label = { Text("Número de Celular") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                reservaExitosa = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reservar Cita")
        }

        if (reservaExitosa) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "¡Reserva Exitosa!",
                color = Color(0xFF4CAF50),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
