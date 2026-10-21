package com.duoc.maderita

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.duoc.maderita.ui.Navegacion
import com.duoc.maderita.ui.RegistroScreen
import com.duoc.maderita.ui.theme.MaderitaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaderitaTheme {
                Scaffold { paddingInterno ->
                    Box(modifier = Modifier.padding(paddingInterno)) {
                        Navegacion()
                    }
                }
            }
        }
    }
}