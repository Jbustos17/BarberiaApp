package com.example.barberia.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun AdminPanelScreen(navController: NavHostController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Panel de Administrador")

        // Botón para ir a agregar un barbero
        Button(onClick = {
            navController.navigate("agregarBarbero")
        }) {
            Text("Agregar Barbero")
        }

        // Botón para ir a eliminar un barbero
        Button(onClick = {
            navController.navigate("eliminarBarbero")
        }) {
            Text("Eliminar Barbero")
        }
    }
}
