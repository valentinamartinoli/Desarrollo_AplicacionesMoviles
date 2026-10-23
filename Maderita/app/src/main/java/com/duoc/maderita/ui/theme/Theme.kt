package com.duoc.maderita.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Esquema de colores de toda la app
private val EsquemaColores = lightColorScheme(
    primary = VerdeBoton,
    onPrimary = TextoOscuro,
    background = FondoCrema,
    onBackground = TextoOscuro,
    surface = FondoCrema,
    onSurface = TextoOscuro,
    surfaceContainerHigh = VerdePalido,
    surfaceContainerHighest = VerdePalido
)

@Composable
fun MaderitaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaColores,
        typography = Typography,
        content = content
    )
}