package com.example.guiabotanica.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.guiabotanica.model.Planta

@Dao
interface PlantaDao {
    @Query("SELECT * FROM plantas WHERE categoria = :categoriaBuscada")
    suspend fun obtenerPorCategoria(categoriaBuscada: String): List<Planta>

    @Query("SELECT * FROM plantas WHERE id = :idBuscado")
    suspend fun obtenerPorId(idBuscado: String): Planta?

    @Query("SELECT * FROM plantas WHERE nombre LIKE '%' || :query || '%' OR descripcion LIKE '%' || :query || '%'")
    suspend fun buscarPlantas(query: String): List<Planta>

    // Obtener la lista de plantas marcadas como favoritas para el Perfil
    @Query("SELECT * FROM plantas WHERE esFavorita = 1")
    suspend fun obtenerFavoritas(): List<Planta>

    // Actualizar el estado de una planta (ej. cambiar a favorita)
    @Update
    suspend fun actualizarPlanta(planta: Planta)

    @Query("SELECT COUNT(*) FROM plantas")
    suspend fun contarPlantas(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(plantas: List<Planta>)
}