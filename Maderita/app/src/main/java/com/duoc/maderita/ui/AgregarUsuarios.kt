package com.duoc.maderita.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.duoc.maderita.repository.UsuarioRepositorio

@Composable
fun AgregarUsuarios(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var nacionalidad by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TituloVista("REGISTRO DATOS")

        CampoTexto(valor = nombre, etiqueta = "Nombre", alCambiar = { nombre = it })
        CampoTexto(valor = apellido, etiqueta = "Apellido", alCambiar = { apellido = it })
        CampoTexto(valor = nacionalidad, etiqueta = "Nacionalidad", alCambiar = { nacionalidad = it })
        CampoTexto(valor = telefono, etiqueta = "Teléfono", alCambiar = { telefono = it })
        CampoTexto(valor = correo, etiqueta = "Correo electrónico", alCambiar = { correo = it })

        Button(
            onClick = {
                UsuarioRepositorio.crearNuevoUsuario(nombre, apellido, nacionalidad, telefono, correo)
                nombre = ""
                apellido = ""
                nacionalidad = ""
                telefono = ""
                correo = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar usuario")
        }

        OutlinedButton(
            onClick = { navController.navigate("listaUsuarios") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver registro usuarios")
        }
    }
}