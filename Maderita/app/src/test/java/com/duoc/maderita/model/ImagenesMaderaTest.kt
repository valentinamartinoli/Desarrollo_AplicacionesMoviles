package com.duoc.maderita.model

import org.junit.Assert.assertEquals
import org.junit.Test

class ImagenesMaderaTest {

    @Test
    fun `cada madera del catalogo tiene su foto`() {
        assertEquals("maderapino", ImagenesMadera.nombreRecurso("Pino Radiata"))
        assertEquals("maderacoigue", ImagenesMadera.nombreRecurso("Coigüe/Coihue"))
        assertEquals("maderarauli", ImagenesMadera.nombreRecurso("Raulí"))
    }

    @Test
    fun `ignora mayusculas y espacios sobrantes`() {
        assertEquals("maderaulmo", ImagenesMadera.nombreRecurso("  ULMO "))
    }

    @Test
    fun `una madera desconocida usa la imagen por defecto`() {
        assertEquals(ImagenesMadera.POR_DEFECTO, ImagenesMadera.nombreRecurso("Alerce"))
    }
}
