package com.duoc.maderita.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CargaViewModel : ViewModel(){
    private val _listo = MutableStateFlow(false)
    val listo = _listo.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1500)
            _listo.value = true
        }
    }
}