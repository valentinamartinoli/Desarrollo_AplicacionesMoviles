package com.duoc.maderita.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.duoc.maderita.R
import com.duoc.maderita.ui.theme.*
import com.duoc.maderita.viewmodel.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Login(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    var cargando by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()   // deja espacio para las barras del celular
            .imePadding()          // sube el contenido cuando aparece el teclado
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_maderita),
            contentDescription = "Logo de Maderita",
            modifier = Modifier.size(140.dp)
        )

        TituloVista("INICIO SESIÓN")

        CampoTexto(
            valor = loginViewModel.nombre,
            etiqueta = "Nombre",
            error = loginViewModel.errorNombre,
            alCambiar = { loginViewModel.cambiarNombre(it) }
        )
        CampoTexto(
            valor = loginViewModel.contrasena,
            etiqueta = "Contraseña",
            error = loginViewModel.errorContrasena,
            alCambiar = { loginViewModel.cambiarContrasena(it) },
            esContrasena = true
        )

        Button(
            onClick = {
                // Solo entra si los dos campos tienen algo escrito
                cargando = true
                scope.launch {
                    delay(1500)
                    loginViewModel.guardarSesion()
                    loginViewModel.guardarSesion()
                    cargando = false
                    // Se borra el login de la pila para que "atrás" no vuelva a él
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            },
            enabled = !cargando,
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeBoton
            ),
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            if (cargando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.5.dp,
                    color = Color.White)
        } else {
        Text(
            text = "Iniciar sesión",
            color = Color.White,
            fontWeight = FontWeight.Bold)
                }
            }
        TextButton(onClick = { navController.navigate("registro") }) {
            Text("¿No tienes cuenta? Regístrate aquí",
            color = TextoOscuro)
        }
    }
}