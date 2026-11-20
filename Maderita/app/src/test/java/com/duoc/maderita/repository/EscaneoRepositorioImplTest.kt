package com.duoc.maderita.repository

import com.duoc.maderita.UsuarioDaoFalso
import com.duoc.maderita.local.UsuarioEntity
import com.duoc.maderita.paso
import com.duoc.maderita.remote.ClienteRetrofit
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class EscaneoRepositorioImplTest {
    private lateinit var servidor: MockWebServer
    private lateinit var dao: UsuarioDaoFalso
    private lateinit var repositorio: EscaneoRepositorioImpl

    @Before
    fun preparar() = runTest {
        servidor = MockWebServer()
        servidor.start()
        dao = UsuarioDaoFalso()
        dao.guardar(UsuarioEntity(7, "Valentina", "Martinoli", "", "", "v@maderita.cl", "TOKEN_GUARDADO", true))
        repositorio = EscaneoRepositorioImpl(
            api = ClienteRetrofit.crearEscaneoApi(servidor.url("/api:TEST/").toString()),
            usuarios = dao
        )
    }

    @After
    fun limpiar() {
        servidor.shutdown()
    }

    @Test
    fun `crear envia la foto y los datos a POST escaneo como formulario`() = runTest {
        paso("INICIO", "comprobar que un escaneo se envía a Xano con su foto")
        paso("PREPARACION", "Xano aceptará el escaneo")
        servidor.enqueue(MockResponse().setBody("""{"id":1,"user_id":7,"madera_id":3,"confianza":0.91,"notas":"Escaneo"}"""))

        paso("ACCION", "guardar el escaneo de la madera 3 con la foto")
        val resultado = repositorio.crear(3, 0.91, "Escaneo", "FOTO".toByteArray())

        paso("COMPROBACION", "POST /escaneo con token y un formulario con usuario, madera, coincidencia y foto")
        assertTrue(resultado.isSuccess)
        val solicitud = servidor.takeRequest()
        assertEquals("POST", solicitud.method)
        assertEquals("/api:TEST/escaneo", solicitud.path)
        assertEquals("Bearer TOKEN_GUARDADO", solicitud.getHeader("Authorization"))
        assertTrue(solicitud.getHeader("Content-Type")!!.startsWith("multipart/form-data"))

        val cuerpo = solicitud.body.readUtf8()
        assertTrue(cuerpo.contains("name=\"user_id\""))
        assertTrue(cuerpo.contains("name=\"madera_id\""))
        assertTrue(cuerpo.contains("name=\"confianza\""))
        assertTrue(cuerpo.contains("0.91"))
        assertTrue(cuerpo.contains("name=\"imagen_escaneada\"; filename=\"escaneo.jpg\""))
        assertTrue(cuerpo.contains("FOTO"))
        paso("CORRECTO", "el escaneo se envió completo")
    }

    @Test
    fun `listar deja solo los escaneos del usuario con sesion`() = runTest {
        servidor.enqueue(
            MockResponse().setBody(
                """[{"id":1,"user_id":7,"madera_id":3,"confianza":0.9,"notas":"a"},
                    {"id":2,"user_id":8,"madera_id":4,"confianza":0.8,"notas":"b"},
                    {"id":3,"user_id":7,"madera_id":5,"confianza":0.7}]"""
            )
        )

        val resultado = repositorio.listar()

        val escaneos = resultado.getOrThrow()
        assertEquals("/api:TEST/escaneo", servidor.takeRequest().path)
        assertEquals(listOf(1, 3), escaneos.map { it.id })
        assertEquals(listOf(3, 5), escaneos.map { it.maderaId })
        // Si Xano no manda las notas, queda un texto vacío
        assertEquals("", escaneos[1].notas)
    }

    @Test
    fun `eliminar hace DELETE escaneo por id con el token`() = runTest {
        servidor.enqueue(MockResponse().setResponseCode(200))

        val resultado = repositorio.eliminar(5)

        assertTrue(resultado.isSuccess)
        val solicitud = servidor.takeRequest()
        assertEquals("DELETE", solicitud.method)
        assertEquals("/api:TEST/escaneo/5", solicitud.path)
        assertEquals("Bearer TOKEN_GUARDADO", solicitud.getHeader("Authorization"))
    }

    @Test
    fun `si Xano rechaza el token el resultado es un fallo`() = runTest {
        servidor.enqueue(MockResponse().setResponseCode(401))

        assertTrue(repositorio.listar().isFailure)
    }

    @Test
    fun `sin sesion no se llama a Xano`() = runTest {
        dao.cerrarSesiones()

        assertTrue(repositorio.listar().isFailure)
        assertTrue(repositorio.crear(1, 0.8, "", byteArrayOf(1)).isFailure)
        assertTrue(repositorio.eliminar(1).isFailure)
        assertEquals(0, servidor.requestCount)
    }
}
