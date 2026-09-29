package com.example.guiabotanica.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.* // Importante para remember y mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.guiabotanica.data.PlantaRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetallePlanta(plantaId: String, navController: NavController) {
    val planta = PlantaRepository.obtenerPlantaPorId(plantaId)

    // Aquí guardamos el estado de la interfaz. Si cambia a true, la pantalla se redibuja sola.
    var modoDetallado by remember { mutableStateOf(false) }

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
                // 1. Carrusel de imágenes
                val pagerState = rememberPagerState(pageCount = { 3 })
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                ) { page ->
                    AsyncImage(
                        model = "https://picsum.photos/seed/${planta.id}$page/400/250",
                        contentDescription = "Imagen de ${planta.nombre}",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // 2. Advertencia de Seguridad (Solo aparece si la planta es tóxica en el JSON)
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
                        onCheckedChange = { modoDetallado = it } // Cambia el valor al presionarlo
                    )
                }

                // 5. Bloque de Datos Científicos (Solo visible si el interruptor está activado)
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
            }
        }
    }
}

// Estos dos componentes idealmente irán en TarjetasUI.kt más adelante
@Composable
fun IndicadorCard(titulo: String, valor: String) {
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = titulo, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
            Text(text = valor, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun InfoTarjeta(titulo: String, contenido: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = titulo,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = contenido, fontSize = 16.sp)
        }
    }
}