package com.duoc.maderita.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.maderita.Usuario
import com.duoc.maderita.repository.EscaneoRepositorio
import com.duoc.maderita.repository.MaderaRepositorio
import com.duoc.maderita.repository.UsuarioRepositorio
import com.duoc.maderita.repository.mensajeParaUsuario
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Un escaneo del historial, listo para mostrarse en el perfil
data class EscaneoItem(val id: Int, val maderaId: Int, val nombreMadera: String, val confianza: Double)

// Perfil del usuario: datos de la cuenta, edición, cierre de sesión e historial de escaneos
class PerfilViewModel(
    private val usuarios: UsuarioRepositorio,
    private val escaneos: EscaneoRepositorio,
    private val maderas: MaderaRepositorio
) : ViewModel() {

    val usuario: StateFlow<Usuario?> = usuarios.usuario
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    var historial by mutableStateOf<List<EscaneoItem>>(emptyList())
        private set

    var cargando by mutableStateOf(false)
        private set

    var error by mutableStateOf("")
        private set

    // GET escaneo: trae el historial y le pone a cada escaneo el nombre de su madera
    fun cargarHistorial() {
        viewModelScope.launch {
            val resultado = escaneos.listar()
            val catalogo = maderas.maderas.first()
            historial = resultado.getOrNull().orEmpty().map { escaneo ->
                EscaneoItem(
                    id = escaneo.id,
                    maderaId = escaneo.maderaId,
                    nombreMadera = catalogo.firstOrNull { it.id == escaneo.maderaId }?.nombre
                        ?: "Madera #${escaneo.maderaId}",
                    confianza = escaneo.confianza
                )
            }
            error = resultado.exceptionOrNull()
                ?.mensajeParaUsuario("No se pudo cargar el historial") ?: ""
        }
    }

    // DELETE escaneo/{id}
    fun eliminarEscaneo(id: Int) {
        viewModelScope.launch {
            val resultado = escaneos.eliminar(id)
            if (resultado.isSuccess) {
                historial = historial.filter { it.id != id }
                error = ""
            } else {
                error = resultado.exceptionOrNull()
                    ?.mensajeParaUsuario("No se pudo eliminar el escaneo") ?: ""
            }
        }
    }

    // PATCH user/{id}: guarda los cambios y avisa a la pantalla cuando terminó bien
    fun guardarCambios(nombreCompleto: String, telefono: String, correo: String, alTerminar: () -> Unit) {
        if (cargando) return
        cargando = true
        error = ""
        viewModelScope.launch {
            val resultado = usuarios.actualizarPerfil(nombreCompleto, telefono, correo)
            cargando = false
            if (resultado.isSuccess) {
                alTerminar()
            } else {
                error = resultado.exceptionOrNull()
                    ?.mensajeParaUsuario("No se pudieron guardar los cambios") ?: ""
            }
        }
    }

    fun cerrarSesion(alTerminar: () -> Unit) {
        viewModelScope.launch {
            usuarios.cerrarSesion()
            alTerminar()
        }
    }
}
