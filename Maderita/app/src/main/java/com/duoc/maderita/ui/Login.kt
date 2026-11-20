package com.duoc.maderita.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.duoc.maderita.R
import com.duoc.maderita.viewmodel.FabricaViewModel
import com.duoc.maderita.viewmodel.LoginViewModel

@Composable
fun Login(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel(factory = FabricaViewModel.Fabrica)
) {
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
            valor = loginViewModel.correo,
            etiqueta = "Correo electrónico",
            error = loginViewModel.errorCorreo,
            alCambiar = { loginViewModel.cambiarCorreo(it) }
        )
        CampoTexto(
            valor = loginViewModel.contrasena,
            etiqueta = "Contraseña",
            error = loginViewModel.errorContrasena,
            alCambiar = { loginViewModel.cambiarContrasena(it) },
            esContrasena = true
        )

        // Error que viene de Xano o de la conexión
        if (loginViewModel.errorGeneral != "") {
            Text(text = loginViewModel.errorGeneral, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = { loginViewModel.ingresar() },
            enabled = !loginViewModel.cargando,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (loginViewModel.cargando) {
                CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 2.5.dp)
            } else {
                Text("Iniciar sesión")
            }
        }

        TextButton(onClick = { navController.navigate("registro") }) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }

    // Modal que aparece cuando el inicio de sesión es correcto
    if (loginViewModel.ingresoExitoso) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Inicio de sesión exitoso") },
            text = { Text("Bienvenido/a, ${loginViewModel.nombreBienvenida}") },
            confirmButton = {
                Button(
                    onClick = {
                        loginViewModel.ingresoExitoso = false
                        // Se borra el login de la pila para que "atrás" no vuelva a él
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                ) {
                    Text("Continuar")
                }
            }
        )
    }
}
