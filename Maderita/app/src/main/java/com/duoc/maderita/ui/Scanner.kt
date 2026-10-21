package com.duoc.maderita.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.*
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.duoc.maderita.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Scanner(navController: NavController) {
    var escanearEstado by remember { mutableStateOf(false) }
    var mostrarExito by remember { mutableStateOf(false) }
    var mostrarError by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val transition = rememberInfiniteTransition(label = "scanLine")
    val offsetY by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lineY"
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
            Text(
                text = "Escanear Madera",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .border(3.dp, Color(0xFF4CAF50), RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.maderita),
                    contentDescription = "Madera a escanear",
                    modifier = Modifier.size(160.dp)
                )

                // Línea verde animada si está escaneando
                if (escanearEstado) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val lineY = size.height * offsetY
                        drawLine(
                            color = Color(0xFF00FF66),
                            start = Offset(0f, lineY),
                            end = Offset(size.width, lineY),
                            strokeWidth = 6f
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    escanearEstado = true
                    scope.launch {
                        delay(2000) // Simula 2 segundos de lectura
                        escanearEstado = false
                        // Para probar éxito o error, simulamos éxito por defecto:
                        mostrarExito = true
                    }
                },
                enabled = !escanearEstado,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                if (escanearEstado) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text("Escanear", fontSize = 18.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón secundario para probar el Pop-Up de Error (según tu dibujo)
            TextButton(onClick = { mostrarError = true }) {
                Text("Simular Error de Lectura", color = Color.LightGray)
            }
        }
        if (mostrarExito) {
            AlertDialog(
                onDismissRequest = { mostrarExito = false },
                title = { Text("¡Escaneo Exitoso!", fontWeight = FontWeight.Bold) },
                text = { Text("Esta es la Madera #1 (Trepén)") },
                confirmButton = {
                    Button(
                        onClick = {
                            mostrarExito = false
                            navController.navigate("detalleMadera") // Va a la ficha técnica
                        }
                    ) {
                        Text("Ver Ficha Madera #1")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarExito = false }) {
                        Text("OK")
                    }
                }
            )
        }
        if (mostrarError) {
            AlertDialog(
                onDismissRequest = { mostrarError = false },
                title = { Text("Error de Lectura", color = MaterialTheme.colorScheme.error) },
                text = { Text("No se pudo detectar el tipo de madera. Intenta enfocar nuevamente.") },
                confirmButton = {
                    Button(onClick = { mostrarError = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}