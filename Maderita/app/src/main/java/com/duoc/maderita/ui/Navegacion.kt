package com.duoc.maderita.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun Navegacion(destinoInicial: String) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = destinoInicial) {
        composable("login") { Login(navController = navController) }
        composable("registro") { RegistroScreen(navController = navController) }
        composable("home") { HomeScreen(navController = navController) }
        composable("escaner") { Scanner(navController = navController) }
        composable("perfil") { PerfilUsuario(navController = navController) }
        composable("editarPerfil") { EditarPerfil(navController = navController) }
        composable("agregarUsuarios") { AgregarUsuarios(navController = navController) }
        composable("listaUsuarios") { ListaUsuarios(navController = navController) }
        composable("tiposMadera") { TiposMadera(navController = navController) }

        //Detalle de un tipo de madera: recibe el id en la ruta
        composable(
            route = "detalleMadera/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            DetalleMadera(navController = navController, maderaId = id)
        }
    }
}