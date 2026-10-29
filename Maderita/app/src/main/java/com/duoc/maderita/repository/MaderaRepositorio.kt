package com.duoc.maderita.repository

import com.duoc.maderita.R //imagen de relleno mientras se agregan las fotos de cada una
// luego se cambia a R.drawable.nombre
import com.duoc.maderita.model.Madera

// Fuente de datos de los tipos de madera

object MaderaRepositorio {
    private val maderas = listOf(
        Madera(
            id = 1,
            nombre = "Avellano chileno",
            imagen = R.drawable.maderaavellano,
            nombreCientifico = "Gevuina avellana",
            origen = "Nativa",
            dureza = "Semidura",
            descripcion = "Madera clara con manchas o jaspeados rojizos que la hacen muy decorativa." +
                    "Se encuentra entre la Región de Valparaíso y la Región de Los Lagos." +
                    "Es liviana, de veta fina y fácil de trabajar.",
            usos = listOf("Muebles", "Chapas decorativas", "Tornería", "Artesanía")
        ),
        Madera(
            id = 2,
            nombre = "Ciprés de la cordillera",
            imagen = R.drawable.maderacipres,
            nombreCientifico = "Austrocedrus chilensis",
            origen = "Nativa",
            dureza = "Blanda",
            descripcion = "Madera blanco-amarillenta y aromática." +
                    "Se encuentra entre la Región de Valparaíso y la Región de Los Lagos." +
                    "Se caracteriza por su gran durabilidad y resistencia.",
            usos = listOf("Tejuelas", "Puertas", "Postes", "Ventanas")
        ),
        Madera(
            id = 3,
            nombre = "Coigüe/Coihue",
            imagen = R.drawable.maderacoigue,
            nombreCientifico = "Nothofagus dombeyi",
            origen = "Nativa" ,
            dureza = "Dura",
            descripcion = "Madera de color rosado claro a pardo, densa y resistente." +
                "Se encuentra entre la Región de O'Higgins y la Región de  Aysén." +
                "Es muy usada en la zona sur para construcción y terminaciones.",
            usos = listOf("Pisos", "Estructuras", "Revestimientos", "Muebles")
        ),
        Madera(
            id = 4,
            nombre = "Laurel",
            imagen = R.drawable.maderalaurel,
            nombreCientifico = "Laurelia sempervirens",
            origen = "Nativa",
            dureza = "Blanda",
            descripcion = "Madera de color claro grisáceo, aromática y fácil de tallar y tornear." +
                    "Se encuentra entre la Región de O'Higgins y la Región de Los Lagos.",
            usos = listOf("Muebles", "Piezas torneadas", "Cajones", "Tallado")
        ),
        Madera(
            id = 5,
            nombre = "Lenga",
            imagen = R.drawable.maderalenga,
            nombreCientifico = "Nothofagus pumilio",
            origen = "Nativa",
            dureza = "Semidura",
            descripcion = "Especie típica de la zona austral, de color rosado pálido y veta fina." +
                "Se encuentra entre la Región del Maule y Tierra del Fuego." +
                "Es fácil de trabajar y tiene buen acabado.",
            usos = listOf("Muebles", "Puertas", "Terminaciones interiores", "Molduras")
        ),
        Madera(
            id = 6,
            nombre = "Pino Radiata",
            imagen = R.drawable.maderapino,
            nombreCientifico = "Pinus radiata",
            origen = "Introducida",
            dureza = "Blanda",
            descripcion = "La madera más utilizada en Chile, representa cerca del 97% de la producción nacional de madera." +
                "Se encuentra entre la Region de O'Higgins y la Región de Los Ríos." +
                "Es de color blanco amarillento, liviana y económica.",
            usos = listOf("Construcción", "Tableros", "Muebles", "Celulosa")
        ),
        Madera(
            id = 7,
            nombre = "Raulí",
            imagen = R.drawable.maderarauli,
            nombreCientifico = "Nothofagus alpina",
            origen = "Nativa",
            dureza = "Semidura",
            descripcion = "Madera de tono rojizo a rosado, de veta fina y muy fácil de trabajar. " +
                    "Se encuentra entre la Región del Maule y la Región de Los Lagos." +
                    "Es una de las maderas nativas más apreciadas en Chile por su belleza y estabilidad.",
            usos = listOf("Muebles finos", "Puertas y ventanas", "Revestimientos", "Pisos")
        ),
        Madera(
            id = 8,
            nombre = "Roble",
            imagen = R.drawable.maderaroble,
            nombreCientifico = "Nothofagus obliqua",
            origen = "Nativa",
            dureza = "Dura",
            descripcion = "Madera que varía de tonos amarillos opacos, marrones claros y cafés dorados y rojizos." +
                    "Se encuentra entre la Región de Valparaíso y la Región de Los Lagos." +
                    "Es considerada una de la más nobles y valoradas en la carpintería y en la construcción.",
            usos = listOf("Vigas y estructuras", "Postes", "Pisos", "Construcción exterior")
        ),
        Madera(
            id = 9,
            nombre = "Tepa",
            imagen = R.drawable.maderatepa,
            nombreCientifico = "Laureliopsis philippiana",
            origen = "Nativa",
            dureza = "Blanda",
            descripcion = "Madera clara, liviana y con un aroma característico. " +
                    "Se encuentra entre la Región del Maule y la Región de Aysén." +
                    "Se trabaja con facilidad y se usa principalmente en interiores.",
            usos = listOf("Molduras", "Cajonería", "Terciados", "Interiores")
        ),
        Madera(
            id = 10,
            nombre = "Ulmo",
            imagen = R.drawable.maderaulmo,
            nombreCientifico = "Eucryphia cordifolia",
            origen = "Nativa",
            dureza = "Dura",
            descripcion = "Madera pesada y resistente, de color rosado pardo. " +
                    "Se encuentra entre la Región del Bíobío y la Región de Los Lagos." +
                    "Es muy valorada como leña por su alto poder calorífico.",
            usos = listOf("Pisos", "Construcción", "Durmientes", "Leña")
        )
    )


    fun obtenerMaderas(): List<Madera> = maderas

    // vista detallada
    fun obtenerMaderaPorId(id: Int): Madera? = maderas.find { it.id ==id }
    }