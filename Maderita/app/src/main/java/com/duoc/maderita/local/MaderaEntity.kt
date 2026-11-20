package com.duoc.maderita.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Fila de la tabla "maderas" de Room: copia local del catálogo
@Entity(tableName = "maderas")
data class MaderaEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val nombreCientifico: String,
    val origen: String,
    val dureza: String,
    val descripcion: String,
    // Los usos se guardan en un solo texto separado por "|"
    val usos: String
)
