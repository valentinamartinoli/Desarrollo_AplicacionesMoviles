package com.duoc.maderita

import android.os.*
import androidx.activity.*
import androidx.compose.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import com.duoc.maderita.ui.theme.MaderitaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaderitaTheme {
               var nombre by remember { mutableStateOf("") }
                var modal by remember { mutableStateOf(false) }
                var terminos by remember { mutableStateOf(false) }
                fun abrirModal(){ modal = true }
                fun cerrarModal(){ modal = false }
                var juegoSeleccionado by remember { mutableStateOf(0) }
                val juegos = listOf("God of War", "FIFA", "Kenshi", "Minecraf")
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    juegos.forEachIndexed { index, opcion ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = juegoSeleccionado == index,
                                onClick = { juegoSeleccionado = index }
                            )
                            Text(opcion)
                        }
                    }
                    Checkbox(
                        checked = terminos,
                        onCheckedChange = { terminos = it },
                    )
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Ingresa nombre") }
                    )
                    Button(onClick={abrirModal()}) {
                        Text(text="Enviar")
                    }
                }
                if(modal) {
                    AlertDialog(
                        onDismissRequest = { },
                        title = { Text("Confirmación") },
                        text = { Text("Formulario enviado correctamente") },
                        confirmButton = {
                            Button(onClick = {cerrarModal()}) { Text("OK") }
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MaderitaTheme {
        Greeting("Android")
    }
}