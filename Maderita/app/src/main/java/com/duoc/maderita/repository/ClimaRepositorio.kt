package com.duoc.maderita.repository

import com.duoc.maderita.remote.ClimaApi
import kotlinx.coroutines.CancellationException

data class Clima(val temperatura: Double, val humedad: Double)

// Clima actual desde una API externa (Open-Meteo). No usa los endpoints propios de Xano.
interface ClimaRepositorio {
    suspend fun obtenerClima(): Result<Clima>
}

class ClimaRepositorioImpl(private val api: ClimaApi) : ClimaRepositorio {

    override suspend fun obtenerClima(): Result<Clima> = try {
        val actual = api.obtenerClima(LATITUD_SANTIAGO, LONGITUD_SANTIAGO).actual
        val temperatura = actual?.temperatura
        val humedad = actual?.humedad
        if (temperatura == null || humedad == null) {
            Result.failure(IllegalStateException("Respuesta de clima incompleta"))
        } else {
            Result.success(Clima(temperatura, humedad))
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

    private companion object {
        const val LATITUD_SANTIAGO = -33.45
        const val LONGITUD_SANTIAGO = -70.66
    }
}
