package com.duoc.maderita.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.maderita.model.PerfilUsuario
import com.duoc.maderita.viewmodel.RegistroViewModel

@Composable
fun RegistroScreen(registroViewModel: RegistroViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Crear cuenta", style = MaterialTheme.typography.headlineMedium)

        CampoTexto(
            valor = registroViewModel.nombre,
            etiqueta = "Nombre completo",
            mensajeError = registroViewModel.errorNombre,
            alCambiarValor = registroViewModel::onNombreChange
        )
        CampoTexto(
            valor = registroViewModel.correo,
            etiqueta = "Correo electrónico",
            mensajeError = registroViewModel.errorCorreo,
            alCambiarValor = registroViewModel::onCorreoChange,
            tipoTeclado = KeyboardType.Email
        )
        CampoTexto(
            valor = registroViewModel.contrasena,
            etiqueta = "Contraseña",
            mensajeError = registroViewModel.errorContrasena,
            alCambiarValor = registroViewModel::onContrasenaChange,
            tipoTeclado = KeyboardType.Password,
            esContrasena = true
        )
        CampoTexto(
            valor = registroViewModel.confirmacion,
            etiqueta = "Confirmar contraseña",
            mensajeError = registroViewModel.errorConfirmacion,
            alCambiarValor = registroViewModel::onConfirmacionChange,
            tipoTeclado = KeyboardType.Password,
            esContrasena = true
        )

        MenuPerfiles(
            perfilSeleccionado = registroViewModel.perfil,
            mensajeError = registroViewModel.errorPerfil,
            alSeleccionarPerfil = registroViewModel::onPerfilChange
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = registroViewModel.terminosAceptados,
                onCheckedChange = registroViewModel::onTerminosChange
            )
            Text(text = "Acepto los términos de uso de la aplicación")
        }
        registroViewModel.errorTerminos?.let { mensaje ->
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Button(
            onClick = registroViewModel::onRegistrarClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarme")
        }
    }

    if (registroViewModel.registroExitoso) {
        AlertDialog(
            onDismissRequest = registroViewModel::onRegistroConfirmado,
            title = { Text("Registro exitoso") },
            text = { Text("Bienvenido/a, ${registroViewModel.nombre.trim()}") },
            confirmButton = {
                TextButton(onClick = registroViewModel::onRegistroConfirmado) {
                    Text("Aceptar")
                }
            }
        )
    }
}

/** Campo de texto reutilizable que muestra su mensaje de error debajo cuando existe. */
@Composable
private fun CampoTexto(
    valor: String,
    etiqueta: String,
    mensajeError: String?,
    alCambiarValor: (String) -> Unit,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    esContrasena: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = alCambiarValor,
        label = { Text(etiqueta) },
        isError = mensajeError != null,
        supportingText = { mensajeError?.let { Text(it) } },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado),
        visualTransformation = if (esContrasena) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth()
    )
}

/** Menú desplegable con los perfiles de usuario del caso CENAMAD. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MenuPerfiles(
    perfilSeleccionado: PerfilUsuario?,
    mensajeError: String?,
    alSeleccionarPerfil: (PerfilUsuario) -> Unit
) {
    var menuExpandido by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = menuExpandido,
        onExpandedChange = { menuExpandido = it }
    ) {
        OutlinedTextField(
            value = perfilSeleccionado?.etiqueta ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Perfil de usuario") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpandido) },
            isError = mensajeError != null,
            supportingText = { mensajeError?.let { Text(it) } },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = menuExpandido,
            onDismissRequest = { menuExpandido = false }
        ) {
            PerfilUsuario.entries.forEach { perfil ->
                DropdownMenuItem(
                    text = { Text(perfil.etiqueta) },
                    onClick = {
                        alSeleccionarPerfil(perfil)
                        menuExpandido = false
                    }
                )
            }
        }
    }
}