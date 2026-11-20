package com.duoc.maderita

import com.duoc.maderita.local.MaderaDao
import com.duoc.maderita.local.MaderaEntity
import com.duoc.maderita.local.UsuarioDao
import com.duoc.maderita.local.UsuarioEntity
import com.duoc.maderita.model.Madera
import com.duoc.maderita.repository.Escaneo
import com.duoc.maderita.repository.EscaneoRepositorio
import com.duoc.maderita.repository.MaderaRepositorio
import com.duoc.maderita.repository.UsuarioRepositorio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

// Muestra en la consola cada etapa de una prueba (Preparación, Acción, Comprobación)
fun paso(etapa: String, texto: String) = println("$etapa: $texto")

// Error HTTP como el que lanza Retrofit cuando Xano responde con ese código
fun errorHttp(codigo: Int) = HttpException(Response.error<Any>(codigo, "".toResponseBody(null)))

// Repositorio de usuarios falso: devuelve lo que la prueba le indique y anota cómo lo llamaron
class UsuarioRepositorioFalso : UsuarioRepositorio {
    val usuarioActual = MutableStateFlow<Usuario?>(null)
    override val usuario: Flow<Usuario?> = usuarioActual

    var sesionActiva = false
    var resultadoLogin: Result<Unit> = Result.success(Unit)
    var resultadoRegistro: Result<Unit> = Result.success(Unit)
    var resultadoPerfil: Result<Unit> = Result.success(Unit)

    val llamadasLogin = mutableListOf<Pair<String, String>>()
    val llamadasRegistro = mutableListOf<Triple<String, String, String>>()
    var sesionCerrada = false

    override suspend fun haySesionActiva() = sesionActiva

    override suspend fun registrar(nombre: String, correo: String, contrasena: String): Result<Unit> {
        llamadasRegistro.add(Triple(nombre, correo, contrasena))
        return resultadoRegistro
    }

    override suspend fun iniciarSesion(correo: String, contrasena: String): Result<Unit> {
        llamadasLogin.add(Pair(correo, contrasena))
        return resultadoLogin
    }

    override suspend fun actualizarPerfil(nombreCompleto: String, telefono: String, correo: String) = resultadoPerfil

    override suspend fun cerrarSesion() {
        sesionCerrada = true
    }
}

// Room falso en memoria: sirve para probar los repositorios sin emulador
class MaderaDaoFalso : MaderaDao() {
    private val datos = MutableStateFlow<List<MaderaEntity>>(emptyList())
    val guardadas: List<MaderaEntity> get() = datos.value

    override fun observarTodas(): Flow<List<MaderaEntity>> = datos

    override fun observarPorId(id: Int): Flow<MaderaEntity?> =
        datos.map { lista -> lista.firstOrNull { it.id == id } }

    override suspend fun guardarTodas(maderas: List<MaderaEntity>) {
        val ids = maderas.map { it.id }.toSet()
        datos.value = (datos.value.filter { it.id !in ids } + maderas).sortedBy { it.id }
    }

    override suspend fun borrarTodas() {
        datos.value = emptyList()
    }
}

class UsuarioDaoFalso : UsuarioDao {
    private val datos = MutableStateFlow<List<UsuarioEntity>>(emptyList())
    val guardados: List<UsuarioEntity> get() = datos.value

    override fun observarSesion(): Flow<UsuarioEntity?> =
        datos.map { lista -> lista.firstOrNull { it.sesionActiva } }

    override suspend fun obtenerSesion(): UsuarioEntity? = datos.value.firstOrNull { it.sesionActiva }

    override suspend fun guardar(usuario: UsuarioEntity) {
        datos.value = datos.value.filter { it.id != usuario.id } + usuario
    }

    override suspend fun cerrarSesiones() {
        datos.value = datos.value.map { it.copy(sesionActiva = false) }
    }
}

class MaderaRepositorioFalso(maderasIniciales: List<Madera> = emptyList()) : MaderaRepositorio {
    private val catalogo = MutableStateFlow(maderasIniciales)
    override val maderas: Flow<List<Madera>> = catalogo

    var resultadoRefrescar: Result<Unit> = Result.success(Unit)
    var llamadasRefrescar = 0

    override fun madera(id: Int): Flow<Madera?> = catalogo.map { lista -> lista.firstOrNull { it.id == id } }

    override suspend fun refrescar(): Result<Unit> {
        llamadasRefrescar++
        return resultadoRefrescar
    }

    override suspend fun refrescarUna(id: Int): Result<Unit> = Result.success(Unit)
}

class EscaneoRepositorioFalso : EscaneoRepositorio {
    class Creacion(val maderaId: Int, val confianza: Double, val foto: ByteArray)

    var escaneos: Result<List<Escaneo>> = Result.success(emptyList())
    var resultadoCrear: Result<Unit> = Result.success(Unit)
    var resultadoEliminar: Result<Unit> = Result.success(Unit)

    val creados = mutableListOf<Creacion>()
    val eliminados = mutableListOf<Int>()

    override suspend fun listar(): Result<List<Escaneo>> = escaneos

    override suspend fun crear(maderaId: Int, confianza: Double, notas: String, foto: ByteArray): Result<Unit> {
        creados.add(Creacion(maderaId, confianza, foto))
        return resultadoCrear
    }

    override suspend fun eliminar(id: Int): Result<Unit> {
        eliminados.add(id)
        return resultadoEliminar
    }
}
