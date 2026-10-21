package com.duoc.maderita.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navegacion() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "login") {
        composable("login") { Login(navController) }
        composable("agregarUsuarios") { AgregarUsuarios() }
    }
}