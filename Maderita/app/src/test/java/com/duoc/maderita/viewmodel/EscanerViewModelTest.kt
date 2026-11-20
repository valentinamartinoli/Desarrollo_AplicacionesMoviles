package com.duoc.maderita.viewmodel

import com.duoc.maderita.EscaneoRepositorioFalso
import com.duoc.maderita.MaderaRepositorioFalso
import com.duoc.maderita.errorHttp
import com.duoc.maderita.model.Madera
import com.duoc.maderita.paso
import kotlin.random.Random
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EscanerViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val escaneos = EscaneoRepositorioFalso()
    private val foto = byteArrayOf(1, 2, 3)
    private val catalogo = listOf(
        Madera(1, "Lenga", 0, "Nothofagus pumilio", "Nativa", "Semidura", "", emptyList()),
        Madera(2, "Tepa", 0, "Laureliopsis philippiana", "Nativa", "Blanda", "", emptyList())
    )

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    @Test
    fun `una foto identifica una madera del catalogo y se guarda en Xano`() = runTest {
        paso("PREPARACION", "el catálogo tiene dos maderas y Xano aceptará el escaneo")
        val viewModel = EscanerViewModel(MaderaRepositorioFalso(catalogo), escaneos, Random(7))

        paso("ACCION", "procesar la foto tomada")
        viewModel.procesarFoto(foto)
        advanceUntilIdle()

        paso("COMPROBACION", "la madera sale del catálogo, la coincidencia está entre 70% y 99% y se hizo el POST")
        val identificada = viewModel.maderaIdentificada
        assertNotNull(identificada)
        assertTrue(catalogo.any { it == identificada })
        assertTrue(viewModel.confianza in 0.70..0.99)
        assertEquals("", viewModel.error)
        assertEquals(1, escaneos.creados.size)
        assertEquals(identificada!!.id, escaneos.creados[0].maderaId)
        assertEquals(viewModel.confianza, escaneos.creados[0].confianza, 0.0)
        assertArrayEquals(foto, escaneos.creados[0].foto)
        paso("CORRECTO", "el escaneo quedó guardado")
    }

    @Test
    fun `si Xano falla igual se muestra la madera y se avisa del error`() = runTest {
        escaneos.resultadoCrear = Result.failure(errorHttp(500))
        val viewModel = EscanerViewModel(MaderaRepositorioFalso(catalogo), escaneos, Random(1))

        viewModel.procesarFoto(foto)
        advanceUntilIdle()

        assertNotNull(viewModel.maderaIdentificada)
        assertEquals("No se pudo guardar el escaneo en tu historial", viewModel.error)
    }

    @Test
    fun `sin catalogo no se identifica nada ni se llama a Xano`() = runTest {
        val viewModel = EscanerViewModel(MaderaRepositorioFalso(emptyList()), escaneos)

        viewModel.procesarFoto(foto)
        advanceUntilIdle()

        assertNull(viewModel.maderaIdentificada)
        assertEquals("El catálogo aún no se ha cargado. Intenta de nuevo", viewModel.error)
        assertTrue(escaneos.creados.isEmpty())
    }

    @Test
    fun `reiniciar limpia el resultado`() = runTest {
        val viewModel = EscanerViewModel(MaderaRepositorioFalso(catalogo), escaneos)
        viewModel.procesarFoto(foto)
        advanceUntilIdle()

        viewModel.reiniciar()

        assertNull(viewModel.maderaIdentificada)
        assertEquals("", viewModel.error)
    }
}
