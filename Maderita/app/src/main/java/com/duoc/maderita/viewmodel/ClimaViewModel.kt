package com.duoc.maderita.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.maderita.repository.Clima
import com.duoc.maderita.repository.ClimaRepositorio
import kotlinx.coroutines.launch

// Clima del día (API externa). Si falla, el Home simplemente no muestra la tarjeta.
class ClimaViewModel(private val repositorio: ClimaRepositorio) : ViewModel() {

    var clima by mutableStateOf<Clima?>(null)
        private set

    init {
        viewModelScope.launch {
            clima = repositorio.obtenerClima().getOrNull()
        }
    }
}
