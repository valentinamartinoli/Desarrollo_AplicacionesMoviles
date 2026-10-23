package com.duoc.maderita.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.duoc.maderita.repository.UsuarioRepositorio

@Composable
fun ListaUsuarios(navController: NavController) {
    val usuarios = UsuarioRepositorio.obtenerUsuarios()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TituloVista("USUARIOS REGISTRADOS")

        if (usuarios.isEmpty()) {
            Text("Aún no hay usuarios registrados.")
        } else {
            // Lista que muestra una tarjeta por cada usuario guardado
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(usuarios) { usuario ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("${usuario.nombre} ${usuario.apellido}", fontWeight = FontWeight.Bold)
                            Text(usuario.correo)
                            Text("Teléfono: ${usuario.telefono}")
                            Text("Nacionalidad: ${usuario.nacionalidad}")
                        }
                    }
                }
            }
        }

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}