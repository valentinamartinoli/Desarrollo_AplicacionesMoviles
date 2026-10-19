package com.duoc.maderita.ui

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.duoc.maderita.repository.UsuarioRepositorio

@Composable
fun AgregarUsuarios() {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var nacionalidad by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    val usuarios by remember { derivedStateOf { UsuarioRepositorio.usuarios } }


    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Formulario
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = apellido,
            onValueChange = { apellido = it },
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = nacionalidad,
            onValueChange = { nacionalidad = it },
            label = { Text("Nacionalidad") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                UsuarioRepositorio.crearNuevoUsuario(nombre, apellido, nacionalidad, telefono, correo)
                nombre = ""
                apellido = ""
                nacionalidad = ""
                telefono = ""
                correo = ""
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Guardar Usuario")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tabla de usuarios
        Text("Usuarios Registrados", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Nombre", modifier = Modifier.weight(1f))
                    Text("Apellido", modifier = Modifier.weight(1f))
                    Text("Correo", modifier = Modifier.weight(1f))
                }
                Divider()
            }
            items(usuarios) { usuario ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(usuario.nombre, modifier = Modifier.weight(1f))
                    Text(usuario.apellido, modifier = Modifier.weight(1f))
                    Text(usuario.correo, modifier = Modifier.weight(1f))
                }
                Divider()
            }
        }
    }
}