package com.duoc.maderita.repository

import com.duoc.maderita.MaderaDaoFalso
import com.duoc.maderita.local.MaderaEntity
import com.duoc.maderita.paso
import com.duoc.maderita.remote.ClienteRetrofit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

// Prueba el repositorio con Room falso y un servidor local que imita a Xano (no se usa internet)
class MaderaRepositorioImplTest {
    private lateinit var servidor: MockWebServer
    private lateinit var dao: MaderaDaoFalso
    private lateinit var repositorio: MaderaRepositorioImpl

    private val jsonPino = """{"id":11,"created_at":1795147826754,"nombre":"Pino Radiata","especie":"Pinus radiata",
        "descripcion":"Liviana y económica.","densidad":0,"dureza":"Blanda","origen":"Introducida",
        "usos":"Construcción|Tableros|Muebles","imagen_recursos":"","imagen":{"access":"public","path":"","name":"","type":"","size":0,"mime":"","meta":{}}}"""

    private val jsonRoble = """{"id":12,"nombre":"Roble","especie":"Nothofagus obliqua","descripcion":"Noble.",
        "dureza":"Dura","origen":"Nativa","usos":"Pisos|Postes"}"""

    @Before
    fun preparar() {
        servidor = MockWebServer()
        servidor.start()
        dao = MaderaDaoFalso()
        // La foto se resuelve con una función simple: el id de la imagen es el largo de su nombre
        repositorio = MaderaRepositorioImpl(
            api = ClienteRetrofit.crearMaderaApi(servidor.url("/api:TEST/").toString()),
            dao = dao,
            resolverImagen = { nombre -> nombre.length }
        )
    }

    @After
    fun limpiar() {
        servidor.shutdown()
    }

    @Test
    fun `refrescar consulta GET madera y guarda las maderas en Room`() = runTest {
        paso("INICIO", "comprobar que el catálogo de Xano se guarda en Room")
        paso("PREPARACION", "el servidor local responderá con el Pino y el Roble")
        servidor.enqueue(MockResponse().setBody("[$jsonPino,$jsonRoble]"))

        paso("ACCION", "refrescar el catálogo")
        val resultado = repositorio.refrescar()

        paso("COMPROBACION", "se usó GET /madera y las dos maderas quedaron convertidas")
        assertTrue(resultado.isSuccess)
        val solicitud = servidor.takeRequest()
        assertEquals("GET", solicitud.method)
        assertEquals("/api:TEST/madera", solicitud.path)

        val maderas = repositorio.maderas.first()
        assertEquals(listOf(11, 12), maderas.map { it.id })
        assertEquals("Pinus radiata", maderas[0].nombreCientifico)
        assertEquals(listOf("Construcción", "Tableros", "Muebles"), maderas[0].usos)
        assertEquals("maderapino".length, maderas[0].imagen)
        paso("CORRECTO", "el catálogo quedó guardado")
    }

    @Test
    fun `refrescar reemplaza lo que habia antes en Room`() = runTest {
        dao.guardarTodas(listOf(MaderaEntity(1, "Antigua", "", "", "", "", "")))
        servidor.enqueue(MockResponse().setBody("[$jsonPino]"))

        repositorio.refrescar()

        assertEquals(listOf(11), dao.guardadas.map { it.id })
    }

    @Test
    fun `si Xano responde error se conserva lo guardado`() = runTest {
        dao.guardarTodas(listOf(MaderaEntity(1, "Lenga", "", "", "", "", "")))
        servidor.enqueue(MockResponse().setResponseCode(500))

        val resultado = repositorio.refrescar()

        assertTrue(resultado.isFailure)
        assertEquals(listOf(1), dao.guardadas.map { it.id })
    }

    @Test
    fun `si Xano responde una lista vacia no se borra el catalogo`() = runTest {
        dao.guardarTodas(listOf(MaderaEntity(1, "Lenga", "", "", "", "", "")))
        servidor.enqueue(MockResponse().setBody("[]"))

        val resultado = repositorio.refrescar()

        assertTrue(resultado.isSuccess)
        assertEquals(listOf(1), dao.guardadas.map { it.id })
    }

    @Test
    fun `refrescarUna consulta GET madera por id y la guarda`() = runTest {
        servidor.enqueue(MockResponse().setBody(jsonRoble))

        val resultado = repositorio.refrescarUna(12)

        assertTrue(resultado.isSuccess)
        assertEquals("/api:TEST/madera/12", servidor.takeRequest().path)
        assertEquals("Roble", repositorio.madera(12).first()?.nombre)
    }

    @Test
    fun `una madera que no existe en Room entrega null`() = runTest {
        assertEquals(null, repositorio.madera(999).first())
    }
}
