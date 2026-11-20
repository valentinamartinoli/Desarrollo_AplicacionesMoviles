package com.duoc.maderita.model

// Relaciona cada madera del catálogo de Xano con la foto que viene dentro de la app.
// Así las fotos se ven aunque no haya internet.
object ImagenesMadera {
    const val POR_DEFECTO = "maderita"

    private val recursoPorNombre = mapOf(
        "avellano chileno" to "maderaavellano",
        "ciprés de la cordillera" to "maderacipres",
        "coigüe/coihue" to "maderacoigue",
        "laurel" to "maderalaurel",
        "lenga" to "maderalenga",
        "pino radiata" to "maderapino",
        "raulí" to "maderarauli",
        "roble" to "maderaroble",
        "tepa" to "maderatepa",
        "ulmo" to "maderaulmo"
    )

    // Devuelve el nombre del archivo de la foto (sin extensión)
    fun nombreRecurso(nombreMadera: String): String =
        recursoPorNombre[nombreMadera.trim().lowercase()] ?: POR_DEFECTO
}
