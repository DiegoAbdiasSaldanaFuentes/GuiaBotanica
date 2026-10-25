package com.example.guiabotanica.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.guiabotanica.model.Usuario

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun registrarUsuario(usuario: Usuario): Long

    @Query("SELECT * FROM usuarios WHERE correo = :correoBuscado AND contrasena = :contrasenaBuscada")
    suspend fun login(correoBuscado: String, contrasenaBuscada: String): Usuario?


    @Query("SELECT * FROM usuarios WHERE correo = :correoBuscado")
    suspend fun obtenerUsuarioPorCorreo(correoBuscado: String): Usuario?

    @androidx.room.Update
    suspend fun actualizarUsuario(usuario: Usuario)
}