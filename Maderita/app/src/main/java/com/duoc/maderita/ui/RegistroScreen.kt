package com.duoc.maderita.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.duoc.maderita.viewmodel.RegistroViewModel

@Composable
fun RegistroScreen(
    navController: NavController,
    registroViewModel: RegistroViewModel = viewModel()
) {
    // Indica si el menú de perfiles está abierto o cerrado
    var menuPerfilAbierto by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TituloVista("REGISTRO USUARIO")

        CampoTexto(
            valor = registroViewModel.nombre,
            etiqueta = "Nombre completo",
            error = registroViewModel.errorNombre,
            alCambiar = { registroViewModel.cambiarNombre(it) }
        )
        CampoTexto(
            valor = registroViewModel.correo,
            etiqueta = "Correo electrónico",
            error = registroViewModel.errorCorreo,
            alCambiar = { registroViewModel.cambiarCorreo(it) }
        )
        CampoTexto(
            valor = registroViewModel.contrasena,
            etiqueta = "Contraseña",
            error = registroViewModel.errorContrasena,
            alCambiar = { registroViewModel.cambiarContrasena(it) },
            esContrasena = true
        )
        CampoTexto(
            valor = registroViewModel.confirmacion,
            etiqueta = "Confirmar contraseña",
            error = registroViewModel.errorConfirmacion,
            alCambiar = { registroViewModel.cambiarConfirmacion(it) },
            esContrasena = true
        )

        // Menú desplegable de perfiles: un botón que abre la lista de opciones
        Box {
            OutlinedButton(
                onClick = { menuPerfilAbierto = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (registroViewModel.perfil == "") "Selecciona tu perfil" else registroViewModel.perfil)
            }
            DropdownMenu(
                expanded = menuPerfilAbierto,
                onDismissRequest = { menuPerfilAbierto = false }
            ) {
                registroViewModel.listaPerfiles.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            registroViewModel.cambiarPerfil(opcion)
                            menuPerfilAbierto = false
                        }
                    )
                }
            }
        }
        if (registroViewModel.errorPerfil != "") {
            Text(text = registroViewModel.errorPerfil, color = MaterialTheme.colorScheme.error)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = registroViewModel.terminosAceptados,
                onCheckedChange = { registroViewModel.cambiarTerminos(it) }
            )
            Text("Acepto los términos de uso de la aplicación")
        }
        if (registroViewModel.errorTerminos != "") {
            Text(text = registroViewModel.errorTerminos, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = { registroViewModel.registrar() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }

        TextButton(onClick = { navController.popBackStack() }) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }

    // Modal que aparece cuando todos los campos son válidos
    if (registroViewModel.registroExitoso) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Registro exitoso") },
            text = { Text("Bienvenido/a, ${registroViewModel.nombre}") },
            confirmButton = {
                Button(
                    onClick = {
                        registroViewModel.limpiarFormulario()
                        navController.popBackStack()   // vuelve al inicio de sesión
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}