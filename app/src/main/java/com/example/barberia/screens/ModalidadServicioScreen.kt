package com.example.barberia.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.barberia.screens.AzulBarberi
import com.example.barberia.screens.DoradoBarberia

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalidadServicioScreen(
    idCliente: Long,
    navController: NavHostController
) {
    var modalidadSeleccionada by remember { mutableStateOf<String?>(null) }
    var showDomicilioDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo decorativo
        Canvas(modifier = Modifier.fillMaxSize()) {
            val path1 = Path().apply {
                moveTo(0f, size.height * 0.3f)
                quadraticBezierTo(
                    size.width * 0.5f, size.height * 0.25f,
                    size.width, size.height * 0.35f
                )
                lineTo(size.width, size.height * 0.5f)
                quadraticBezierTo(
                    size.width * 0.5f, size.height * 0.55f,
                    0f, size.height * 0.45f
                )
                close()
            }
            drawPath(path1, AzulBarberi.copy(alpha = 0.1f), style = Fill)

            val path2 = Path().apply {
                moveTo(0f, size.height * 0.7f)
                quadraticBezierTo(
                    size.width * 0.5f, size.height * 0.65f,
                    size.width, size.height * 0.75f
                )
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
            drawPath(path2, DoradoBarberia.copy(alpha = 0.15f), style = Fill)
        }

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("¿Cómo prefieres tu servicio?", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { 
                            // Volver al inicio (pantalla de login/inicio)
                            navController.navigate("inicio") {
                                popUpTo("modalidadServicio/{idCliente}") { inclusive = true }
                            }
                        }) {
                            Icon(Icons.Default.ArrowBack, "Volver")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = AzulBarberi,
                        navigationIconContentColor = AzulBarberi
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Spacer(Modifier.height(8.dp))
                
                Text(
                    text = "Selecciona la modalidad que prefieres",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = AzulBarberi,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Puedes elegir entre asistir a la barbería o recibir el servicio en tu domicilio",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(8.dp))

                // Opción Presencial
                ModalidadCard(
                    titulo = "Presencial",
                    descripcion = "Visita nuestra barbería y disfruta del ambiente",
                    icon = Icons.Default.Store,
                    seleccionada = modalidadSeleccionada == "PRESENCIAL",
                    onClick = { modalidadSeleccionada = "PRESENCIAL" }
                )

                // Opción A Domicilio
                ModalidadCard(
                    titulo = "A Domicilio",
                    descripcion = "Comodidad en tu casa con un cargo adicional",
                    icon = Icons.Default.Home,
                    seleccionada = modalidadSeleccionada == "DOMICILIO",
                    onClick = { 
                        showDomicilioDialog = true
                    }
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        modalidadSeleccionada?.let { modalidad ->
                            navController.navigate("servicio/$idCliente/$modalidad")
                        }
                    },
                    enabled = modalidadSeleccionada != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulBarberi),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Continuar",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(Modifier.height(16.dp))
            }
        }
        
        // Diálogo de advertencia para servicio a domicilio
        if (showDomicilioDialog) {
            AlertDialog(
                onDismissRequest = { showDomicilioDialog = false },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(48.dp)
                    )
                },
                title = {
                    Text(
                        "Servicio a Domicilio",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = AzulBarberi
                    )
                },
                text = {
                    Column {
                        Text(
                            "Al elegir servicio a domicilio, ten en cuenta:",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        
                        Row(
                            modifier = Modifier.padding(bottom = 8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text("• ", fontSize = 20.sp, color = Color(0xFF4CAF50))
                            Text(
                                "Cada barbero tiene su propio precio adicional por domicilio",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        
                        Row(
                            modifier = Modifier.padding(bottom = 8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text("• ", fontSize = 20.sp, color = Color(0xFF4CAF50))
                            Text(
                                "Verás el precio adicional al seleccionar el barbero",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            Text("• ", fontSize = 20.sp, color = Color(0xFF4CAF50))
                            Text(
                                "Este cargo se sumará al precio del servicio",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            modalidadSeleccionada = "DOMICILIO"
                            showDomicilioDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Entendido, continuar", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = { showDomicilioDialog = false },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancelar", color = AzulBarberi)
                    }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@Composable
fun ModalidadCard(
    titulo: String,
    descripcion: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    seleccionada: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (seleccionada) AzulBarberi else Color.LightGray
    val backgroundColor = if (seleccionada) AzulBarberi.copy(alpha = 0.1f) else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 180.dp)
            .border(3.dp, borderColor, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(if (seleccionada) 12.dp else 4.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Fondo decorativo
            if (seleccionada) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = AzulBarberi.copy(alpha = 0.1f),
                        radius = size.minDimension * 0.4f,
                        center = Offset(size.width * 0.85f, size.height * 0.25f)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = titulo,
                    tint = if (seleccionada) AzulBarberi else Color.Gray,
                    modifier = Modifier.size(56.dp)
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (seleccionada) AzulBarberi else Color.DarkGray
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 20.sp
                    ),
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    maxLines = 3
                )
            }
        }
    }
}

