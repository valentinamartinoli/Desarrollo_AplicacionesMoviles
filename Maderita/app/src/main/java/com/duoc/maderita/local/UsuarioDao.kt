package com.duoc.maderita.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    // El usuario con la sesión iniciada (null si no hay ninguno)
    @Query("SELECT * FROM usuario WHERE sesionActiva = 1 LIMIT 1")
    fun observarSesion(): Flow<UsuarioEntity?>

    @Query("SELECT * FROM usuario WHERE sesionActiva = 1 LIMIT 1")
    suspend fun obtenerSesion(): UsuarioEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(usuario: UsuarioEntity)

    @Query("UPDATE usuario SET sesionActiva = 0")
    suspend fun cerrarSesiones()
}
