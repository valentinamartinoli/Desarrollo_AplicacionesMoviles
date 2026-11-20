package com.duoc.maderita.viewmodel

import com.duoc.maderita.UsuarioRepositorioFalso
import com.duoc.maderita.paso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CargaViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val repositorio = UsuarioRepositorioFalso()

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    @Test
    fun `la pantalla de carga dura tres segundos`() = runTest {
        paso("INICIO", "crear el ViewModel de carga")
        val viewModel = CargaViewModel(repositorio)

        advanceTimeBy(2999)
        runCurrent()
        paso("COMPROBACION", "a los 2999 ms todavía está cargando")
        assertFalse(viewModel.listo.value)

        advanceTimeBy(1)
        runCurrent()
        assertTrue(viewModel.listo.value)
        paso("CORRECTO", "a los 3000 ms terminó de cargar")
    }

    @Test
    fun `sin sesion guardada entra al login`() = runTest {
        repositorio.sesionActiva = false
        val viewModel = CargaViewModel(repositorio)
        advanceUntilIdle()
        assertEquals("login", viewModel.destinoInicial)
    }

    @Test
    fun `con sesion guardada entra directo al home`() = runTest {
        repositorio.sesionActiva = true
        val viewModel = CargaViewModel(repositorio)
        advanceUntilIdle()
        assertEquals("home", viewModel.destinoInicial)
    }
}
