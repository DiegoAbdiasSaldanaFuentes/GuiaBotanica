package com.example.guiabotanica.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    // Convierte el texto guardado en SQLite de vuelta a una Lista de Kotlin
    @TypeConverter
    fun deStringALista(data: String?): List<String>? {
        if (data == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(data, listType)
    }

    // Convierte la Lista de Kotlin a un texto JSON para poder guardarlo en SQLite
    @TypeConverter
    fun deListaAString(lista: List<String>?): String {
        return gson.toJson(lista)
    }
}