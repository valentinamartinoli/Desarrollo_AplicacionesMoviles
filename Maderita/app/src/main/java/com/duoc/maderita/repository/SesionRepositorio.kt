package com.duoc.maderita.repository

import android.content.Context
import android.content.SharedPreferences
import com.duoc.maderita.Usuario

// Guarda en el celular al usuario que inició sesión.
// Los datos siguen ahí aunque se cierre la app.
object SesionRepositorio {

    // Archivo del celular donde se guardan los datos. Se abre al iniciar la app
    private lateinit var preferencias: SharedPreferences

    // Se llama una sola vez al abrir la app: abre el archivo y carga el nombre guardado
    fun iniciar(context: Context) {
        preferencias = context.getSharedPreferences("sesion", Context.MODE_PRIVATE)
        val nombreGuardado = preferencias.getString("nombre", "")
        if (nombreGuardado != null) {
            mostrarUsuario(nombreGuardado)
        }
    }

    // Devuelve true si el usuario no cerró sesión la última vez
    fun haySesionActiva(): Boolean {
        return preferencias.getBoolean("sesionActiva", false)
    }

    // Guarda el usuario y la contraseña, y deja la sesión iniciada
    fun guardarSesion(nombre: String, contrasena: String) {
        val editor = preferencias.edit()
        editor.putString("nombre", nombre)
        editor.putString("contrasena", contrasena)
        editor.putBoolean("sesionActiva", true)
        editor.apply()
        mostrarUsuario(nombre)
    }

    // Cierra la sesión: la próxima vez la app abrirá en el login
    fun cerrarSesion() {
        val editor = preferencias.edit()
        editor.putBoolean("sesionActiva", false)
        editor.apply()
    }

    // Deja el nombre del login como usuario actual, que es el que muestran el Home y el Perfil
    private fun mostrarUsuario(nombre: String) {
        UsuarioRepositorio.usuarioActual = Usuario(
            id = 1,
            nombre = nombre,
            apellido = "",
            nacionalidad = "",
            telefono = "",
            correo = ""
        )
    }
}