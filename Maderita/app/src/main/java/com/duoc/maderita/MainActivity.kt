package com.duoc.maderita

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.duoc.maderita.ui.Navegacion
import com.duoc.maderita.ui.theme.MaderitaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Se inicializa la Splash Screen antes de super.onCreate
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaderitaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navegacion()
                }
            }
        }
    }
}