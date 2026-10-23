package com.duoc.maderita.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.duoc.maderita.model.ValidadorRegistro

class RegistroViewModel : ViewModel() {

    // Opciones del menú de perfiles (los perfiles definidos en el caso CENAMAD)
    val listaPerfiles = listOf(
        "Investigador/a",
        "Estudiante de pre o postgrado",
        "Industria forestal o de la construcción",
        "Institución pública",
        "Entidad financiadora"
    )

    // Lo que escribe el usuario en el formulario
    var nombre by mutableStateOf("")
    var correo by mutableStateOf("")
    var contrasena by mutableStateOf("")
    var confirmacion by mutableStateOf("")
    var perfil by mutableStateOf("")
    var terminosAceptados by mutableStateOf(false)

    // Mensaje de error de cada campo ("" significa que no hay error)
    var errorNombre by mutableStateOf("")
    var errorCorreo by mutableStateOf("")
    var errorContrasena by mutableStateOf("")
    var errorConfirmacion by mutableStateOf("")
    var errorPerfil by mutableStateOf("")
    var errorTerminos by mutableStateOf("")

    // Cuando es true, la pantalla muestra el modal de registro exitoso
    var registroExitoso by mutableStateOf(false)

    // Cada función guarda el nuevo valor y borra el error anterior de ese campo
    fun cambiarNombre(valor: String) {
        nombre = valor
        errorNombre = ""
    }

    fun cambiarCorreo(valor: String) {
        correo = valor
        errorCorreo = ""
    }

    fun cambiarContrasena(valor: String) {
        contrasena = valor
        errorContrasena = ""
    }

    fun cambiarConfirmacion(valor: String) {
        confirmacion = valor
        errorConfirmacion = ""
    }

    fun cambiarPerfil(valor: String) {
        perfil = valor
        errorPerfil = ""
    }

    fun cambiarTerminos(valor: Boolean) {
        terminosAceptados = valor
        errorTerminos = ""
    }

    // Revisa todos los campos. Si ninguno tiene error, el registro es exitoso
    fun registrar() {
        errorNombre = ValidadorRegistro.validarNombre(nombre)
        errorCorreo = ValidadorRegistro.validarCorreo(correo)
        errorContrasena = ValidadorRegistro.validarContrasena(contrasena)
        errorConfirmacion = ValidadorRegistro.validarConfirmacion(contrasena, confirmacion)
        errorPerfil = ValidadorRegistro.validarPerfil(perfil)
        errorTerminos = ValidadorRegistro.validarTerminos(terminosAceptados)

        registroExitoso = errorNombre == "" && errorCorreo == "" &&
                errorContrasena == "" && errorConfirmacion == "" &&
                errorPerfil == "" && errorTerminos == ""
    }

    // Deja el formulario vacío después de un registro exitoso
    fun limpiarFormulario() {
        nombre = ""
        correo = ""
        contrasena = ""
        confirmacion = ""
        perfil = ""
        terminosAceptados = false
        registroExitoso = false
    }
}