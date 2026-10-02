package com.example.guiabotanica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.guiabotanica.screens.PantallaCatalogo
import com.example.guiabotanica.screens.PantallaDetallePlanta
import com.example.guiabotanica.screens.PantallaListaPlantas
import com.example.guiabotanica.screens.PantallaPerfil
import com.example.guiabotanica.ui.theme.GuiaBotanicaTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            com.example.guiabotanica.data.PlantaRepository.inicializarYMigrarDatos(this@MainActivity)
        }

        setContent {
            GuiaBotanicaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavegacion()
                }
            }
        }
    }
}

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "catalogo") {
        composable("catalogo") {
            PantallaCatalogo(navController)
        }

        composable("lista/{categoria}") { backStackEntry ->
            val categoriaSeleccionada = backStackEntry.arguments?.getString("categoria") ?: "Desconocida"
            PantallaListaPlantas(categoriaSeleccionada, navController)
        }

        composable("detalle/{plantaId}") { backStackEntry ->
            val idSeleccionado = backStackEntry.arguments?.getString("plantaId") ?: ""
            PantallaDetallePlanta(idSeleccionado, navController)
        }

        // Nueva ruta para evitar crasheos al apretar el botón inferior
        composable("perfil") {
            PantallaPerfil(navController)
        }
    }
}