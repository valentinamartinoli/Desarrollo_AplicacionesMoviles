package com.duoc.maderita.repository

import com.duoc.maderita.errorHttp
import java.io.IOException
import org.junit.Assert.assertEquals
import org.junit.Test

class ErroresTest {

    @Test
    fun `un error de red pide revisar la conexion`() {
        assertEquals(
            "Sin conexión. Revisa tu internet e intenta de nuevo",
            IOException().mensajeParaUsuario("x")
        )
    }

    @Test
    fun `un error http usa el mensaje de cada pantalla`() {
        assertEquals("Correo o contraseña incorrectos", errorHttp(403).mensajeParaUsuario("Correo o contraseña incorrectos"))
    }

    @Test
    fun `cualquier otro error muestra un mensaje generico`() {
        assertEquals("Ocurrió un error inesperado", IllegalStateException().mensajeParaUsuario("x"))
    }
}
