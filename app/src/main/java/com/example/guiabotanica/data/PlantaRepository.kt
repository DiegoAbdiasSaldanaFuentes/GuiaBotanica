package com.example.guiabotanica.data

import android.content.Context
import com.example.guiabotanica.database.AppDatabase
import com.example.guiabotanica.database.PlantaDao
import com.example.guiabotanica.database.UsuarioDao
import com.example.guiabotanica.model.Planta
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

object PlantaRepository {

    fun obtenerDao(context: Context): PlantaDao {
        return AppDatabase.obtenerInstancia(context).plantaDao()
    }

    fun obtenerUsuarioDao(context: Context): UsuarioDao {
        return AppDatabase.obtenerInstancia(context).usuarioDao()
    }

    suspend fun inicializarYMigrarDatos(context: Context) {
        val dao = obtenerDao(context)
        try {
            // Verifica si la tabla ya tiene datos. Si tiene, cancela la inserción masiva.
            if (dao.contarPlantas() > 0) {
                return
            }

            val inputStream = context.assets.open("plantas.json")
            val reader = InputStreamReader(inputStream)

            val tipoLista = object : TypeToken<List<Planta>>() {}.type
            val plantas: List<Planta> = Gson().fromJson(reader, tipoLista)
            reader.close()

            dao.insertarTodas(plantas)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}