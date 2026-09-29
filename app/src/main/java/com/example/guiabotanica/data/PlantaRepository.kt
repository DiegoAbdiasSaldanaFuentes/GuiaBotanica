package com.example.guiabotanica.data

import android.content.Context
import com.example.guiabotanica.model.Planta
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

object PlantaRepository {
    // La lista ahora empieza vacía y se llena leyendo el JSON
    private var todasLasPlantas: List<Planta> = emptyList()

    // Función para leer el archivo de la carpeta assets
    fun inicializarDatos(context: Context) {
        if (todasLasPlantas.isEmpty()) {
            try {
                val inputStream = context.assets.open("plantas.json")
                val reader = InputStreamReader(inputStream)
                val tipoLista = object : TypeToken<List<Planta>>() {}.type
                todasLasPlantas = Gson().fromJson(reader, tipoLista)
                reader.close()
            } catch (e: Exception) {
                e.printStackTrace()
                // Si hay error (ej. escribiste mal el JSON), la app no explota
            }
        }
    }

    fun obtenerPlantasPorCategoria(categoria: String): List<Planta> {
        return todasLasPlantas.filter { it.categoria == categoria }
    }

    fun obtenerPlantaPorId(id: String): Planta? {
        return todasLasPlantas.find { it.id == id }
    }
}