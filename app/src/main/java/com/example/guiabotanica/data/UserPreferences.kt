package com.example.guiabotanica.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para instanciar DataStore asegurando que sea un Singleton
val Context.dataStore by preferencesDataStore(name = "sesion_usuario")

class UserPreferences(private val context: Context) {

    companion object {
        val CORREO_USUARIO = stringPreferencesKey("correo_usuario")
    }

    // Lee el correo. Retorna un Flow que emite el valor (o null si no hay sesión)
    val correoLogueado: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[CORREO_USUARIO]
    }

    // Guarda el correo al iniciar sesión exitosamente
    suspend fun guardarSesion(correo: String) {
        context.dataStore.edit { preferences ->
            preferences[CORREO_USUARIO] = correo
        }
    }

    // Borra el correo al cerrar sesión
    suspend fun cerrarSesion() {
        context.dataStore.edit { preferences ->
            preferences.remove(CORREO_USUARIO)
        }
    }
}