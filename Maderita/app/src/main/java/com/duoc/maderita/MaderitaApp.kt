package com.duoc.maderita

import android.app.Application
import android.content.Context
import com.duoc.maderita.local.AppDatabase
import com.duoc.maderita.remote.ClienteRetrofit
import com.duoc.maderita.repository.ClimaRepositorio
import com.duoc.maderita.repository.ClimaRepositorioImpl
import com.duoc.maderita.repository.EscaneoRepositorio
import com.duoc.maderita.repository.EscaneoRepositorioImpl
import com.duoc.maderita.repository.MaderaRepositorio
import com.duoc.maderita.repository.MaderaRepositorioImpl
import com.duoc.maderita.repository.UsuarioRepositorio
import com.duoc.maderita.repository.UsuarioRepositorioImpl

// Crea una sola vez la base de datos, las APIs y los repositorios que usan los ViewModels
class ContenedorApp(context: Context) {
    private val baseDatos = AppDatabase.crear(context)
    private val recursos = context.resources
    private val paquete = context.packageName

    val usuarios: UsuarioRepositorio = UsuarioRepositorioImpl(
        api = ClienteRetrofit.crearAuthApi(),
        perfilApi = ClienteRetrofit.crearPerfilApi(),
        dao = baseDatos.usuarioDao()
    )

    val escaneos: EscaneoRepositorio = EscaneoRepositorioImpl(
        api = ClienteRetrofit.crearEscaneoApi(),
        usuarios = baseDatos.usuarioDao()
    )

    val maderas: MaderaRepositorio = MaderaRepositorioImpl(
        api = ClienteRetrofit.crearMaderaApi(),
        dao = baseDatos.maderaDao(),
        resolverImagen = { nombre ->
            val id = recursos.getIdentifier(nombre, "drawable", paquete)
            if (id != 0) id else R.drawable.maderita
        }
    )

    val clima: ClimaRepositorio = ClimaRepositorioImpl(ClienteRetrofit.crearClimaApi())
}

class MaderitaApp : Application() {
    lateinit var contenedor: ContenedorApp
        private set

    override fun onCreate() {
        super.onCreate()
        contenedor = ContenedorApp(this)
    }
}
