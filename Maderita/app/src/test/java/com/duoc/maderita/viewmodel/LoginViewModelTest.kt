package com.duoc.maderita.viewmodel

import com.duoc.maderita.Usuario
import com.duoc.maderita.UsuarioRepositorioFalso
import com.duoc.maderita.errorHttp
import com.duoc.maderita.paso
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private lateinit var repositorio: UsuarioRepositorioFalso
    private lateinit var viewModel: LoginViewModel

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
        repositorio = UsuarioRepositorioFalso()
        viewModel = LoginViewModel(repositorio)
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    @Test
    fun `campos vacios muestran error y no llaman a Xano`() = runTest {
        paso("INICIO", "intentar ingresar sin escribir nada")
        viewModel.ingresar()
        advanceUntilIdle()

        paso("COMPROBACION", "hay un error por campo y el repositorio no fue llamado")
        assertEquals("Ingresa tu correo", viewModel.errorCorreo)
        assertEquals("Ingresa tu contraseña", viewModel.errorContrasena)
        assertTrue(repositorio.llamadasLogin.isEmpty())
        assertFalse(viewModel.ingresoExitoso)
    }

    @Test
    fun `escribir en un campo borra su error`() {
        viewModel.validarIngreso()
        viewModel.cambiarCorreo("a@b.cl")
        assertEquals("", viewModel.errorCorreo)
        assertEquals("Ingresa tu contraseña", viewModel.errorContrasena)
    }

    @Test
    fun `inicio de sesion correcto abre el modal de bienvenida`() = runTest {
        paso("PREPARACION", "Xano aceptará las credenciales y el usuario se llama Valentina")
        repositorio.usuarioActual.value = Usuario(1, "Valentina", "", "", "", "v@maderita.cl")
        viewModel.cambiarCorreo("v@maderita.cl")
        viewModel.cambiarContrasena("clave1234")

        paso("ACCION", "presionar Iniciar sesión")
        viewModel.ingresar()
        assertTrue(viewModel.cargando)
        advanceUntilIdle()

        paso("COMPROBACION", "se llamó al repositorio con los datos y se muestra el modal")
        assertEquals(listOf(Pair("v@maderita.cl", "clave1234")), repositorio.llamadasLogin)
        assertTrue(viewModel.ingresoExitoso)
        assertEquals("Valentina", viewModel.nombreBienvenida)
        assertFalse(viewModel.cargando)
        paso("CORRECTO", "el login terminó bien")
    }

    @Test
    fun `credenciales incorrectas muestran un mensaje`() = runTest {
        repositorio.resultadoLogin = Result.failure(errorHttp(403))
        viewModel.cambiarCorreo("v@maderita.cl")
        viewModel.cambiarContrasena("mala")

        viewModel.ingresar()
        advanceUntilIdle()

        assertEquals("Correo o contraseña incorrectos", viewModel.errorGeneral)
        assertFalse(viewModel.ingresoExitoso)
        assertFalse(viewModel.cargando)
    }

    @Test
    fun `sin internet se avisa de la conexion`() = runTest {
        repositorio.resultadoLogin = Result.failure(IOException("sin red"))
        viewModel.cambiarCorreo("v@maderita.cl")
        viewModel.cambiarContrasena("clave1234")

        viewModel.ingresar()
        advanceUntilIdle()

        assertEquals("Sin conexión. Revisa tu internet e intenta de nuevo", viewModel.errorGeneral)
    }
}
