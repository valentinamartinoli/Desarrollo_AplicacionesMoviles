package com.duoc.maderita.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.maderita.repository.UsuarioRepositorio
import com.duoc.maderita.repository.mensajeParaUsuario
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginViewModel(private val usuarios: UsuarioRepositorio) : ViewModel() {

    var correo by mutableStateOf("")
    var contrasena by mutableStateOf("")

    // Mensaje de error de cada campo ("" significa que no hay error)
    var errorCorreo by mutableStateOf("")
    var errorContrasena by mutableStateOf("")

    // Error que viene de Xano o de la conexión
    var errorGeneral by mutableStateOf("")

    var cargando by mutableStateOf(false)

    // Cuando es true, la pantalla muestra el modal de inicio de sesión exitoso
    var ingresoExitoso by mutableStateOf(false)
    var nombreBienvenida by mutableStateOf("")

    fun cambiarCorreo(valor: String) {
        correo = valor
        errorCorreo = ""
        errorGeneral = ""
    }

    fun cambiarContrasena(valor: String) {
        contrasena = valor
        errorContrasena = ""
        errorGeneral = ""
    }

    // Revisa que los campos no estén vacíos. Devuelve true si se puede ingresar.
    fun validarIngreso(): Boolean {
        errorCorreo = if (correo.isBlank()) "Ingresa tu correo" else ""
        errorContrasena = if (contrasena.isBlank()) "Ingresa tu contraseña" else ""
        return errorCorreo == "" && errorContrasena == ""
    }

    // Valida los campos, inicia sesión en Xano y guarda la sesión en Room
    fun ingresar() {
        if (cargando || !validarIngreso()) return
        cargando = true
        errorGeneral = ""
        viewModelScope.launch {
            val resultado = usuarios.iniciarSesion(correo, contrasena)
            if (resultado.isSuccess) {
                nombreBienvenida = usuarios.usuario.first()?.nombre.orEmpty()
                ingresoExitoso = true
            } else {
                errorGeneral = resultado.exceptionOrNull()
                    ?.mensajeParaUsuario("Correo o contraseña incorrectos")
                    ?: "Ocurrió un error inesperado"
            }
            cargando = false
        }
    }
}
