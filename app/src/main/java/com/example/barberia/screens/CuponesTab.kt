package com.example.barberia.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.barberia.model.Cupon
import com.example.barberia.viewmodel.CuponViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CuponesTab(
    cuponViewModel: CuponViewModel,
    idAdministrador: Long
) {
    val cupones by cuponViewModel.cupones.collectAsState()
    val isLoading by cuponViewModel.isLoading.collectAsState()
    val error by cuponViewModel.error.collectAsState()
    val mensaje by cuponViewModel.mensaje.collectAsState()

    var filtroSeleccionado by remember { mutableStateOf("todos") }
    var showEditarDialog by remember { mutableStateOf(false) }
    var cuponSeleccionado by remember { mutableStateOf<Cupon?>(null) }
    var showEliminarDialog by remember { mutableStateOf(false) }
    var cuponAEliminar by remember { mutableStateOf<Cupon?>(null) }

    LaunchedEffect(filtroSeleccionado) {
        cuponViewModel.cargarCupones(idAdministrador, filtroSeleccionado)
    }

    LaunchedEffect(mensaje) {
        if (mensaje != null) {
            kotlinx.coroutines.delay(3000)
            cuponViewModel.limpiarMensaje()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título y filtros
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Gestión de Cupones",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filtros
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = filtroSeleccionado == "todos",
                onClick = { filtroSeleccionado = "todos" },
                label = { Text("Todos") }
            )
            FilterChip(
                selected = filtroSeleccionado == "activos",
                onClick = { filtroSeleccionado = "activos" },
                label = { Text("Activos") }
            )
            FilterChip(
                selected = filtroSeleccionado == "expirados",
                onClick = { filtroSeleccionado = "expirados" },
                label = { Text("Expirados") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mensajes
        mensaje?.let {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text(
                    text = it,
                    modifier = Modifier.padding(16.dp),
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        error?.let {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.Red)
            ) {
                Text(
                    text = it,
                    modifier = Modifier.padding(16.dp),
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Lista de cupones
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (cupones.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay cupones disponibles",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cupones) { cupon ->
                    CuponCard(
                        cupon = cupon,
                        onEdit = {
                            cuponSeleccionado = cupon
                            showEditarDialog = true
                        },
                        onDelete = {
                            cuponAEliminar = cupon
                            showEliminarDialog = true
                        }
                    )
                }
            }
        }
    }

    // Dialog para editar cupón
    if (showEditarDialog && cuponSeleccionado != null) {
        EditarCuponDialog(
            cupon = cuponSeleccionado!!,
            onDismiss = {
                showEditarDialog = false
                cuponSeleccionado = null
            },
            onSave = { codigo, porcentaje, fecha, activo ->
                cuponViewModel.actualizarCupon(
                    idAdministrador,
                    cuponSeleccionado!!.id!!,
                    codigo,
                    porcentaje,
                    fecha,
                    activo
                )
                showEditarDialog = false
                cuponSeleccionado = null
            }
        )
    }

    // Dialog para confirmar eliminación
    if (showEliminarDialog && cuponAEliminar != null) {
        AlertDialog(
            onDismissRequest = {
                showEliminarDialog = false
                cuponAEliminar = null
            },
            title = { Text("Eliminar Cupón") },
            text = { Text("¿Estás seguro de que deseas eliminar el cupón ${cuponAEliminar!!.codigo}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        cuponViewModel.eliminarCupon(idAdministrador, cuponAEliminar!!.id!!)
                        showEliminarDialog = false
                        cuponAEliminar = null
                    }
                ) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showEliminarDialog = false
                        cuponAEliminar = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun CuponCard(
    cupon: Cupon,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val hoy = LocalDate.now()
    val fechaValidez = try {
        LocalDate.parse(cupon.fechaValidez)
    } catch (e: Exception) {
        try {
            LocalDate.parse(cupon.fechaValidez, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        } catch (e2: Exception) {
            null
        }
    }
    val estaExpirado = fechaValidez?.isBefore(hoy) ?: false
    val estaActivo = cupon.activo && !estaExpirado

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (estaActivo) Color.White else Color(0xFFF5F5F5)
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = cupon.codigo,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = if (estaActivo) Icons.Default.CheckCircle else Icons.Default.Cancel,
                        contentDescription = null,
                        tint = if (estaActivo) Color(0xFF4CAF50) else Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Descuento: ${cupon.porcentajeDescuento}%",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Válido hasta: ${cupon.fechaValidez}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (estaExpirado) Color.Red else Color.Gray
                )
                Text(
                    text = if (estaActivo) "Estado: Activo" else if (estaExpirado) "Estado: Expirado" else "Estado: Inactivo",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (estaActivo) Color(0xFF4CAF50) else Color.Red
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = Color(0xFF2196F3)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun EditarCuponDialog(
    cupon: Cupon,
    onDismiss: () -> Unit,
    onSave: (String, Int, String, Boolean) -> Unit
) {
    var codigo by remember { mutableStateOf(TextFieldValue(cupon.codigo)) }
    var porcentaje by remember { mutableStateOf(TextFieldValue(cupon.porcentajeDescuento.toString())) }
    var fechaValidez by remember { mutableStateOf(TextFieldValue(cupon.fechaValidez)) }
    var activo by remember { mutableStateOf(cupon.activo) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Cupón") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = codigo,
                    onValueChange = { codigo = it },
                    label = { Text("Código") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = porcentaje,
                    onValueChange = { if (it.text.all { char -> char.isDigit() }) porcentaje = it },
                    label = { Text("Porcentaje de Descuento") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = fechaValidez,
                    onValueChange = { fechaValidez = it },
                    label = { Text("Fecha de Validez (dd/MM/yyyy)") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Ej: 30/11/2025") }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = activo,
                        onCheckedChange = { activo = it }
                    )
                    Text("Cupón activo")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val porcentajeInt = porcentaje.text.toIntOrNull() ?: 0
                    if (codigo.text.isNotBlank() && porcentajeInt > 0 && fechaValidez.text.isNotBlank()) {
                        onSave(codigo.text.uppercase(), porcentajeInt, fechaValidez.text, activo)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

