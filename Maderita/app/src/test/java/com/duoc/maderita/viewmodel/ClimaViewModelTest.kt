package com.duoc.maderita.viewmodel

import com.duoc.maderita.paso
import com.duoc.maderita.repository.Clima
import com.duoc.maderita.repository.ClimaRepositorio
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

// Repositorio de clima falso: devuelve lo que la prueba le indique y anota las coordenadas pedidas
private class ClimaRepositorioFalso : ClimaRepositorio {
    var resultado: Result<Clima> = Result.success(Clima(17.3, 62.0))
    val coordenadasPedidas = mutableListOf<Pair<Double, Double>>()

    override suspend fun obtenerClima(latitud: Double, longitud: Double): Result<Clima> {
        coordenadasPedidas.add(Pair(latitud, longitud))
        return resultado
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ClimaViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private lateinit var repositorio: ClimaRepositorioFalso

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
        repositorio = ClimaRepositorioFalso()
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    @Test
    fun `al abrirse muestra el clima de Santiago`() = runTest {
        paso("PREPARACION", "el repositorio falso responderá 17.3 grados y 62% de humedad")

        paso("ACCION", "crear el ViewModel")
        val viewModel = ClimaViewModel(repositorio)
        advanceUntilIdle()

        paso("COMPROBACION", "se pidió el clima de Santiago y la tarjeta tiene sus datos")
        assertEquals(listOf(Pair(-33.45, -70.66)), repositorio.coordenadasPedidas)
        assertEquals(Clima(17.3, 62.0), viewModel.clima)
        assertEquals("Santiago", viewModel.lugar)
        paso("CORRECTO", "el clima de Santiago se cargó")
    }

    @Test
    fun `cargar clima con otra ubicacion cambia las coordenadas y el lugar`() = runTest {
        val viewModel = ClimaViewModel(repositorio)
        advanceUntilIdle()

        viewModel.cargarClima(-36.82, -73.05, "tu ubicación")
        advanceUntilIdle()

        assertEquals(Pair(-36.82, -73.05), repositorio.coordenadasPedidas.last())
        assertEquals("tu ubicación", viewModel.lugar)
        assertEquals(Clima(17.3, 62.0), viewModel.clima)
    }

    @Test
    fun `si el clima falla no se muestra la tarjeta`() = runTest {
        repositorio.resultado = Result.failure(IOException("sin red"))

        val viewModel = ClimaViewModel(repositorio)
        advanceUntilIdle()

        assertNull(viewModel.clima)
    }
}