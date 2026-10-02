package com.example.guiabotanica.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.guiabotanica.components.IndicadorCard
import com.example.guiabotanica.components.InfoTarjeta
import com.example.guiabotanica.data.PlantaRepository
import kotlinx.coroutines.launch

// Función auxiliar para extraer el ID de un video de YouTube
fun extraerIdVideoYoutube(url: String?): String? {
    if (url.isNullOrEmpty()) return null
    return if (url.contains("v=")) {
        url.substringAfter("v=").substringBefore("&")
    } else if (url.contains("youtu.be/")) {
        url.substringAfter("youtu.be/").substringBefore("?")
    } else {
        null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetallePlanta(plantaId: String, navController: NavController) {
    val context = LocalContext.current
    val dao = PlantaRepository.obtenerDao(context)
    val scope = rememberCoroutineScope()

    var modoDetallado by remember { mutableStateOf(false) }
    var plantaState by remember { mutableStateOf<com.example.guiabotanica.model.Planta?>(null) }

    LaunchedEffect(plantaId) {
        plantaState = dao.obtenerPorId(plantaId)
    }

    val planta = plantaState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(planta?.nombre ?: "Cargando...") },
                navigationIcon = {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("Atrás")
                    }
                },
                actions = {
                    planta?.let { p ->
                        IconButton(onClick = {
                            val nuevoEstado = !p.esFavorita
                            val plantaActualizada = p.copy(esFavorita = nuevoEstado)
                            scope.launch {
                                dao.actualizarPlanta(plantaActualizada)
                                plantaState = plantaActualizada
                            }
                        }) {
                            Icon(
                                imageVector = if (p.esFavorita) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Marcar como favorita",
                                tint = if (p.esFavorita) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (planta == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AsyncImage(
                    model = planta.imagenPrincipal ?: "",
                    contentDescription = "Imagen principal de ${planta.nombre}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )

                if (planta.esToxica) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "⚠️ ADVERTENCIA DE TOXICIDAD",
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = planta.advertenciaToxicidad ?: "Planta peligrosa.")
                            if (planta.primerosAuxilios != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Primeros Auxilios: ${planta.primerosAuxilios}", fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IndicadorCard(titulo = "Riego", valor = planta.nivelRiego)
                    IndicadorCard(titulo = "Sol", valor = planta.nivelSol)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Modo Avanzado (Botánico)", fontWeight = FontWeight.Medium)
                    Switch(
                        checked = modoDetallado,
                        onCheckedChange = { modoDetallado = it }
                    )
                }

                if (modoDetallado) {
                    InfoTarjeta(titulo = "Nombre Científico", contenido = planta.nombreCientifico ?: "No registrado")
                    InfoTarjeta(titulo = "Familia", contenido = planta.familia ?: "No registrada")
                    InfoTarjeta(titulo = "Hábitat", contenido = planta.habitat ?: "No registrado")
                    InfoTarjeta(titulo = "Altitud", contenido = planta.altitud ?: "No registrada")
                }

                InfoTarjeta(titulo = "Descripción", contenido = planta.descripcion)
                InfoTarjeta(titulo = "Parásitos Comunes", contenido = planta.parasitosComunes)
                InfoTarjeta(titulo = "Tipo de Raíz", contenido = planta.tipoRaiz)
                InfoTarjeta(titulo = "Abono", contenido = planta.abono)
                InfoTarjeta(titulo = "Trasplante", contenido = planta.trasplante)
                InfoTarjeta(titulo = "¿Maceta primero?", contenido = planta.macetaPrimero)

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                Text(text = "Tutoriales y Galería", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                // Lógica de miniatura de YouTube en lugar del botón simple
                val videoId = extraerIdVideoYoutube(planta.urlVideo)
                if (videoId != null) {
                    val urlMiniatura = "https://img.youtube.com/vi/$videoId/hqdefault.jpg"

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(planta.urlVideo))
                                context.startActivity(intent)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = urlMiniatura,
                            contentDescription = "Miniatura del video",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        // Icono de play encima de la miniatura
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Reproducir",
                            modifier = Modifier.size(64.dp),
                            tint = Color.White
                        )
                    }
                }

                val galeria = planta.imagenesGaleria ?: emptyList()
                if (galeria.isNotEmpty()) {
                    val pagerState = rememberPagerState(pageCount = { galeria.size })
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) { page ->
                        AsyncImage(
                            model = galeria[page],
                            contentDescription = "Galería imagen ${page + 1} de ${planta.nombre}",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}