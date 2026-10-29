package com.duoc.maderita.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.duoc.maderita.model.Madera
import com.duoc.maderita.repository.MaderaRepositorio
import com.duoc.maderita.ui.theme.*

@Composable
fun DetalleMadera(navController: NavController, maderaId: Int) {
    val madera = MaderaRepositorio.obtenerMaderaPorId(maderaId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoCrema)
            .systemBarsPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Contenido con scroll, sirve para que el botón "Volver" siempre quede abajo
        Column(
            modifier = Modifier.weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (madera == null) {
                TituloVista("MADERA NO ENCONTRADA")
                Text("No existe información para esta madera")
            } else {
                ContenidoDetalle(madera)
            }
        }

        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(containerColor = VerdeBoton),
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Volver")
        }
    }
}

@Composable
private fun ContenidoDetalle(madera: Madera) {
    Image(
        painter = painterResource(id = madera.imagen),
        contentDescription = madera.nombre,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth()
            .aspectRatio(16f/10f)
            .clip(RoundedCornerShape(16.dp))
    )

    Column{
        Text(
            text = madera.nombre,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = madera.nombreCientifico,
            style = MaterialTheme.typography.bodyLarge,
            fontStyle = FontStyle.Italic
        )
    }
    //Etiquetas para origen y dureza
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Etiqueta("Origen: ${madera.origen}")
        Etiqueta("Dureza: ${madera.dureza}")
    }

    HorizontalDivider(thickness = 1.dp, color = VerdePalido)

    SeccionDetalle(titulo = "Descripción") {
        Text(madera.descripcion, style = MaterialTheme.typography.bodyLarge,
            color = TextoOscuro)
    }

    SeccionDetalle(titulo = "Usos principales") {
        madera.usos.forEach { uso -> Text("•  $uso",
            style = MaterialTheme.typography.bodyLarge,
            color = TextoOscuro)
        }
    }
}

@Composable
private fun Etiqueta(texto: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = VerdePalido
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.labelLarge,
            color = TextoOscuro,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun SeccionDetalle(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextoOscuro
        )
        contenido()
    }
}