package com.duoc.maderita.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.duoc.maderita.Usuario

object UsuarioRepositorio {
    private var _usuarios by mutableStateOf(listOf<Usuario>())
    val usuarios: List<Usuario> get() = _usuarios
    private var nextId = 1

    // Usuario activo para mostrar en la pantalla de Perfil
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

    fun crearNuevoUsuario(
        nombre: String,
        apellido: String,
        nacionalidad: String,
        telefono: String,
        correo: String
    ): Usuario {
        val usuario = Usuario(
            id = nextId++,
            nombre = nombre,
            apellido = apellido,
            nacionalidad = nacionalidad,
            telefono = telefono,
            correo = correo
        )
        _usuarios = _usuarios + usuario
        return usuario
    }

    fun obtenerUsuarios(): List<Usuario> = usuarios

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