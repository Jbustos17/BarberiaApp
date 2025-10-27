package com.example.barberia.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.barberia.viewmodel.GaleriaViewModel
import com.example.barberia.viewmodel.BarberoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GaleriaScreen(
    navController: NavHostController,
    idBarbero: Long,
    galeriaViewModel: GaleriaViewModel = viewModel(),
    barberoViewModel: BarberoViewModel = viewModel()
) {
    val galeria by galeriaViewModel.galeria.collectAsState()
    val isLoading by galeriaViewModel.isLoading.collectAsState()
    val error by galeriaViewModel.error.collectAsState()
    val barberos by barberoViewModel.barberos.collectAsState()

    LaunchedEffect(idBarbero) {
        galeriaViewModel.cargarGaleria(idBarbero)
        barberoViewModel.obtenerBarberos()
    }

    val barbero = barberos.find { it.idBarbero == idBarbero }
    val snackbarHostState = remember { SnackbarHostState() }

    // Mostrar error si existe
    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
            galeriaViewModel.limpiarError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(GrisClaro)
                .padding(paddingValues)
        ) {
            // Canvas decorativo
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawPath(
                    path = Path().apply {
                        moveTo(0f, size.height * 0.85f)
                        cubicTo(
                            size.width * 0.25f, size.height * 0.95f,
                            size.width * 0.75f, size.height * 0.75f,
                            size.width, size.height * 0.9f
                        )
                        lineTo(size.width, size.height)
                        lineTo(0f, size.height)
                        close()
                    },
                    brush = Brush.horizontalGradient(
                        colors = listOf(AzulClaroBarberia, AzulBarberi, DoradoBarberia, AmarilloBarberia)
                    )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header con botón de volver
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = AzulBarberi
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            "Galería de Cortes",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = AzulBarberi
                        )
                        barbero?.let {
                            Text(
                                it.nombre ?: "Barbero",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray
                            )
                        }
                    }
                }

                if (isLoading && galeria.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = AzulBarberi)
                    }
                } else if (galeria.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.PhotoLibrary,
                                contentDescription = null,
                                modifier = Modifier.size(80.dp),
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "No hay fotos en la galería",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(galeria) { foto ->
                            FotoCard(fotoUrl = foto.fotoUrl, descripcion = foto.descripcion)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FotoCard(fotoUrl: String, descripcion: String?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = fotoUrl,
                contentDescription = descripcion,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            
            // Overlay con descripción si existe
            descripcion?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                            )
                        )
                        .padding(8.dp)
                ) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        maxLines = 2
                    )
                }
            }
        }
    }
}

