package com.duoc.maderita.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.duoc.maderita.model.PerfilUsuario
import com.duoc.maderita.model.ValidadorRegistro

class RegistroViewModel : ViewModel() {

    // Valores del formulario
    // "private set" evita que la vista los modifique directamente
    var nombre by mutableStateOf("")
        private set
    var correo by mutableStateOf("")
        private set
    var contrasena by mutableStateOf("")
        private set
    var confirmacion by mutableStateOf("")
        private set
    var perfil by mutableStateOf<PerfilUsuario?>(null)
        private set
    var terminosAceptados by mutableStateOf(false)
        private set

    // Mensajes de error de cada campo (null significa que no hay error)
    var errorNombre by mutableStateOf<String?>(null)
        private set
    var errorCorreo by mutableStateOf<String?>(null)
        private set
    var errorContrasena by mutableStateOf<String?>(null)
        private set
    var errorConfirmacion by mutableStateOf<String?>(null)
        private set
    var errorPerfil by mutableStateOf<String?>(null)
        private set
    var errorTerminos by mutableStateOf<String?>(null)
        private set

    // Indica si hay que mostrar el modal de registro exitoso
    var registroExitoso by mutableStateOf(false)
        private set

    // Al editar un campo se borra su error anterior
    fun onNombreChange(valor: String) {
        nombre = valor
        errorNombre = null
    }

    fun onCorreoChange(valor: String) {
        correo = valor
        errorCorreo = null
    }

    fun onContrasenaChange(valor: String) {
        contrasena = valor
        errorContrasena = null
    }

    fun onConfirmacionChange(valor: String) {
        confirmacion = valor
        errorConfirmacion = null
    }

    fun onPerfilChange(valor: PerfilUsuario) {
        perfil = valor
        errorPerfil = null
    }

    fun onTerminosChange(valor: Boolean) {
        terminosAceptados = valor
        errorTerminos = null
    }

    /** Valida todos los campos; si ninguno tiene error, el registro es exitoso. */
    fun onRegistrarClick() {
        errorNombre = ValidadorRegistro.validarNombre(nombre)
        errorCorreo = ValidadorRegistro.validarCorreo(correo)
        errorContrasena = ValidadorRegistro.validarContrasena(contrasena)
        errorConfirmacion = ValidadorRegistro.validarConfirmacion(contrasena, confirmacion)
        errorPerfil = ValidadorRegistro.validarPerfil(perfil)
        errorTerminos = ValidadorRegistro.validarTerminos(terminosAceptados)

        registroExitoso = errorNombre == null && errorCorreo == null &&
                errorContrasena == null && errorConfirmacion == null &&
                errorPerfil == null && errorTerminos == null
    }

    /** Se llama al cerrar el modal: deja el formulario vacío. */
    fun onRegistroConfirmado() {
        nombre = ""
        correo = ""
        contrasena = ""
        confirmacion = ""
        perfil = null
        terminosAceptados = false
        registroExitoso = false
    }
}