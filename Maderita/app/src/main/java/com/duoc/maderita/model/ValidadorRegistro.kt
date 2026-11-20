package com.duoc.maderita.model

/**
 * Reglas de validación del formulario de registro.
 * Cada función devuelve el mensaje de error, o "" si el valor es correcto.
 */
object ValidadorRegistro {

    // Formato básico de correo: texto@dominio.extensión
    private val formatoCorreo = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun validarNombre(nombre: String): String {
        if (nombre.isBlank()) return "El nombre es obligatorio"
        if (nombre.length < 3) return "Debe tener al menos 3 caracteres"
        if (nombre.any { it.isDigit() }) return "No puede contener números"
        return ""
    }

    fun validarCorreo(correo: String): String {
        if (correo.isBlank()) return "El correo es obligatorio"
        if (!formatoCorreo.matches(correo)) return "Ingresa un correo válido"
        return ""
    }

    fun validarContrasena(contrasena: String): String {
        if (contrasena.isEmpty()) return "La contraseña es obligatoria"
        if (contrasena.length < 8) return "Debe tener al menos 8 caracteres"
        if (!contrasena.any { it.isDigit() }) return "Debe incluir al menos un número"
        return ""
    }

    fun validarConfirmacion(contrasena: String, confirmacion: String): String {
        if (confirmacion != contrasena) return "Las contraseñas no coinciden"
        return ""
    }

    fun validarPerfil(perfil: String): String {
        if (perfil == "") return "Selecciona un perfil"
        return ""
    }

    fun validarTerminos(aceptados: Boolean): String {
        if (!aceptados) return "Debes aceptar los términos para continuar"
        return ""
    }
}