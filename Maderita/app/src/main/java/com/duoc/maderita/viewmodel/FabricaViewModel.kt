package com.duoc.maderita.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.duoc.maderita.MaderitaApp

private fun CreationExtras.app(): MaderitaApp =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MaderitaApp

// Entrega a cada ViewModel los repositorios que necesita
object FabricaViewModel {
    val Fabrica: ViewModelProvider.Factory = viewModelFactory {
        initializer { CargaViewModel(app().contenedor.usuarios) }
        initializer { LoginViewModel(app().contenedor.usuarios) }
        initializer { RegistroViewModel(app().contenedor.usuarios) }
        initializer { CatalogoViewModel(app().contenedor.maderas) }
        initializer { ClimaViewModel(app().contenedor.clima) }
        initializer {
            val c = app().contenedor
            PerfilViewModel(c.usuarios, c.escaneos, c.maderas)
        }
        initializer {
            val c = app().contenedor
            EscanerViewModel(c.maderas, c.escaneos)
        }
    }

    // La ficha necesita saber de qué madera es, por eso tiene su propia fábrica
    fun fabricaDetalle(id: Int): ViewModelProvider.Factory = viewModelFactory {
        initializer { DetalleMaderaViewModel(app().contenedor.maderas, id) }
    }
}
