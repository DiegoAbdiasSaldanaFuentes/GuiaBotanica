package com.example.guiabotanica.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plantas")
data class Planta(
    @PrimaryKey val id: String,
    val nombre: String,
    val categoria: String,
    val nivelRiego: String,
    val nivelSol: String,
    val descripcion: String,
    val parasitosComunes: String,
    val tipoRaiz: String,
    val abono: String,
    val trasplante: String,
    val macetaPrimero: String,
    val nombreCientifico: String?,
    val familia: String?,
    val habitat: String?,
    val altitud: String?,
    val esToxica: Boolean,
    val advertenciaToxicidad: String? = null,
    val primerosAuxilios: String? = null,
    val imagenPrincipal: String?,
    val imagenesGaleria: List<String>?,
    val urlVideo: String?,
    var esFavorita: Boolean = false // Nueva columna para manejar el perfil
)