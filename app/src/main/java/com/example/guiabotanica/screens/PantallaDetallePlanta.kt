package com.example.guiabotanica.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.guiabotanica.components.IndicadorCard
import com.example.guiabotanica.components.InfoTarjeta
import com.example.guiabotanica.data.PlantaRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetallePlanta(plantaId: String, navController: NavController) {
    val planta = PlantaRepository.obtenerPlantaPorId(plantaId)
    var modoDetallado by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(planta?.nombre ?: "Error") },
                navigationIcon = {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (planta == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Planta no encontrada.")
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
                // 1. Cabecera Principal (Lee la imagen local desde assets)
                AsyncImage(
                    model = planta.imagenPrincipal ?: "",
                    contentDescription = "Imagen principal de ${planta.nombre}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                // 2. Advertencia de Seguridad
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

                // 3. Indicadores Básicos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IndicadorCard(titulo = "Riego", valor = planta.nivelRiego)
                    IndicadorCard(titulo = "Sol", valor = planta.nivelSol)
                }

                // 4. Interruptor de Modo Detallado
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

                // 5. Bloque de Datos Científicos
                if (modoDetallado) {
                    InfoTarjeta(titulo = "Nombre Científico", contenido = planta.nombreCientifico ?: "No registrado")
                    InfoTarjeta(titulo = "Familia", contenido = planta.familia ?: "No registrada")
                    InfoTarjeta(titulo = "Hábitat", contenido = planta.habitat ?: "No registrado")
                    InfoTarjeta(titulo = "Altitud", contenido = planta.altitud ?: "No registrada")
                }

                // 6. Tarjetas Estándar
                InfoTarjeta(titulo = "Descripción", contenido = planta.descripcion)
                InfoTarjeta(titulo = "Parásitos Comunes", contenido = planta.parasitosComunes)
                InfoTarjeta(titulo = "Tipo de Raíz", contenido = planta.tipoRaiz)
                InfoTarjeta(titulo = "Abono", contenido = planta.abono)
                InfoTarjeta(titulo = "Trasplante", contenido = planta.trasplante)
                InfoTarjeta(titulo = "¿Maceta primero?", contenido = planta.macetaPrimero)

                // 7. Sección Multimedia (Video y Galería basados en el JSON)
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                Text(text = "Tutoriales y Galería", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                // Botón de Video condicional (Solo aparece si el JSON tiene un enlace válido)
                if (!planta.urlVideo.isNullOrEmpty()) {
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(planta.urlVideo))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ver Video Tutorial")
                    }
                }

                // Carrusel de Imágenes Secundarias (Galería)
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
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}