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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.duoc.maderita.viewmodel.EscanerViewModel
import com.duoc.maderita.viewmodel.FabricaViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File

private val FondoCrema = Color(0xFFF0F1E5)
private val VerdePalido = Color(0xFFC2CFB0)
private val VerdeBoton = Color(0xFF7EAA6E)
private val TextoOscuro = Color(0xFF1F4D36)

// Reduce la foto y la convierte a JPEG para enviarla a Xano sin gastar tantos datos
private fun Bitmap.aJpeg(ladoMaximo: Int = 1024): ByteArray {
    val escala = ladoMaximo.toFloat() / maxOf(width, height)
    val reducida = if (escala < 1f) {
        Bitmap.createScaledBitmap(this, (width * escala).toInt(), (height * escala).toInt(), true)
    } else {
        this
    }
    val salida = ByteArrayOutputStream()
    reducida.compress(Bitmap.CompressFormat.JPEG, 80, salida)
    return salida.toByteArray()
}

@Composable
fun Scanner(
    navController: NavController,
    escanerViewModel: EscanerViewModel = viewModel(factory = FabricaViewModel.Fabrica)
) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var escanearEstado by remember { mutableStateOf(false) }
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
                    // Identifica la madera y guarda el escaneo en Xano (POST escaneo)
                    bitmap?.let { escanerViewModel.procesarFoto(it.aJpeg()) }
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
        }

        val maderaIdentificada = escanerViewModel.maderaIdentificada
        if (maderaIdentificada != null) {
            AlertDialog(
                onDismissRequest = { escanerViewModel.reiniciar() },
                shape = RoundedCornerShape(20.dp),
                containerColor = VerdePalido,
                title = { Text("¡Escaneo Exitoso!", fontWeight = FontWeight.Bold, color = TextoOscuro) },
                text = {
                    Column {
                        Text(
                            "Esta es: ${maderaIdentificada.nombre} " +
                                    "(${(escanerViewModel.confianza * 100).toInt()}% de coincidencia)",
                            color = TextoOscuro
                        )
                        // Si no se pudo guardar en Xano, se avisa pero igual se puede ver la ficha
                        if (escanerViewModel.error != "") {
                            Text(escanerViewModel.error, color = MaterialTheme.colorScheme.error)
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            escanerViewModel.reiniciar()
                            navController.navigate("detalleMadera/${maderaIdentificada.id}")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = VerdeBoton)
                    ) {
                        Text("Ver Ficha", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { escanerViewModel.reiniciar() }) {
                        Text("OK", color = TextoOscuro)
                    }
                }
            )
        }

        // Error de la cámara, o de la app cuando todavía no hay catálogo para identificar la madera
        if (mostrarError || (maderaIdentificada == null && escanerViewModel.error != "")) {
            AlertDialog(
                onDismissRequest = {
                    mostrarError = false
                    escanerViewModel.reiniciar()
                },
                shape = RoundedCornerShape(20.dp),
                containerColor = VerdePalido,
                title = { Text("Error de Lectura", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold) },
                text = {
                    Text(
                        escanerViewModel.error.ifEmpty { "No se pudo tomar la foto o se denegó el permiso de la cámara." },
                        color = TextoOscuro
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            mostrarError = false
                            escanerViewModel.reiniciar()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("OK", color = Color.White)
                    }
                }
            )
        }
    }
}