package com.duoc.maderita.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.duoc.maderita.R
import com.duoc.maderita.repository.UsuarioRepositorio

@Composable
fun EditarPerfil(navController: NavController) {
    val usuario = UsuarioRepositorio.usuarioActual

    var nombreCompleto by remember { mutableStateOf("${usuario.nombre} ${usuario.apellido}") }
    var telefono by remember { mutableStateOf(usuario.telefono) }
    var email by remember { mutableStateOf(usuario.correo) }
    var contrasena by remember { mutableStateOf("12345678") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = "Editar Perfil",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_maderita),
                contentDescription = "Avatar",
                modifier = Modifier.size(60.dp)
            )
        }

        TextButton(onClick = { navController.navigate("escaner") }) {
            Text("Cambiar foto (img.png)")
        }

        Spacer(modifier = Modifier.height(8.dp))

        CampoTexto(
            valor = nombreCompleto,
            etiqueta = "Nombre",
            alCambiar = { nombreCompleto = it }
        )
        CampoTexto(
            valor = telefono,
            etiqueta = "Teléfono",
            alCambiar = { telefono = it }
        )
        CampoTexto(
            valor = email,
            etiqueta = "Email",
            alCambiar = { email = it }
        )
        CampoTexto(
            valor = contrasena,
            etiqueta = "Contraseña",
            alCambiar = { contrasena = it },
            esContrasena = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                UsuarioRepositorio.actualizarUsuarioActual(
                    nombreCompleto = nombreCompleto,
                    telefono = telefono,
                    correo = email
                )
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Cambios")
        }
    }
}