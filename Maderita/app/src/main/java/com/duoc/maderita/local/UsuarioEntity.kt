package com.duoc.maderita.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Fila de la tabla "usuario" de Room: el usuario que inició sesión y su token de Xano
@Entity(tableName = "usuario")
data class UsuarioEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val apellido: String,
    val nacionalidad: String,
    val telefono: String,
    val correo: String,
    val token: String,
    val sesionActiva: Boolean
)
