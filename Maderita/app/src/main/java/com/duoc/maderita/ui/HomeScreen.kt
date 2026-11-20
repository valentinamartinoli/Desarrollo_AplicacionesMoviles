package com.duoc.maderita.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.duoc.maderita.R
import com.duoc.maderita.ui.theme.*
import com.duoc.maderita.viewmodel.CatalogoViewModel
import com.duoc.maderita.viewmodel.ClimaViewModel
import com.duoc.maderita.viewmodel.FabricaViewModel
import com.duoc.maderita.viewmodel.PerfilViewModel


@Composable
fun HomeScreen(
    navController: NavController,
    perfilViewModel: PerfilViewModel = viewModel(factory = FabricaViewModel.Fabrica),
    catalogoViewModel: CatalogoViewModel = viewModel(factory = FabricaViewModel.Fabrica),
    climaViewModel: ClimaViewModel = viewModel(factory = FabricaViewModel.Fabrica)
) {
    val usuario by perfilViewModel.usuario.collectAsState()
    val maderas by catalogoViewModel.maderas.collectAsState()
    val maderasEjemplo = maderas.take(4)
    val clima = climaViewModel.clima
    val contexto = LocalContext.current

    // Pide el permiso de ubicación. Si el usuario acepta, se carga el clima de donde está
    val permisoUbicacion = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            val ubicacion = obtenerUbicacion(contexto)
            if (ubicacion != null) {
                climaViewModel.cargarClima(ubicacion.latitude, ubicacion.longitude, "tu ubicación")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoCrema)
            .systemBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Encabezado con el logo y el acceso al perfil
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.logo_splash),
                    contentDescription = "Logo",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Maderita",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextoOscuro
                )
            }
            IconButton(onClick = { navController.navigate("perfil") }) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil",
                    tint = VerdeBoton,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjeta con los datos del usuario
        Card(
            onClick = { navController.navigate("perfil") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = VerdePalido
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_splash),
                    contentDescription = "Foto Perfil",
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .border(2.dp, VerdeBoton, CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "¡Hola, ${usuario?.nombre.orEmpty()}!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextoOscuro
                    )
                    Text(
                        text = usuario?.correo.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoOscuro.copy(alpha = 0.8f)
                    )
                }
                Text(
                    text = "Ver perfil >",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextoOscuro
                )
            }
        }

        // Clima del día (API externa). Si no se pudo cargar, la tarjeta no se muestra.
        if (clima != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = VerdePalido.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Hoy en ${climaViewModel.lugar}: ${clima.temperatura}°C, humedad ${clima.humedad.toInt()}%",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoOscuro
                    )
                    TextButton(
                        onClick = { permisoUbicacion.launch(Manifest.permission.ACCESS_COARSE_LOCATION) }
                    ) {
                        Text("Usar mi ubicación")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botones de acceso al escáner y a los tipos de madera
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                onClick = { navController.navigate("escaner") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = VerdePalido
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Scanner",
                        modifier = Modifier.size(36.dp),
                        tint = VerdeBoton
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Scanner", fontWeight = FontWeight.Bold, color = TextoOscuro)
                }
            }

            Card(
                onClick = { navController.navigate("tiposMadera") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = VerdePalido
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🪵", fontSize = 32.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tipos de Madera", fontWeight = FontWeight.Bold, color = TextoOscuro)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Ejemplos de maderas del catálogo
        Text(
            text = "Ejemplos de Madera",
            style = MaterialTheme.typography.titleMedium,
            color = TextoOscuro,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 12.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(maderasEjemplo) { madera ->
                Card(
                    onClick = { navController.navigate("detalleMadera/${madera.id}") },
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
                            painter = painterResource(id = madera.imagen),
                            contentDescription = madera.nombre,
                            modifier = Modifier.size(50.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = madera.nombre,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextoOscuro,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

// Última ubicación conocida del celular (null si no hay permiso o no hay datos)
private fun obtenerUbicacion(contexto: Context): Location? {
    val permiso = ContextCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_COARSE_LOCATION)
    if (permiso != PackageManager.PERMISSION_GRANTED) return null

    val manager = contexto.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
}