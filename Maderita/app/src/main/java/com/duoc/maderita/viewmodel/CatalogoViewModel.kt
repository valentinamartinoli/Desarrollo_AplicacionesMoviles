package com.duoc.maderita.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.maderita.model.Madera
import com.duoc.maderita.repository.MaderaRepositorio
import com.duoc.maderita.repository.mensajeParaUsuario
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Catálogo de maderas para el Home y la lista de tipos de madera
class CatalogoViewModel(private val repositorio: MaderaRepositorio) : ViewModel() {

    val maderas: StateFlow<List<Madera>> = repositorio.maderas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    var cargando by mutableStateOf(false)
    var error by mutableStateOf("")

    init {
        refrescar()
    }

    // Vuelve a pedir el catálogo a Xano. Si falla, se siguen mostrando las maderas guardadas.
    fun refrescar() {
        if (cargando) return
        cargando = true
        error = ""
        viewModelScope.launch {
            val resultado = repositorio.refrescar()
            error = resultado.exceptionOrNull()
                ?.mensajeParaUsuario("No se pudo cargar el catálogo")
                ?: ""
            cargando = false
        }
    }
}
