package com.example.guiabotanica.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaListaPlantas(categoria: String, navController: NavController) {
    val context = LocalContext.current
    val dao = PlantaRepository.obtenerDao(context)

    var plantas by remember { mutableStateOf<List<Planta>>(emptyList()) }

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(categoria) {
        isLoading = true
        plantas = dao.obtenerPorCategoria(categoria)
        isLoading = false
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Categoría: $categoria", fontWeight = FontWeight.Bold) }) }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (plantas.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay plantas registradas en esta categoría.")
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(plantas) { planta ->
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
        }
    }
}