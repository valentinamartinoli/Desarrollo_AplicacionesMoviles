package com.duoc.maderita.repository

import com.duoc.maderita.local.UsuarioDao
import com.duoc.maderita.remote.ClienteRetrofit
import com.duoc.maderita.remote.EscaneoApi
import com.duoc.maderita.remote.EscaneoDto
import kotlinx.coroutines.CancellationException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

data class Escaneo(
    val id: Int,
    val maderaId: Int,
    val confianza: Double,
    val notas: String
)

// Historial de maderas escaneadas por el usuario (endpoints privados de Xano)
interface EscaneoRepositorio {
    // GET escaneo: solo los escaneos del usuario con sesión iniciada
    suspend fun listar(): Result<List<Escaneo>>

    // POST escaneo: guarda un escaneo nuevo con su foto
    suspend fun crear(maderaId: Int, confianza: Double, notas: String, foto: ByteArray): Result<Unit>

    // DELETE escaneo/{id}
    suspend fun eliminar(id: Int): Result<Unit>
}

class EscaneoRepositorioImpl(
    private val api: EscaneoApi,
    private val usuarios: UsuarioDao
) : EscaneoRepositorio {

    override suspend fun listar(): Result<List<Escaneo>> = try {
        val sesion = usuarios.obtenerSesion() ?: throw IllegalStateException("No hay sesión iniciada")
        val escaneos = api.listarEscaneos(ClienteRetrofit.cabeceraToken(sesion.token))
            // El endpoint de Xano devuelve los escaneos de todos: se dejan solo los de este usuario
            .filter { it.userId == sesion.id }
            .map { it.aModelo() }
        Result.success(escaneos)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun crear(
        maderaId: Int,
        confianza: Double,
        notas: String,
        foto: ByteArray
    ): Result<Unit> = try {
        val sesion = usuarios.obtenerSesion() ?: throw IllegalStateException("No hay sesión iniciada")
        val texto = "text/plain".toMediaType()
        api.crearEscaneo(
            token = ClienteRetrofit.cabeceraToken(sesion.token),
            userId = sesion.id.toString().toRequestBody(texto),
            maderaId = maderaId.toString().toRequestBody(texto),
            confianza = confianza.toString().toRequestBody(texto),
            notas = notas.toRequestBody(texto),
            imagen = MultipartBody.Part.createFormData(
                "foto",
                "escaneo.jpg",
                foto.toRequestBody("image/jpeg".toMediaType())
            )
        )
        Result.success(Unit)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun eliminar(id: Int): Result<Unit> = try {
        val sesion = usuarios.obtenerSesion() ?: throw IllegalStateException("No hay sesión iniciada")
        api.eliminarEscaneo(ClienteRetrofit.cabeceraToken(sesion.token), id)
        Result.success(Unit)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}

private fun EscaneoDto.aModelo() = Escaneo(
    id = id,
    maderaId = maderaId,
    confianza = confianza ?: 0.0,
    notas = notas.orEmpty()
)
