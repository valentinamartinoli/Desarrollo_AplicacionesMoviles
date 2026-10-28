package com.duoc.maderita.model

import androidx.annotation.DrawableRes

// Tipos de madera
data class Madera(
    val id: Int,
    val nombre: String,
    @DrawableRes val imagen: Int,
    val nombreCientifico: String,
    val origen: String, //Nativa o Introducida
    val dureza: String, //Blanda, Semidura, Dura
    val descripcion: String,
    val usos: List<String>
)