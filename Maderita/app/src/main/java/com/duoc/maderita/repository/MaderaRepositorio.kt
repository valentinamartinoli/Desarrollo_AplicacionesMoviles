package com.duoc.maderita.repository

import com.duoc.maderita.R //imagen de relleno mientras se agregan las fotos de cada una
// luego se cambia a R.drawable.nombre
import com.duoc.maderita.model.Madera

// Fuente de datos de los tipos de madera

object MaderaRepositorio {
    private val maderas = listOf(
        Madera(id = 1, nombre = "Avellano", imagen = R.drawable.madera1),
        Madera(id = 2, nombre = "Ciprés de las Guatecas", imagen = R.drawable.madera2),
        Madera(id = 3, nombre = "Coigüe", imagen = R.drawable.madera3),
        Madera(id = 4, nombre = "Eucalipto", imagen = R.drawable.madera4),
        Madera(id = 5, nombre = "Laurel", imagen = R.drawable.madera5),
        Madera(id = 6, nombre = "Lenga", imagen = R.drawable.madera6),
        Madera(id = 7, nombre = "Pino radiata", imagen = R.drawable.madera7),
        Madera(id = 8, nombre = "Raulí", imagen = R.drawable.madera8),
        Madera(id = 9, nombre = "Roble", imagen = R.drawable.madera9),
        Madera(id = 10, nombre = "Tepa", imagen = R.drawable.madera10)
    )

    fun obtenerMaderas(): List<Madera> = maderas

    // vista detallada
    // fun obtenerMaderaPorId(id: Int): Madera? = maderas.find { it.id ==id }

    fun obtener4Ejemplos(): List<Madera> = maderas.take(4)

    //Lista para el HS, saca 4 para mostrar
}