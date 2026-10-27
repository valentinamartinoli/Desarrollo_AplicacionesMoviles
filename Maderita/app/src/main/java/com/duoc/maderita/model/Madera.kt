package com.duoc.maderita.model

import androidx.annotation.DrawableRes

// Tipos de madera
data class Madera(
    val id: Int,
    val nombre: String,
    @DrawableRes val imagen: Int
)