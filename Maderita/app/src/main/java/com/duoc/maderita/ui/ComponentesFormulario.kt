package com.duoc.maderita.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

// Título de cada vista, con una línea debajo
@Composable
fun TituloVista(titulo: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.onBackground)
    }
}

// Campo de texto con esquinas redondeadas. Muestra el mensaje de error debajo.
// Si esContrasena es true, los caracteres se muestran como puntos.
@Composable
fun CampoTexto(
    valor: String,
    etiqueta: String,
    alCambiar: (String) -> Unit,
    error: String = "",
    esContrasena: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        label = { Text(etiqueta) },
        isError = error != "",
        supportingText = { Text(error) },
        singleLine = true,
        shape = RoundedCornerShape(20.dp),
        visualTransformation = if (esContrasena) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth()
    )
}