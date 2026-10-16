package com.duoc.maderita

data class Usuario(
    val id: Int,
    var nombre: String,
    var apellido: String,
    var nacionalidad: String,
    var telefono: String,
    var correo: String
) {
    fun mostrarInfo(): String {
        return "ID: $id\nNombre: $nombre $apellido\nNacionalidad: $nacionalidad\nTeléfono: $telefono\nCorreo: $correo"
    }
}