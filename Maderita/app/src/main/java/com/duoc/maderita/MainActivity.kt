package com.duoc.maderita

import android.os.*
import androidx.activity.*
import androidx.activity.compose.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.duoc.maderita.ui.theme.MaderitaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaderitaTheme {
                var nombre by remember { mutableStateOf("") }
                var modal by remember { mutableStateOf(false) }

                fun abrirModal() {
                    modal = true;
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id=R.drawable.maderita),
                        contentDescription = "Img Login",
                        modifier = Modifier.size(150.dp)
                    )
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Ingresa nombre") }
                    )
                    Button(onClick = { abrirModal() }) {
                        Text(text = "Enviar")
                    }
                }

            }
        }
    }
}