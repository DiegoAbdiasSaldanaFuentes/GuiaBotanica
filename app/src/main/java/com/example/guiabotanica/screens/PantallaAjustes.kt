package com.example.guiabotanica.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAjustes(navController: NavController) {
    var notificacionesActivas by remember { mutableStateOf(true) }
    var recordatoriosRiego by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).verticalScroll(rememberScrollState())
        ) {
            Text("Preferencias", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp))

            ListItemAjustesSwitch(titulo = "Notificaciones Generales", subtitulo = "Alertas de nuevos artículos", icono = Icons.Default.Notifications, checked = notificacionesActivas, onCheckedChange = { notificacionesActivas = it })
            ListItemAjustesSwitch(titulo = "Recordatorios de Riego", subtitulo = "Avisos para tus plantas favoritas", icono = Icons.Default.WaterDrop, checked = recordatoriosRiego, onCheckedChange = { recordatoriosRiego = it })

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text("Cuenta y Seguridad", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp))
            ListItemAjustesClicable(titulo = "Cambiar Contraseña", icono = Icons.Default.Security, onClick = { })
            ListItemAjustesClicable(titulo = "Borrar todos mis datos", subtitulo = "Esta acción es irreversible", icono = Icons.Default.DeleteForever, onClick = { })

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text("Información", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp))
            ListItemAjustesClicable(titulo = "Política de Privacidad", icono = Icons.Default.Security, onClick = { })
            ListItemAjustesClicable(titulo = "Acerca de Guía Botánica", subtitulo = "Versión 1.0.0", icono = Icons.Default.Info, onClick = { })
        }
    }
}

@Composable
fun ListItemAjustesSwitch(titulo: String, subtitulo: String, icono: ImageVector, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icono, contentDescription = titulo, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = titulo, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text(text = subtitulo, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun ListItemAjustesClicable(titulo: String, subtitulo: String? = null, icono: ImageVector, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(horizontal = 16.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icono, contentDescription = titulo, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = titulo, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            if (subtitulo != null) Text(text = subtitulo, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}