package com.example.guiabotanica.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "plantas_favoritas",
    primaryKeys = ["usuarioEmail", "plantaId"],
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["correo"],
            childColumns = ["usuarioEmail"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Planta::class,
            parentColumns = ["id"],
            childColumns = ["plantaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PlantaFavorita(
    val usuarioEmail: String,
    val plantaId: String
)
