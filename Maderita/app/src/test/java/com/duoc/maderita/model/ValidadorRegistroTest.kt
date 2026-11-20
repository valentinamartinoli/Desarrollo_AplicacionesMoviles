package com.duoc.maderita.model

import com.duoc.maderita.paso
import org.junit.Assert.assertEquals
import org.junit.Test

class ValidadorRegistroTest {

    @Test
    fun `nombre vacio, corto o con numeros es rechazado`() {
        paso("INICIO", "comprobar las reglas del nombre")
        assertEquals("El nombre es obligatorio", ValidadorRegistro.validarNombre("  "))
        assertEquals("Debe tener al menos 3 caracteres", ValidadorRegistro.validarNombre("Al"))
        assertEquals("No puede contener números", ValidadorRegistro.validarNombre("Ana1"))
        paso("CORRECTO", "los tres nombres inválidos devolvieron su mensaje")
    }

    @Test
    fun `nombre valido no devuelve error`() {
        assertEquals("", ValidadorRegistro.validarNombre("Valentina"))
    }

    @Test
    fun `correo vacio o sin formato es rechazado`() {
        assertEquals("El correo es obligatorio", ValidadorRegistro.validarCorreo(""))
        assertEquals("Ingresa un correo válido", ValidadorRegistro.validarCorreo("correo"))
        assertEquals("Ingresa un correo válido", ValidadorRegistro.validarCorreo("valentina@maderita"))
    }

    @Test
    fun `correo con formato correcto no devuelve error`() {
        assertEquals("", ValidadorRegistro.validarCorreo("valentina@maderita.cl"))
    }

    @Test
    fun `contrasena debe tener 8 caracteres y un numero`() {
        paso("INICIO", "comprobar las reglas de la contraseña")
        assertEquals("La contraseña es obligatoria", ValidadorRegistro.validarContrasena(""))
        assertEquals("Debe tener al menos 8 caracteres", ValidadorRegistro.validarContrasena("abc123"))
        assertEquals("Debe incluir al menos un número", ValidadorRegistro.validarContrasena("abcdefgh"))
        assertEquals("", ValidadorRegistro.validarContrasena("abcdefg1"))
        paso("CORRECTO", "solo la contraseña de 8 caracteres con número fue aceptada")
    }

    @Test
    fun `confirmacion debe ser igual a la contrasena`() {
        assertEquals("Las contraseñas no coinciden", ValidadorRegistro.validarConfirmacion("abcdefg1", "abcdefg2"))
        assertEquals("", ValidadorRegistro.validarConfirmacion("abcdefg1", "abcdefg1"))
    }

    @Test
    fun `perfil y terminos son obligatorios`() {
        assertEquals("Selecciona un perfil", ValidadorRegistro.validarPerfil(""))
        assertEquals("", ValidadorRegistro.validarPerfil("Investigador/a"))
        assertEquals("Debes aceptar los términos para continuar", ValidadorRegistro.validarTerminos(false))
        assertEquals("", ValidadorRegistro.validarTerminos(true))
    }
}
