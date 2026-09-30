package com.example.guiabotanica.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.guiabotanica.model.Planta

@Dao
interface PlantaDao {

    // Consulta SQL pura para obtener plantas por categoría
    @Query("SELECT * FROM plantas WHERE categoria = :categoriaBuscada")
    suspend fun obtenerPorCategoria(categoriaBuscada: String): List<Planta>

    // Consulta SQL pura para buscar una planta por ID
    @Query("SELECT * FROM plantas WHERE id = :idBuscado")
    suspend fun obtenerPorId(idBuscado: String): Planta?

    // Sentencia SQL para la búsqueda en tiempo real (que implementaremos en el siguiente paso)
    @Query("SELECT * FROM plantas WHERE nombre LIKE '%' || :query || '%' OR descripcion LIKE '%' || :query || '%'")
    suspend fun buscarPlantas(query: String): List<Planta>

    // Inserción masiva o unitaria (reemplaza si ya existe el ID)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(plantas: List<Planta>)
}