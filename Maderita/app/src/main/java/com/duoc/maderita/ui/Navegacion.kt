package com.duoc.maderita.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navegacion() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { Login(navController = navController) }
        composable("registro") { RegistroScreen(navController = navController) }                // CAMBIA: ahora recibe navController
        composable("home") { HomeScreen(navController = navController) }
        composable("escaner") { Scanner(navController = navController) }
        composable("agregarUsuarios") { AgregarUsuarios(navController = navController) }       // CAMBIA: ahora recibe navController
        composable("listaUsuarios") { ListaUsuarios(navController = navController) }           // NUEVA: pantalla "Ver registro usuarios"
    }
}