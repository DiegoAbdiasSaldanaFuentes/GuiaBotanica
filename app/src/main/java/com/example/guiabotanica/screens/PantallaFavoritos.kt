package com.example.guiabotanica.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.guiabotanica.data.PlantaRepository
import com.example.guiabotanica.model.Planta
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFavoritos(navController: NavController) {
    val context = LocalContext.current
    val dao = PlantaRepository.obtenerDao(context)

    var plantasFavoritas by remember { mutableStateOf<List<Planta>>(emptyList()) }
    var textoBusqueda by remember { mutableStateOf("") }

    // Ejecuta la búsqueda inicial o reactiva cuando cambie textoBusqueda
    LaunchedEffect(textoBusqueda) {
        delay(300) // Debounce para no saturar SQLite
        plantasFavoritas = if (textoBusqueda.isBlank()) {
            dao.obtenerFavoritas()
        } else {
            dao.buscarFavoritas(textoBusqueda)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Favoritos", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)
        ) {
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                label = { Text("Buscar en favoritos...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (plantasFavoritas.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                    Text("No se encontraron resultados.")
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().weight(1f)) {
                    items(plantasFavoritas) { planta ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { navController.navigate("detalle/${planta.id}") },
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = planta.nombre, fontSize = 18.sp)
                                Icon(Icons.Default.Favorite, contentDescription = "Favorita", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    }
}