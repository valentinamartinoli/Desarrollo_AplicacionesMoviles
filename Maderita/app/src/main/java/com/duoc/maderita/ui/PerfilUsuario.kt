package com.duoc.maderita.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.duoc.maderita.R
import com.duoc.maderita.ui.theme.*
import com.duoc.maderita.viewmodel.FabricaViewModel
import com.duoc.maderita.viewmodel.PerfilViewModel

@Composable
fun PerfilUsuario(
    navController: NavController,
    perfilViewModel: PerfilViewModel = viewModel(factory = FabricaViewModel.Fabrica)
) {
    val usuario by perfilViewModel.usuario.collectAsState()

    // Cada vez que se abre el perfil se pide el historial de escaneos a Xano
    LaunchedEffect(Unit) { perfilViewModel.cargarHistorial() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoCrema)
            .systemBarsPadding()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                text = "Perfil Usuario",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextoOscuro,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = VerdePalido
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_splash),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .border(2.dp, VerdeBoton, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "${usuario?.nombre.orEmpty()} ${usuario?.apellido.orEmpty()}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TextoOscuro
                        )
                        Text(
                            text = usuario?.correo.orEmpty(),
                            fontSize = 14.sp,
                            color = TextoOscuro.copy(alpha = 0.8f)
                        )
                        Text(
                            text = usuario?.telefono.orEmpty(),
                            fontSize = 14.sp,
                            color = TextoOscuro.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { navController.navigate("editarPerfil") },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VerdeBoton
                        )
                    ) {
                        Text("Editar", color = Color.White)
                    }
                    OutlinedButton(
                        onClick = {
                            // Borra la sesión guardada para que la app abra en el login la próxima vez
                            perfilViewModel.cerrarSesion {
                                navController.navigate("login") {
                                    popUpTo(0)
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = TextoOscuro
                        )
                    ) {
                        Text("Cerrar Sesión")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Maderas Escaneadas",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextoOscuro
        )

        if (perfilViewModel.error != "") {
            Text(text = perfilViewModel.error, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(perfilViewModel.historial, key = { it.id }) { escaneo ->
                Card(
                    onClick = { navController.navigate("detalleMadera/${escaneo.maderaId}") },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = VerdePalido.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.maderita),
                            contentDescription = escaneo.nombreMadera,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = escaneo.nombreMadera,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextoOscuro
                        )
                        Text(
                            text = "${(escaneo.confianza * 100).toInt()}% de coincidencia",
                            fontSize = 11.sp,
                            color = TextoOscuro.copy(alpha = 0.8f)
                        )
                        // DELETE escaneo/{id}
                        IconButton(onClick = { perfilViewModel.eliminarEscaneo(escaneo.id) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar escaneo",
                                tint = TextoOscuro
                            )
                        }
                    }
                }
            }
        }
    }
}