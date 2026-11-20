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

    // Nombre del lugar que se muestra en la tarjeta
    var lugar by mutableStateOf("Santiago")
        private set

    // Al abrir el Home se muestra el clima de Santiago
    init {
        cargarClima(-33.45, -70.66, "Santiago")
    }

    // Pide el clima de unas coordenadas y guarda el nombre del lugar
    fun cargarClima(latitud: Double, longitud: Double, nombreLugar: String) {
        viewModelScope.launch {
            clima = repositorio.obtenerClima(latitud, longitud).getOrNull()
            lugar = nombreLugar
        }
    }
}