package com.duoc.maderita.repository

import com.duoc.maderita.Usuario
import com.duoc.maderita.local.UsuarioDao
import com.duoc.maderita.remote.AuthApi
import com.duoc.maderita.remote.ClienteRetrofit
import com.duoc.maderita.remote.LoginRequest
import com.duoc.maderita.remote.PerfilApi
import com.duoc.maderita.remote.PerfilRequest
import com.duoc.maderita.remote.SignupRequest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Usuario y sesión: el login se hace en Xano y la sesión queda guardada en Room
interface UsuarioRepositorio {
    // El usuario con la sesión iniciada (null si no hay ninguno)
    val usuario: Flow<Usuario?>

    suspend fun haySesionActiva(): Boolean

    // POST auth/signup
    suspend fun registrar(nombre: String, correo: String, contrasena: String): Result<Unit>

    // POST auth/login + GET auth/me; guarda el usuario y su token en Room
    suspend fun iniciarSesion(correo: String, contrasena: String): Result<Unit>

    // PATCH user/{id}: guarda los cambios del perfil en Xano y en Room
    suspend fun actualizarPerfil(nombreCompleto: String, telefono: String, correo: String): Result<Unit>

    suspend fun cerrarSesion()
}

// Separa "Valentina Martinoli" en nombre y apellido. Si solo hay un nombre se mantiene el apellido actual.
fun dividirNombre(nombreCompleto: String, apellidoActual: String): Pair<String, String> {
    val partes = nombreCompleto.trim().split(" ", limit = 2)
    return Pair(partes.getOrElse(0) { "" }, partes.getOrElse(1) { apellidoActual })
}

class UsuarioRepositorioImpl(
    private val api: AuthApi,
    private val perfilApi: PerfilApi,
    private val dao: UsuarioDao
) : UsuarioRepositorio {

    override val usuario: Flow<Usuario?> = dao.observarSesion().map { it?.aModelo() }

    override suspend fun haySesionActiva(): Boolean = dao.obtenerSesion() != null

    override suspend fun registrar(nombre: String, correo: String, contrasena: String): Result<Unit> = try {
        api.registrar(SignupRequest(name = nombre.trim(), email = correo.trim(), password = contrasena))
        Result.success(Unit)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun iniciarSesion(correo: String, contrasena: String): Result<Unit> = try {
        val token = api.iniciarSesion(LoginRequest(email = correo.trim(), password = contrasena)).authToken
        val perfil = api.obtenerPerfil(ClienteRetrofit.cabeceraToken(token))
        dao.cerrarSesiones()
        dao.guardar(perfil.aEntity(token))
        Result.success(Unit)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun actualizarPerfil(
        nombreCompleto: String,
        telefono: String,
        correo: String
    ): Result<Unit> = try {
        val sesion = dao.obtenerSesion() ?: throw IllegalStateException("No hay sesión iniciada")
        val (nombre, apellido) = dividirNombre(nombreCompleto, sesion.apellido)
        val actualizado = perfilApi.actualizarPerfil(
            token = ClienteRetrofit.cabeceraToken(sesion.token),
            id = sesion.id,
            datos = PerfilRequest(name = nombre, apellido = apellido, telefono = telefono.trim(), email = correo.trim())
        )
        dao.guardar(actualizado.aEntity(sesion.token))
        Result.success(Unit)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun cerrarSesion() = dao.cerrarSesiones()
}
