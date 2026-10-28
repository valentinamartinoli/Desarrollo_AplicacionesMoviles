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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.duoc.maderita.R
import com.duoc.maderita.repository.UsuarioRepositorio
import com.duoc.maderita.ui.theme.*

@Composable
fun PerfilUsuario(navController: NavController) {
    val usuario = UsuarioRepositorio.usuarioActual
    val maderasGuardadas = listOf("Trepén", "Pino", "Roble", "Raulí", "Coigüe", "Alerce")

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
                            text = "${usuario.nombre} ${usuario.apellido}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TextoOscuro
                        )
                        Text(
                            text = usuario.correo,
                            fontSize = 14.sp,
                            color = TextoOscuro.copy(alpha = 0.8f)
                        )
                        Text(
                            text = usuario.telefono,
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
                            navController.navigate("login") {
                                popUpTo(0)
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

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(maderasGuardadas) { madera ->
                Card(
                    onClick = { navController.navigate("detalleMadera") },
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
                            contentDescription = madera,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = madera,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextoOscuro
                        )
                    }
                }
            }
        }
    }
}