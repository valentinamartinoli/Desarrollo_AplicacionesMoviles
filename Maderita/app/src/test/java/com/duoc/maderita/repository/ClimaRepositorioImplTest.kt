package com.duoc.maderita.repository

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

// La API externa (Open-Meteo) también se prueba con un servidor local
class ClimaRepositorioImplTest {
    private lateinit var servidor: MockWebServer
    private lateinit var repositorio: ClimaRepositorioImpl

    @Before
    fun preparar() {
        servidor = MockWebServer()
        servidor.start()
        repositorio = ClimaRepositorioImpl(ClienteRetrofit.crearClimaApi(servidor.url("/").toString()))
    }

    @After
    fun limpiar() {
        servidor.shutdown()
    }

    @Test
    fun `convierte la respuesta de Open-Meteo en temperatura y humedad`() = runTest {
        paso("PREPARACION", "el servidor local responderá 17.3 grados y 62% de humedad")
        servidor.enqueue(MockResponse().setBody("""{"current":{"temperature_2m":17.3,"relative_humidity_2m":62.0}}"""))

        paso("ACCION", "pedir el clima")
        val resultado = repositorio.obtenerClima()

        paso("COMPROBACION", "se consultó la coordenada de Santiago y el clima salió bien convertido")
        assertEquals(Clima(17.3, 62.0), resultado.getOrNull())
        val ruta = servidor.takeRequest().path!!
        assertTrue(ruta.startsWith("/v1/forecast"))
        assertTrue(ruta.contains("latitude=-33.45"))
        assertTrue(ruta.contains("longitude=-70.66"))
        assertTrue(java.net.URLDecoder.decode(ruta, "UTF-8").contains("current=temperature_2m,relative_humidity_2m"))
    }

    @Test
    fun `una respuesta incompleta es un fallo`() = runTest {
        servidor.enqueue(MockResponse().setBody("""{"current":{"temperature_2m":17.3}}"""))

        assertTrue(repositorio.obtenerClima().isFailure)
    }

    @Test
    fun `un error del servidor es un fallo`() = runTest {
        servidor.enqueue(MockResponse().setResponseCode(500))

        assertTrue(repositorio.obtenerClima().isFailure)
    }
}
