package com.duoc.maderita.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.maderita.model.Madera
import com.duoc.maderita.repository.MaderaRepositorio
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Ficha de una madera: se muestra la copia de Room y se actualiza con GET /madera/{id}
class DetalleMaderaViewModel(
    private val repositorio: MaderaRepositorio,
    id: Int
) : ViewModel() {

    val madera: StateFlow<Madera?> = repositorio.madera(id)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        viewModelScope.launch { repositorio.refrescarUna(id) }
    }
}
