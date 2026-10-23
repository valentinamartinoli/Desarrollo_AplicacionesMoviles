package com.duoc.maderita.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    var nombre by mutableStateOf("")
    var contrasena by mutableStateOf("")

    // Mensaje de error de cada campo ("" significa que no hay error)
    var errorNombre by mutableStateOf("")
    var errorContrasena by mutableStateOf("")

    fun cambiarNombre(valor: String) {
        nombre = valor
        errorNombre = ""
    }

    fun cambiarContrasena(valor: String) {
        contrasena = valor
        errorContrasena = ""
    }

    // Por indicación del profesor, solo se exige que los campos no estén vacíos.
    // Devuelve true si se puede ingresar.
    fun validarIngreso(): Boolean {
        errorNombre = if (nombre.isBlank()) "Ingresa tu nombre" else ""
        errorContrasena = if (contrasena.isBlank()) "Ingresa tu contraseña" else ""
        return errorNombre == "" && errorContrasena == ""
    }
}