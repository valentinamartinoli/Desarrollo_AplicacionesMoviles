package com.duoc.maderita.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MaderaDao {
    @Query("SELECT * FROM maderas ORDER BY id")
    abstract fun observarTodas(): Flow<List<MaderaEntity>>

    @Query("SELECT * FROM maderas WHERE id = :id")
    abstract fun observarPorId(id: Int): Flow<MaderaEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun guardarTodas(maderas: List<MaderaEntity>)

    @Query("DELETE FROM maderas")
    abstract suspend fun borrarTodas()

    // Cambia todo el catálogo local por el que llegó de Xano, en una sola operación
    @Transaction
    open suspend fun reemplazarTodas(maderas: List<MaderaEntity>) {
        borrarTodas()
        guardarTodas(maderas)
    }
}
