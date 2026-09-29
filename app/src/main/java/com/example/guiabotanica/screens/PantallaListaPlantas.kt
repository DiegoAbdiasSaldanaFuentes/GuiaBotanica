package com.example.guiabotanica.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.guiabotanica.data.PlantaRepository
import com.example.guiabotanica.model.Planta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaListaPlantas(categoria: String, navController: NavController) {
    val plantas = PlantaRepository.obtenerPlantasPorCategoria(categoria)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categoría: $categoria") },
                navigationIcon = {
                    // Reemplazamos el IconButton con error por un TextButton simple
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (plantas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay plantas registradas en esta categoría aún.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(plantas) { planta ->
                    PlantaItemRow(planta = planta) {
                        // Navegamos al detalle usando el ID de la planta
                        navController.navigate("detalle/${planta.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun PlantaItemRow(planta: Planta, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Un cuadro temporal donde irá la imagen de la planta
            Surface(
                modifier = Modifier.size(60.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            ) {}

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = planta.nombre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = planta.descripcion,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1 // Evitamos que textos largos rompan la tarjeta
                )
            }
        }
    }
}