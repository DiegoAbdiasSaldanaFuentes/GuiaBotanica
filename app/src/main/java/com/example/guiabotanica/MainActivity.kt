package com.example.guiabotanica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.guiabotanica.screens.PantallaCatalogo
import com.example.guiabotanica.screens.PantallaListaPlantas
import com.example.guiabotanica.ui.theme.GuiaBotanicaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.example.guiabotanica.data.PlantaRepository.inicializarDatos(this)
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
            com.example.guiabotanica.screens.PantallaDetallePlanta(idSeleccionado, navController)
        }
    }
}