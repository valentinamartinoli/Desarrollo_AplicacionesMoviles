package com.duoc.maderita.repository

import com.duoc.maderita.UsuarioDaoFalso
import com.duoc.maderita.local.UsuarioEntity
import com.duoc.maderita.paso
import com.duoc.maderita.remote.ClienteRetrofit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UsuarioRepositorioImplTest {
    private lateinit var servidor: MockWebServer
    private lateinit var dao: UsuarioDaoFalso
    private lateinit var repositorio: UsuarioRepositorioImpl

    private val jsonUsuario = """{"id":7,"name":"Valentina","email":"v@maderita.cl",
        "apellido":"Martinoli","nacionalidad":"Chilena","telefono":"+5691111"}"""

    @Before
    fun preparar() {
        servidor = MockWebServer()
        servidor.start()
        dao = UsuarioDaoFalso()
        val url = servidor.url("/api:TEST/").toString()
        repositorio = UsuarioRepositorioImpl(
            api = ClienteRetrofit.crearAuthApi(url),
            perfilApi = ClienteRetrofit.crearPerfilApi(url),
            dao = dao
        )
    }

    @After
    fun limpiar() {
        servidor.shutdown()
    }

    private suspend fun dejarSesionIniciada() {
        dao.guardar(UsuarioEntity(7, "Valentina", "Martinoli", "Chilena", "+5691111", "v@maderita.cl", "TOKEN_GUARDADO", true))
    }

    @Test
    fun `iniciar sesion hace login, pide el perfil y guarda la sesion en Room`() = runTest {
        paso("INICIO", "comprobar el inicio de sesión contra Xano")
        paso("PREPARACION", "el servidor local entregará un token y luego el perfil")
        servidor.enqueue(MockResponse().setBody("""{"authToken":"TOKEN123"}"""))
        servidor.enqueue(MockResponse().setBody(jsonUsuario))

        paso("ACCION", "iniciar sesión")
        val resultado = repositorio.iniciarSesion(" v@maderita.cl ", "clave1234")

        paso("COMPROBACION", "POST auth/login, luego GET auth/me con el token, y la sesión queda en Room")
        assertTrue(resultado.isSuccess)
        val login = servidor.takeRequest()
        assertEquals("POST", login.method)
        assertEquals("/api:TEST/auth/login", login.path)
        val cuerpoLogin = login.body.readUtf8()
        assertTrue(cuerpoLogin.contains("\"email\":\"v@maderita.cl\""))
        assertTrue(cuerpoLogin.contains("\"password\":\"clave1234\""))

        val perfil = servidor.takeRequest()
        assertEquals("GET", perfil.method)
        assertEquals("/api:TEST/auth/me", perfil.path)
        assertEquals("Bearer TOKEN123", perfil.getHeader("Authorization"))

        assertTrue(repositorio.haySesionActiva())
        assertEquals("TOKEN123", dao.guardados.single().token)
        assertEquals("Valentina", repositorio.usuario.first()?.nombre)
        paso("CORRECTO", "la sesión quedó guardada")
    }

    @Test
    fun `credenciales incorrectas no guardan ninguna sesion`() = runTest {
        servidor.enqueue(MockResponse().setResponseCode(403))

        val resultado = repositorio.iniciarSesion("v@maderita.cl", "mala")

        assertTrue(resultado.isFailure)
        assertFalse(repositorio.haySesionActiva())
        assertTrue(dao.guardados.isEmpty())
    }

    @Test
    fun `registrar envia nombre, correo, contrasena y perfil a auth signup`() = runTest {
        servidor.enqueue(MockResponse().setBody("""{"authToken":"NUEVO"}"""))

        val resultado = repositorio.registrar(" Valentina Martinoli ", "v@maderita.cl", "clave1234", " Carpintero ")

        assertTrue(resultado.isSuccess)
        val solicitud = servidor.takeRequest()
        assertEquals("POST", solicitud.method)
        assertEquals("/api:TEST/auth/signup", solicitud.path)
        val cuerpo = solicitud.body.readUtf8()
        assertTrue(cuerpo.contains("\"name\":\"Valentina Martinoli\""))
        assertTrue(cuerpo.contains("\"email\":\"v@maderita.cl\""))
        assertTrue(cuerpo.contains("\"password\":\"clave1234\""))
        assertTrue(cuerpo.contains("\"perfil\":\"Carpintero\""))
        // Registrarse no inicia sesión: el usuario debe entrar por el login
        assertFalse(repositorio.haySesionActiva())
    }

    @Test
    fun `actualizar perfil hace PATCH user con el token y actualiza Room`() = runTest {
        paso("PREPARACION", "hay una sesión iniciada y Xano devolverá el usuario actualizado")
        dejarSesionIniciada()
        servidor.enqueue(
            MockResponse().setBody(
                """{"id":7,"name":"Valentina","email":"nuevo@maderita.cl","apellido":"Pérez Soto",
                    "nacionalidad":"Chilena","telefono":"+5692222"}"""
            )
        )

        paso("ACCION", "guardar el nombre completo, teléfono y correo nuevos")
        val resultado = repositorio.actualizarPerfil("Valentina Pérez Soto", " +5692222 ", "nuevo@maderita.cl")

        paso("COMPROBACION", "PATCH /user/7 con token, nombre y apellido separados, y Room actualizado")
        assertTrue(resultado.isSuccess)
        val solicitud = servidor.takeRequest()
        assertEquals("PATCH", solicitud.method)
        assertEquals("/api:TEST/user/7", solicitud.path)
        assertEquals("Bearer TOKEN_GUARDADO", solicitud.getHeader("Authorization"))
        val cuerpo = solicitud.body.readUtf8()
        assertTrue(cuerpo.contains("\"name\":\"Valentina\""))
        assertTrue(cuerpo.contains("\"apellido\":\"Pérez Soto\""))
        assertTrue(cuerpo.contains("\"telefono\":\"+5692222\""))
        assertTrue(cuerpo.contains("\"email\":\"nuevo@maderita.cl\""))

        val guardado = dao.guardados.single()
        assertEquals("nuevo@maderita.cl", guardado.correo)
        assertEquals("Pérez Soto", guardado.apellido)
        // El token se conserva para las siguientes llamadas
        assertEquals("TOKEN_GUARDADO", guardado.token)
        assertTrue(guardado.sesionActiva)
    }

    @Test
    fun `actualizar perfil sin sesion falla sin llamar a Xano`() = runTest {
        val resultado = repositorio.actualizarPerfil("Valentina", "1", "v@maderita.cl")

        assertTrue(resultado.isFailure)
        assertEquals(0, servidor.requestCount)
    }

    @Test
    fun `cerrar sesion deja la app sin sesion activa`() = runTest {
        dejarSesionIniciada()
        assertTrue(repositorio.haySesionActiva())

        repositorio.cerrarSesion()

        assertFalse(repositorio.haySesionActiva())
        assertEquals(null, repositorio.usuario.first())
    }

    @Test
    fun `dividir nombre separa nombre y apellido`() {
        assertEquals(Pair("Valentina", "Martinoli Soto"), dividirNombre("Valentina Martinoli Soto", "X"))
    }

    @Test
    fun `dividir nombre con una sola palabra mantiene el apellido actual`() {
        assertEquals(Pair("Valentina", "Martinoli"), dividirNombre(" Valentina ", "Martinoli"))
    }
}
