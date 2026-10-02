package com.example.guiabotanica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.guiabotanica.data.PlantaRepository
import com.example.guiabotanica.data.UserPreferences
import com.example.guiabotanica.screens.*
import com.example.guiabotanica.ui.theme.GuiaBotanicaTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            PlantaRepository.inicializarYMigrarDatos(this@MainActivity)
        }
        setContent {
            GuiaBotanicaTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNavegacion()
                }
            }
        }
    }
}

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val userPreferences = UserPreferences(context)

    // Escuchamos el estado del DataStore
    val correoLogueado by userPreferences.correoLogueado.collectAsState(initial = null)

    // Definimos el destino inicial en base a si el correo existe o no
    val startDestination = if (correoLogueado == null) "login" else "catalogo"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") { PantallaLogin(navController) }
        composable("registro") { PantallaRegistro(navController) }
        composable("catalogo") { PantallaCatalogo(navController) }

        composable("lista/{categoria}") { backStackEntry ->
            val categoriaSeleccionada = backStackEntry.arguments?.getString("categoria") ?: "Desconocida"
            PantallaListaPlantas(categoriaSeleccionada, navController)
        }

        composable("detalle/{plantaId}") { backStackEntry ->
            val idSeleccionado = backStackEntry.arguments?.getString("plantaId") ?: ""
            PantallaDetallePlanta(idSeleccionado, navController)
        }

        composable("perfil") { PantallaPerfil(navController) }
        composable("favoritos") { PantallaFavoritos(navController) }
        composable("ajustes") { PantallaAjustes(navController) }
    }
}