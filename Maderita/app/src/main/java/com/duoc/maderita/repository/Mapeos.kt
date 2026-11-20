package com.duoc.maderita.repository

import com.duoc.maderita.Usuario
import com.duoc.maderita.local.MaderaEntity
import com.duoc.maderita.local.UsuarioEntity
import com.duoc.maderita.model.Madera
import com.duoc.maderita.remote.MaderaDto
import com.duoc.maderita.remote.UsuarioDto

// Conversiones entre lo que llega de Xano, lo que guarda Room y lo que muestra la pantalla

fun MaderaDto.aEntity() = MaderaEntity(
    id = id,
    nombre = nombre.orEmpty(),
    nombreCientifico = especie.orEmpty(),
    origen = origen.orEmpty(),
    dureza = dureza.orEmpty(),
    descripcion = descripcion.orEmpty(),
    usos = usos.orEmpty()
)

fun MaderaEntity.aModelo(imagen: Int) = Madera(
    id = id,
    nombre = nombre,
    imagen = imagen,
    nombreCientifico = nombreCientifico,
    origen = origen,
    dureza = dureza,
    descripcion = descripcion,
    // En Xano los usos vienen en un solo texto separado por "|"
    usos = usos.split("|").map { it.trim() }.filter { it.isNotEmpty() }
)

fun UsuarioDto.aEntity(token: String) = UsuarioEntity(
    id = id,
    nombre = name.orEmpty(),
    apellido = apellido.orEmpty(),
    nacionalidad = nacionalidad.orEmpty(),
    telefono = telefono.orEmpty(),
    correo = email.orEmpty(),
    token = token,
    sesionActiva = true
)

fun UsuarioEntity.aModelo() = Usuario(
    id = id,
    nombre = nombre,
    apellido = apellido,
    nacionalidad = nacionalidad,
    telefono = telefono,
    correo = correo
)
