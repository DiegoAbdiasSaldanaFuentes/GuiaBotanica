package com.example.guiabotanica.model

data class Planta(
    val id: String,
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
    val imagenPrincipal: String? = null,
    val imagenesGaleria: List<String>? = null,
    val urlVideo: String? = null,

    val nombreCientifico: String? = null,
    val familia: String? = null,
    val habitat: String? = null,
    val altitud: String? = null,


    val esToxica: Boolean = false,
    val advertenciaToxicidad: String? = null,
    val primerosAuxilios: String? = null
)