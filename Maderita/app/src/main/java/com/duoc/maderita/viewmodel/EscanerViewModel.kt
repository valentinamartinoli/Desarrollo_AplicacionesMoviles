package com.duoc.maderita.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.maderita.model.Madera
import com.duoc.maderita.repository.EscaneoRepositorio
import com.duoc.maderita.repository.MaderaRepositorio
import com.duoc.maderita.repository.mensajeParaUsuario
import kotlin.random.Random
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// Escáner: identifica una madera a partir de la foto y guarda el escaneo en Xano
class EscanerViewModel(
    private val maderas: MaderaRepositorio,
    private val escaneos: EscaneoRepositorio,
    private val azar: Random = Random.Default
) : ViewModel() {

    var maderaIdentificada by mutableStateOf<Madera?>(null)
        private set

    // Porcentaje de coincidencia entre 0.70 y 0.99
    var confianza by mutableStateOf(0.0)
        private set

    var error by mutableStateOf("")
        private set

    // La identificación es simulada: se elige una madera del catálogo al azar
    fun procesarFoto(foto: ByteArray) {
        viewModelScope.launch {
            val elegida = maderas.maderas.first().randomOrNull(azar)
            if (elegida == null) {
                error = "El catálogo aún no se ha cargado. Intenta de nuevo"
                return@launch
            }
            val coincidencia = (70 + azar.nextInt(30)) / 100.0
            maderaIdentificada = elegida
            confianza = coincidencia
            error = escaneos.crear(elegida.id, coincidencia, "Escaneo desde la app", foto)
                .exceptionOrNull()
                ?.mensajeParaUsuario("No se pudo guardar el escaneo en tu historial")
                ?: ""
        }
    }

    fun reiniciar() {
        maderaIdentificada = null
        error = ""
    }
}
