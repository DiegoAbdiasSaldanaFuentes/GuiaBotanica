package com.example.guiabotanica.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.guiabotanica.network.RetrofitInstance
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaClima(navController: NavController) {
    // Estados para las coordenadas (por defecto en Talca para pruebas rápidas)
    var latitud by remember { mutableStateOf("-35.4264") }
    var longitud by remember { mutableStateOf("-71.6554") }

    // Estado reactivo para la respuesta o error (Exigido en la rúbrica)
    var resultadoClima by remember { mutableStateOf("Ingresa las coordenadas para conocer las condiciones.") }

    // Scope para lanzar la petición asíncrona sin bloquear la UI
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clima y Hábitat", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Consultar Open-Meteo API", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = latitud,
                onValueChange = { latitud = it },
                label = { Text("Latitud") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = longitud,
                onValueChange = { longitud = it },
                label = { Text("Longitud") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    scope.launch {
                        resultadoClima = "Cargando..."
                        try {
                            val lat = latitud.toDoubleOrNull() ?: 0.0
                            val lon = longitud.toDoubleOrNull() ?: 0.0

                            val response = RetrofitInstance.api.getWeather(latitude = lat, longitude = lon)

                            response.current?.let { current ->
                                resultadoClima = "Temperatura: ${current.temperature2m} °C\nViento: ${current.windSpeed10m} km/h"
                            } ?: run {
                                resultadoClima = "La API no devolvió datos para esta ubicación."
                            }
                        } catch (e: Exception) {
                            resultadoClima = "Error de red: Verifica tu conexión a internet o los datos ingresados."
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Consultar Clima", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(
                    text = resultadoClima,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}