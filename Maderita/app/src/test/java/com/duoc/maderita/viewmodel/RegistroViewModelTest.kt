package com.duoc.maderita.viewmodel

import com.duoc.maderita.UsuarioRepositorioFalso
import com.duoc.maderita.errorHttp
import com.duoc.maderita.paso
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
class RegistroViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private lateinit var repositorio: UsuarioRepositorioFalso
    private lateinit var viewModel: RegistroViewModel

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
        repositorio = UsuarioRepositorioFalso()
        viewModel = RegistroViewModel(repositorio)
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    private fun llenarFormularioValido() {
        viewModel.cambiarNombre("Valentina Martinoli")
        viewModel.cambiarCorreo("v@maderita.cl")
        viewModel.cambiarContrasena("clave1234")
        viewModel.cambiarConfirmacion("clave1234")
        viewModel.cambiarPerfil("Estudiante de pre o postgrado")
        viewModel.cambiarTerminos(true)
    }

    @Test
    fun `formulario vacio muestra todos los errores y no llama a Xano`() = runTest {
        paso("INICIO", "registrar con el formulario vacío")
        viewModel.registrar()
        advanceUntilIdle()

        paso("COMPROBACION", "cada campo obligatorio tiene su error")
        assertEquals("El nombre es obligatorio", viewModel.errorNombre)
        assertEquals("El correo es obligatorio", viewModel.errorCorreo)
        assertEquals("La contraseña es obligatoria", viewModel.errorContrasena)
        assertEquals("Selecciona un perfil", viewModel.errorPerfil)
        assertEquals("Debes aceptar los términos para continuar", viewModel.errorTerminos)
        assertTrue(repositorio.llamadasRegistro.isEmpty())
        assertFalse(viewModel.registroExitoso)
    }

    @Test
    fun `formulario valido crea la cuenta en Xano`() = runTest {
        paso("PREPARACION", "llenar el formulario con datos correctos")
        llenarFormularioValido()

        paso("ACCION", "presionar Registrarse")
        viewModel.registrar()
        advanceUntilIdle()

        paso("COMPROBACION", "se envió nombre, correo y contraseña, y se muestra el modal")
        assertEquals(
            listOf(Triple("Valentina Martinoli", "v@maderita.cl", "clave1234")),
            repositorio.llamadasRegistro
        )
        assertEquals(viewModel.perfil, repositorio.ultimoPerfilRegistrado)
        assertTrue(viewModel.registroExitoso)
        assertFalse(viewModel.cargando)
        paso("CORRECTO", "el registro terminó bien")
    }

    @Test
    fun `si Xano rechaza el registro se muestra el error y no hay modal`() = runTest {
        repositorio.resultadoRegistro = Result.failure(errorHttp(403))
        llenarFormularioValido()

        viewModel.registrar()
        advanceUntilIdle()

        assertEquals(
            "No se pudo registrar. Es posible que el correo ya esté en uso",
            viewModel.errorGeneral
        )
        assertFalse(viewModel.registroExitoso)
    }

    @Test
    fun `limpiar formulario deja todo vacio`() = runTest {
        llenarFormularioValido()
        viewModel.registrar()
        advanceUntilIdle()

        viewModel.limpiarFormulario()

        assertEquals("", viewModel.nombre)
        assertEquals("", viewModel.correo)
        assertEquals("", viewModel.perfil)
        assertFalse(viewModel.terminosAceptados)
        assertFalse(viewModel.registroExitoso)
    }
}
