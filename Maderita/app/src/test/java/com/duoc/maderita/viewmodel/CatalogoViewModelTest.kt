package com.duoc.maderita.viewmodel

import com.duoc.maderita.MaderaRepositorioFalso
import com.duoc.maderita.model.Madera
import com.duoc.maderita.paso
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogoViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val pino = Madera(6, "Pino Radiata", 0, "Pinus radiata", "Introducida", "Blanda", "Liviana", listOf("Muebles"))
    private val repositorio = MaderaRepositorioFalso(listOf(pino))

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    @Test
    fun `al abrirse pide el catalogo a Xano y publica las maderas guardadas`() = runTest {
        paso("PREPARACION", "el repositorio falso tiene el Pino Radiata y Xano responde bien")

        paso("ACCION", "crear el ViewModel y suscribirse a las maderas")
        val viewModel = CatalogoViewModel(repositorio)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.maderas.collect { } }
        advanceUntilIdle()

        paso("COMPROBACION", "se pidió el catálogo una vez y la lista tiene el Pino")
        assertEquals(1, repositorio.llamadasRefrescar)
        assertEquals(listOf(pino), viewModel.maderas.value)
        assertEquals("", viewModel.error)
        assertFalse(viewModel.cargando)
        paso("CORRECTO", "el catálogo se cargó")
    }

    @Test
    fun `si Xano falla se avisa pero siguen las maderas guardadas`() = runTest {
        repositorio.resultadoRefrescar = Result.failure(IOException("sin red"))

        val viewModel = CatalogoViewModel(repositorio)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.maderas.collect { } }
        advanceUntilIdle()

        assertEquals("Sin conexión. Revisa tu internet e intenta de nuevo", viewModel.error)
        assertEquals(listOf(pino), viewModel.maderas.value)
    }

    @Test
    fun `reintentar vuelve a pedir el catalogo`() = runTest {
        repositorio.resultadoRefrescar = Result.failure(IOException("sin red"))
        val viewModel = CatalogoViewModel(repositorio)
        advanceUntilIdle()

        repositorio.resultadoRefrescar = Result.success(Unit)
        viewModel.refrescar()
        advanceUntilIdle()

        assertEquals(2, repositorio.llamadasRefrescar)
        assertEquals("", viewModel.error)
    }
}
