package com.duoc.maderita.repository

import com.duoc.maderita.R //imagen de relleno mientras se agregan las fotos de cada una
// luego se cambia a R.drawable.nombre
import com.duoc.maderita.model.Madera

// Fuente de datos de los tipos de madera

object MaderaRepositorio {
    private val maderas = listOf(
        Madera(id = 1, nombre = "Avellano", imagen = R.drawable.maderita),
        Madera(id = 2, nombre = "Ciprés de las Guatecas", imagen = R.drawable.maderita),
        Madera(id = 3, nombre = "Coigüe", imagen = R.drawable.maderita),
        Madera(id = 4, nombre = "Eucalipto", imagen = R.drawable.maderita),
        Madera(id = 5, nombre = "Laurel", imagen = R.drawable.maderita),
        Madera(id = 6, nombre = "Lenga", imagen = R.drawable.maderita),
        Madera(id = 7, nombre = "Pino radiata", imagen = R.drawable.maderita),
        Madera(id = 8, nombre = "Raulí", imagen = R.drawable.maderita),
        Madera(id = 9, nombre = "Roble", imagen = R.drawable.maderita),
        Madera(id = 10, nombre = "Tepa", imagen = R.drawable.maderita)
    )

    fun obtenerMaderas(): List<Madera> = maderas

    // vista detallada
    // fun obtenerMaderaPorId(id: Int): Madera? = maderas.find { it.id ==id }
}