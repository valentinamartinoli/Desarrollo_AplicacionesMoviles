package com.duoc.maderita.repository

import com.duoc.maderita.local.MaderaDao
import com.duoc.maderita.model.ImagenesMadera
import com.duoc.maderita.model.Madera
import com.duoc.maderita.remote.MaderaApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Catálogo de maderas: se lee desde Room y se actualiza desde Xano
interface MaderaRepositorio {
    val maderas: Flow<List<Madera>>
    fun madera(id: Int): Flow<Madera?>

    // GET /madera: trae el catálogo de Xano y lo guarda en Room
    suspend fun refrescar(): Result<Unit>

    // GET /madera/{id}: trae una madera de Xano y la guarda en Room
    suspend fun refrescarUna(id: Int): Result<Unit>
}

class MaderaRepositorioImpl(
    private val api: MaderaApi,
    private val dao: MaderaDao,
    // Convierte el nombre de una foto en su id de recurso (se pasa desde la app para poder probarlo)
    private val resolverImagen: (String) -> Int
) : MaderaRepositorio {

    override val maderas: Flow<List<Madera>> = dao.observarTodas().map { lista ->
        lista.map { it.aModelo(resolverImagen(ImagenesMadera.nombreRecurso(it.nombre))) }
    }

    override fun madera(id: Int): Flow<Madera?> = dao.observarPorId(id).map { entidad ->
        entidad?.aModelo(resolverImagen(ImagenesMadera.nombreRecurso(entidad.nombre)))
    }

    override suspend fun refrescar(): Result<Unit> = try {
        val remotas = api.listarMaderas()
        // Si Xano responde una lista vacía no se borra lo que ya está guardado
        if (remotas.isNotEmpty()) dao.reemplazarTodas(remotas.map { it.aEntity() })
        Result.success(Unit)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun refrescarUna(id: Int): Result<Unit> = try {
        dao.guardarTodas(listOf(api.obtenerMadera(id).aEntity()))
        Result.success(Unit)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}
