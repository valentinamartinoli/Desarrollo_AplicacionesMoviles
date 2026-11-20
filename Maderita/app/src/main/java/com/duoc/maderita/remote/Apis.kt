package com.duoc.maderita.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

// Endpoints públicos del catálogo (grupo "maderita" de Xano)
interface MaderaApi {
    @GET("madera")
    suspend fun listarMaderas(): List<MaderaDto>

    @GET("madera/{id}")
    suspend fun obtenerMadera(@Path("id") id: Int): MaderaDto
}

// Endpoints privados del historial de escaneos (grupo "maderita" de Xano)
interface EscaneoApi {
    @GET("escaneo")
    suspend fun listarEscaneos(@Header("Authorization") token: String): List<EscaneoDto>

    // Se envía como formulario porque Xano recibe la foto en el campo imagen_escaneada
    @Multipart
    @POST("escaneo")
    suspend fun crearEscaneo(
        @Header("Authorization") token: String,
        @Part("user_id") userId: RequestBody,
        @Part("madera_id") maderaId: RequestBody,
        @Part("confianza") confianza: RequestBody,
        @Part("notas") notas: RequestBody,
        @Part imagen: MultipartBody.Part
    ): EscaneoDto

    @DELETE("escaneo/{id}")
    suspend fun eliminarEscaneo(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    )
}

// Edición del perfil (grupo "maderita" de Xano, requiere token)
interface PerfilApi {
    @PATCH("user/{id}")
    suspend fun actualizarPerfil(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body datos: PerfilRequest
    ): UsuarioDto
}

// Endpoints de autenticación (grupo "Authentication" de Xano)
interface AuthApi {
    @POST("auth/signup")
    suspend fun registrar(@Body datos: SignupRequest): TokenResponse

    @POST("auth/login")
    suspend fun iniciarSesion(@Body datos: LoginRequest): TokenResponse

    @GET("auth/me")
    suspend fun obtenerPerfil(@Header("Authorization") token: String): UsuarioDto
}

// API externa gratuita y sin clave: clima actual de una coordenada
interface ClimaApi {
    @GET("v1/forecast")
    suspend fun obtenerClima(
        @Query("latitude") latitud: Double,
        @Query("longitude") longitud: Double,
        @Query("current") campos: String = "temperature_2m,relative_humidity_2m"
    ): ClimaResponse
}

object ClienteRetrofit {
    const val URL_CATALOGO = "https://x8ki-letl-twmt.n7.xano.io/api:VGpzK8P9/"
    const val URL_AUTH = "https://x8ki-letl-twmt.n7.xano.io/api:LzyNtQiz/"
    const val URL_CLIMA = "https://api.open-meteo.com/"

    // Xano espera el token con el prefijo "Bearer "
    fun cabeceraToken(token: String): String = "Bearer $token"

    private fun <T> crear(baseUrl: String, api: Class<T>): T =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)

    fun crearMaderaApi(baseUrl: String = URL_CATALOGO): MaderaApi = crear(baseUrl, MaderaApi::class.java)
    fun crearEscaneoApi(baseUrl: String = URL_CATALOGO): EscaneoApi = crear(baseUrl, EscaneoApi::class.java)
    fun crearPerfilApi(baseUrl: String = URL_CATALOGO): PerfilApi = crear(baseUrl, PerfilApi::class.java)
    fun crearAuthApi(baseUrl: String = URL_AUTH): AuthApi = crear(baseUrl, AuthApi::class.java)
    fun crearClimaApi(baseUrl: String = URL_CLIMA): ClimaApi = crear(baseUrl, ClimaApi::class.java)
}
