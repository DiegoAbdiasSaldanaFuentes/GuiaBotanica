package com.example.guiabotanica.data

import android.content.Context
import com.example.guiabotanica.database.AppDatabase
import com.example.guiabotanica.database.PlantaDao
import com.example.guiabotanica.model.Planta
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

object PlantaRepository {

    // Lee el JSON y hace un INSERT masivo en la base de datos SQL
    suspend fun inicializarYMigrarDatos(context: Context) {
        withContext(Dispatchers.IO) {
            val db = AppDatabase.obtenerInstancia(context)
            val dao = db.plantaDao()

            try {
                val inputStream = context.assets.open("plantas.json")
                val reader = InputStreamReader(inputStream)
                val tipoLista = object : TypeToken<List<Planta>>() {}.type
                val plantasDelJson: List<Planta> = Gson().fromJson(reader, tipoLista)
                reader.close()

                dao.insertarTodas(plantasDelJson)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Expone el acceso SQL a las pantallas y ViewModels
    fun obtenerDao(context: Context): PlantaDao {
        return AppDatabase.obtenerInstancia(context).plantaDao()
    }
}