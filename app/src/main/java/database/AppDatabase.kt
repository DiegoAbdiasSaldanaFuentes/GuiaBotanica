package com.example.guiabotanica.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.guiabotanica.model.Planta

import androidx.room.TypeConverters
import com.example.guiabotanica.database.Converters

@Database(entities = [Planta::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun plantaDao(): PlantaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun obtenerInstancia(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "guia_botanica_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}