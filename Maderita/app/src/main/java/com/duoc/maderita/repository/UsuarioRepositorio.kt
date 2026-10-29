package com.duoc.maderita.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.duoc.maderita.Usuario

object UsuarioRepositorio {

    // Usuario activo para mostrar en el Home y en el Perfil
    var usuarioActual by mutableStateOf(
        Usuario(
            id = 1,
            nombre = "Valentina",
            apellido = "Martinoli",
            nacionalidad = "Chilena",
            telefono = "+569 8765 4321",
            correo = "valentina@maderita.cl"
        )
    )

    fun actualizarUsuarioActual(nombreCompleto: String, telefono: String, correo: String) {
        val partes = nombreCompleto.trim().split(" ", limit = 2)
        val n = partes.getOrElse(0) { "" }
        val a = partes.getOrElse(1) { usuarioActual.apellido }
        usuarioActual = usuarioActual.copy(
            nombre = n,
            apellido = a,
            telefono = telefono,
            correo = correo
        )
    }
}