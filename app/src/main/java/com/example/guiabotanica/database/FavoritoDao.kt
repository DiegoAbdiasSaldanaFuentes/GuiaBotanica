package com.example.guiabotanica.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.guiabotanica.model.Planta
import com.example.guiabotanica.model.PlantaFavorita

@Dao
interface FavoritoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun agregarFavorito(plantaFavorita: PlantaFavorita)

    @Delete
    suspend fun eliminarFavorito(plantaFavorita: PlantaFavorita)

    @Query("SELECT p.* FROM plantas p INNER JOIN plantas_favoritas f ON p.id = f.plantaId WHERE f.usuarioEmail = :correoUsuario")
    suspend fun obtenerFavoritasDeUsuario(correoUsuario: String): List<Planta>

    @Query("SELECT p.* FROM plantas p INNER JOIN plantas_favoritas f ON p.id = f.plantaId WHERE f.usuarioEmail = :correoUsuario AND (p.nombre LIKE '%' || :query || '%' OR p.descripcion LIKE '%' || :query || '%')")
    suspend fun buscarFavoritasDeUsuario(correoUsuario: String, query: String): List<Planta>

    @Query("SELECT EXISTS(SELECT 1 FROM plantas_favoritas WHERE usuarioEmail = :correoUsuario AND plantaId = :plantaId)")
    suspend fun esFavorita(correoUsuario: String, plantaId: String): Boolean
}
