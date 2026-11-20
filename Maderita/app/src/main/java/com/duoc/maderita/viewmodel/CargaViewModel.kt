package com.duoc.maderita.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.maderita.repository.UsuarioRepositorio
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Pantalla de carga: mientras se muestra el logo, revisa si hay una sesión guardada
class CargaViewModel(
    private val usuarios: UsuarioRepositorio,
    private val duracionMs: Long = 3000
) : ViewModel() {
    private val _listo = MutableStateFlow(false)
    val listo = _listo.asStateFlow()

    // Pantalla a la que se entra al terminar de cargar: "home" si hay sesión, si no "login"
    var destinoInicial = "login"
        private set

    init {
        viewModelScope.launch {
            destinoInicial = if (usuarios.haySesionActiva()) "home" else "login"
            delay(duracionMs)
            _listo.value = true
        }
    }
}
