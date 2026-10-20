package com.duoc.maderita.model

import android.util.Patterns

object ValidadorRegistro {

    fun validarNombre(nombre: String): String? {
        if (nombre.isBlank()) return "El nombre es obligatorio"
        if (nombre.length < 3) return "Debe tener al menos 3 caracteres"
        if (nombre.any { it.isDigit() }) return "No puede contener números"
        return null
    }

    fun validarCorreo(correo: String): String? {
        if (correo.isBlank()) return "El correo es obligatorio"
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) return "Ingresa un correo válido"
        return null
    }

    fun validarContrasena(contrasena: String): String? {
        if (contrasena.isEmpty()) return "La contraseña es obligatoria"
        if (contrasena.length < 8) return "Debe tener al menos 8 caracteres"
        if (!contrasena.any { it.isDigit() }) return "Debe incluir al menos un número"
        return null
    }

    fun validarConfirmacion(contrasena: String, confirmacion: String): String? {
        if (confirmacion != contrasena) return "Las contraseñas no coinciden"
        return null
    }

    fun validarPerfil(perfil: PerfilUsuario?): String? {
        if (perfil == null) return "Selecciona un perfil"
        return null
    }

    fun validarTerminos(aceptados: Boolean): String? {
        if (!aceptados) return "Debes aceptar los términos para continuar"
        return null
    }
}