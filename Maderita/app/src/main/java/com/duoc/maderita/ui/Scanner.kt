package com.duoc.maderita.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.duoc.maderita.ui.theme.*
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
            .background(FondoCrema)
            .systemBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = TextoOscuro
                )
            }
            Text(
                text = "Escanear Madera",
                color = TextoOscuro,
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
                    .border(3.dp, VerdeBoton, RoundedCornerShape(16.dp))
                    .background(VerdePalido.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_splash),
                    contentDescription = "Tronco madera",
                    modifier = Modifier.size(160.dp)
                )

                if (escanearEstado) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val lineY = size.height * offsetY

                        drawLine(
                            color = VerdeBoton,
                            start = Offset(0f, lineY),
                            end = Offset(size.width, lineY),
                            strokeWidth = 6f
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            //boton del scanner
            Button(
                onClick = {
                    escanearEstado = true
                    scope.launch {
                        delay(2000)
                        escanearEstado = false
                        mostrarExito = true
                    }
                },
                enabled = !escanearEstado,
                colors = ButtonDefaults.buttonColors(containerColor = VerdeBoton),
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
//btn de simular error
            TextButton(onClick = { mostrarError = true }) {
                Text("Simular Error de Lectura", color = TextoOscuro.copy(alpha = 0.6f))
            }
        }
        //pop out de funcionóo
        if (mostrarExito) {
            AlertDialog(
                onDismissRequest = { mostrarExito = false },
                shape = RoundedCornerShape(20.dp),
                containerColor = VerdePalido,
                title = { Text("¡Escaneo Exitoso!", fontWeight = FontWeight.Bold, color = TextoOscuro) },
                text = { Text("Esta es la Madera #1 (Trepén)", color = TextoOscuro) },
                confirmButton = {
                    Button(
                        onClick = {
                            mostrarExito = false
                            navController.navigate("detalleMadera/1")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = VerdeBoton)
                    ) {
                        Text("Ver Ficha Madera #1", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarExito = false }) {
                        Text("OK", color = TextoOscuro)
                    }
                }
            )
        }
        //Pop out error
        if (mostrarError) {
            AlertDialog(
                onDismissRequest = { mostrarError = false },
                shape = RoundedCornerShape(20.dp),
                containerColor = VerdePalido,
                title = { Text("Error de Lectura", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold) },
                text = { Text("No se pudo detectar el tipo de madera. Intenta enfocar nuevamente.", color = TextoOscuro) },
                confirmButton = {
                    Button(
                        onClick = { mostrarError = false },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("OK", color = Color.White)
                    }
                }
            )
        }
    }
}