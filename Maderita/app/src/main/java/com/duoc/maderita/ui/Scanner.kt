package com.duoc.maderita.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

private val FondoCrema = Color(0xFFF0F1E5)
private val VerdePalido = Color(0xFFC2CFB0)
private val VerdeBoton = Color(0xFF7EAA6E)
private val TextoOscuro = Color(0xFF1F4D36)

@Composable
fun Scanner(navController: NavController) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var escanearEstado by remember { mutableStateOf(false) }
    var mostrarExito by remember { mutableStateOf(false) }
    var mostrarError by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Genera el archivo temporal y obtiene su URI seguro
    fun prepararArchivoFoto(): Uri? {
        return try {
            val file = File.createTempFile("maderita_escaneo", ".jpg", context.cacheDir)
            file.deleteOnExit()
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
            imageUri = uri
            uri
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Launcher para tomar la foto nativa
    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && imageUri != null) {
            try {
                val inputStream = context.contentResolver.openInputStream(imageUri!!)
                val bmp = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                bitmap = bmp

                escanearEstado = true
                scope.launch {
                    delay(2000)
                    escanearEstado = false
                    mostrarExito = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                mostrarError = true
            }
        }
    }

    // Launcher para pedir permiso de cámara al usuario
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = prepararArchivoFoto()
            if (uri != null) {
                takePictureLauncher.launch(uri)
            } else {
                mostrarError = true
            }
        } else {
            mostrarError = true
        }
    }

    // Verifica el permiso antes de abrir la cámara
    fun solicitarOAbrirCamara() {
        val permissionCheck = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        )
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            val uri = prepararArchivoFoto()
            if (uri != null) {
                takePictureLauncher.launch(uri)
            } else {
                mostrarError = true
            }
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Animación de la línea verde de escaneo
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
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = "Foto capturada",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Cámara",
                            tint = TextoOscuro,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Presiona para tomar foto",
                            color = TextoOscuro,
                            fontSize = 14.sp
                        )
                    }
                }

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

            Button(
                onClick = { solicitarOAbrirCamara() },
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
                    Text(
                        text = if (bitmap == null) "Tomar Foto" else "Tomar Otra Foto",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = { mostrarError = true }) {
                Text("Simular Error de Lectura", color = TextoOscuro.copy(alpha = 0.6f))
            }
        }

        if (mostrarExito) {
            AlertDialog(
                onDismissRequest = { mostrarExito = false },
                shape = RoundedCornerShape(20.dp),
                containerColor = VerdePalido,
                title = { Text("¡Escaneo Exitoso!", fontWeight = FontWeight.Bold, color = TextoOscuro) },
                text = { Text("Esta es la Madera #1 (Avellano chileno)", color = TextoOscuro) },
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

        if (mostrarError) {
            AlertDialog(
                onDismissRequest = { mostrarError = false },
                shape = RoundedCornerShape(20.dp),
                containerColor = VerdePalido,
                title = { Text("Error de Lectura", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold) },
                text = { Text("No se pudo tomar la foto o se denegó el permiso de la cámara.", color = TextoOscuro) },
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