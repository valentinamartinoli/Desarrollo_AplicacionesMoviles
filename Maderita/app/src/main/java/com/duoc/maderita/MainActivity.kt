package com.duoc.maderita

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.duoc.maderita.repository.SesionRepositorio
import com.duoc.maderita.ui.Navegacion
import com.duoc.maderita.ui.PantallaCarga
import com.duoc.maderita.ui.theme.MaderitaTheme
import com.duoc.maderita.viewmodel.CargaViewModel

class MainActivity : ComponentActivity() {
    private val cargaViewModel: CargaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Abre el archivo donde se guarda la sesión
        SesionRepositorio.iniciar(applicationContext)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Si el usuario no cerró sesión, la app abre directo en el Home
        var destinoInicial = "login"
        if (SesionRepositorio.haySesionActiva()) {
            destinoInicial = "home"
        }

        setContent {
            MaderitaTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val listo by cargaViewModel.listo.collectAsState()

                    // Mientras carga se muestra la pantalla de carga, después lleva a la app normal
                    if (listo){
                        Navegacion(destinoInicial = destinoInicial)
                    } else {
                        PantallaCarga()
                    }

                }
            }
        }
    }
}