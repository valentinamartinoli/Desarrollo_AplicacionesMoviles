package com.duoc.maderita.viewmodel

import com.duoc.maderita.EscaneoRepositorioFalso
import com.duoc.maderita.MaderaRepositorioFalso
import com.duoc.maderita.UsuarioRepositorioFalso
import com.duoc.maderita.errorHttp
import com.duoc.maderita.model.Madera
import com.duoc.maderita.paso
import com.duoc.maderita.repository.Escaneo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PerfilViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val usuarios = UsuarioRepositorioFalso()
    private val escaneos = EscaneoRepositorioFalso()
    private val roble = Madera(8, "Roble", 0, "Nothofagus obliqua", "Nativa", "Dura", "Noble", emptyList())
    private val maderas = MaderaRepositorioFalso(listOf(roble))

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    private fun crearViewModel() = PerfilViewModel(usuarios, escaneos, maderas)

    @Test
    fun `el historial muestra el nombre de la madera de cada escaneo`() = runTest {
        paso("PREPARACION", "Xano devuelve un escaneo del Roble (8) y otro de una madera que no está en el catálogo")
        escaneos.escaneos = Result.success(listOf(Escaneo(1, 8, 0.91, ""), Escaneo(2, 99, 0.75, "")))

        paso("ACCION", "cargar el historial")
        val viewModel = crearViewModel()
        viewModel.cargarHistorial()
        advanceUntilIdle()

        paso("COMPROBACION", "el primero tiene nombre y el segundo usa 'Madera #99'")
        assertEquals(
            listOf(
                EscaneoItem(1, 8, "Roble", 0.91),
                EscaneoItem(2, 99, "Madera #99", 0.75)
            ),
            viewModel.historial
        )
    }

    @Test
    fun `si no se puede cargar el historial se muestra un error`() = runTest {
        escaneos.escaneos = Result.failure(errorHttp(500))
        val viewModel = crearViewModel()

        viewModel.cargarHistorial()
        advanceUntilIdle()

        assertEquals("No se pudo cargar el historial", viewModel.error)
        assertTrue(viewModel.historial.isEmpty())
    }

    @Test
    fun `eliminar un escaneo lo quita de la lista`() = runTest {
        escaneos.escaneos = Result.success(listOf(Escaneo(1, 8, 0.91, ""), Escaneo(2, 8, 0.80, "")))
        val viewModel = crearViewModel()
        viewModel.cargarHistorial()
        advanceUntilIdle()

        viewModel.eliminarEscaneo(1)
        advanceUntilIdle()

        assertEquals(listOf(1), escaneos.eliminados)
        assertEquals(listOf(2), viewModel.historial.map { it.id })
    }

    @Test
    fun `si Xano no elimina el escaneo se mantiene en la lista`() = runTest {
        escaneos.escaneos = Result.success(listOf(Escaneo(1, 8, 0.91, "")))
        escaneos.resultadoEliminar = Result.failure(errorHttp(500))
        val viewModel = crearViewModel()
        viewModel.cargarHistorial()
        advanceUntilIdle()

        viewModel.eliminarEscaneo(1)
        advanceUntilIdle()

        assertEquals(1, viewModel.historial.size)
        assertEquals("No se pudo eliminar el escaneo", viewModel.error)
    }

    @Test
    fun `guardar cambios avisa a la pantalla cuando Xano responde bien`() = runTest {
        var terminado = false
        val viewModel = crearViewModel()

        viewModel.guardarCambios("Valentina Martinoli", "+56912345678", "v@maderita.cl") { terminado = true }
        advanceUntilIdle()

        assertTrue(terminado)
        assertEquals("", viewModel.error)
    }

    @Test
    fun `guardar cambios con error muestra el mensaje y no avisa`() = runTest {
        usuarios.resultadoPerfil = Result.failure(errorHttp(400))
        var terminado = false
        val viewModel = crearViewModel()

        viewModel.guardarCambios("Valentina", "1", "v@maderita.cl") { terminado = true }
        advanceUntilIdle()

        assertFalse(terminado)
        assertEquals("No se pudieron guardar los cambios", viewModel.error)
    }

    @Test
    fun `cerrar sesion borra la sesion y luego navega`() = runTest {
        var navegado = false
        val viewModel = crearViewModel()

        viewModel.cerrarSesion { navegado = true }
        advanceUntilIdle()

        assertTrue(usuarios.sesionCerrada)
        assertTrue(navegado)
    }
}
