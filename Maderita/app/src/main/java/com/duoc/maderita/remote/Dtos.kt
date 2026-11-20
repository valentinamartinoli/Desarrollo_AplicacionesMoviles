package com.duoc.maderita.remote

import com.google.gson.annotations.SerializedName

// ---- Catálogo (grupo "maderita" de Xano) ----

// Todos los campos son opcionales: si Xano no manda alguno, la app no se cae
data class MaderaDto(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("nombre") val nombre: String? = null,
    @SerializedName("especie") val especie: String? = null,
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("dureza") val dureza: String? = null,
    @SerializedName("origen") val origen: String? = null,
    @SerializedName("usos") val usos: String? = null
)

// ---- Autenticación (grupo "Authentication" de Xano) ----

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class SignupRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("perfil") val perfil: String = ""
)

data class TokenResponse(
    @SerializedName("authToken") val authToken: String = ""
)

data class UsuarioDto(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("name") val name: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("apellido") val apellido: String? = null,
    @SerializedName("nacionalidad") val nacionalidad: String? = null,
    @SerializedName("telefono") val telefono: String? = null
)

// PATCH /user/{id}: solo se envían los datos que el usuario puede editar
data class PerfilRequest(
    @SerializedName("name") val name: String,
    @SerializedName("apellido") val apellido: String,
    @SerializedName("telefono") val telefono: String,
    @SerializedName("email") val email: String
)

// ---- Historial de escaneos (grupo "maderita" de Xano, requiere token) ----

data class EscaneoDto(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("created_at") val creadoEn: Long = 0,
    @SerializedName("user_id") val userId: Int = 0,
    @SerializedName("madera_id") val maderaId: Int = 0,
    @SerializedName("confianza") val confianza: Double? = null,
    @SerializedName("notas") val notas: String? = null
)

// ---- API externa: Open-Meteo (clima) ----

data class ClimaResponse(
    @SerializedName("current") val actual: ClimaActualDto? = null
)

data class ClimaActualDto(
    @SerializedName("temperature_2m") val temperatura: Double? = null,
    @SerializedName("relative_humidity_2m") val humedad: Double? = null
)
