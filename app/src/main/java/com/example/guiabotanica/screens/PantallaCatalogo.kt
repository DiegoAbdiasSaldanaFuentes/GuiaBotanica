package com.example.guiabotanica.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.guiabotanica.data.PlantaRepository
import com.example.guiabotanica.viewmodel.PlantaViewModel
import com.example.guiabotanica.viewmodel.PlantaViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCatalogo(navController: NavController) {
    val context = LocalContext.current
    val dao = PlantaRepository.obtenerDao(context)
    val viewModel: PlantaViewModel = viewModel(factory = PlantaViewModelFactory(dao))

    var textoBusqueda by remember { mutableStateOf("") }
    val resultadosBusqueda by viewModel.plantasFiltradas.collectAsState()

    val categorias = listOf("Medicinales", "Frutales", "Vegetales", "Aromaticas", "Acuaticas", "Venenosas")

    Scaffold(
        topBar = { TopAppBar(title = { Text("Guía Botánica", fontWeight = FontWeight.Bold) }) },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón de Favoritos enlazado
                    IconButton(onClick = { navController.navigate("favoritos") }) {
                        Icon(imageVector = Icons.Default.Star, contentDescription = "Destacadas")
                    }

                    FloatingActionButton(
                        onClick = { navController.navigate("perfil") },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Perfil del Usuario")
                    }

                    // Botón de Ajustes enlazado
                    IconButton(onClick = { navController.navigate("ajustes") }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Ajustes")
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { nuevoTexto ->
                    textoBusqueda = nuevoTexto
                    viewModel.buscarPlantas(nuevoTexto)
                },
                label = { Text("Buscar planta (ej. Manzano)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (textoBusqueda.isNotBlank()) {
                if (resultadosBusqueda.isEmpty()) {
                    Text("No se encontraron plantas.", color = MaterialTheme.colorScheme.error)
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(resultadosBusqueda) { planta ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { navController.navigate("detalle/${planta.id}") },
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Text(
                                    text = planta.nombre,
                                    modifier = Modifier.padding(16.dp),
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(categorias) { categoria ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clickable { navController.navigate("lista/$categoria") },
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(text = categoria, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }
    }
}