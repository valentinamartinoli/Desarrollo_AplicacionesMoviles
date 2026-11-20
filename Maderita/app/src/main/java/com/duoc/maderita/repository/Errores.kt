package com.duoc.maderita.repository

import java.io.IOException
import retrofit2.HttpException

// Convierte un error técnico en un mensaje que se le puede mostrar al usuario
fun Throwable.mensajeParaUsuario(mensajeHttp: String): String = when (this) {
    is IOException -> "Sin conexión. Revisa tu internet e intenta de nuevo"
    is HttpException -> mensajeHttp
    else -> "Ocurrió un error inesperado"
}
