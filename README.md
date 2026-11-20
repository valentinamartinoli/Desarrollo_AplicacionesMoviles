# Maderita

Maderita es una aplicación móvil nativa para Android desarrollada con Jetpack Compose y Kotlin. Su objetivo principal es ofrecer una herramienta intuitiva para el escaneo, identificación y consulta de información detallada sobre distintos tipos de maderas (como Pino Radiata, Roble, Raulí y Coigüe), integrada con un sistema de perfiles de usuario y un diseño visual coherente.

---

## Enlaces del Proyecto

* Tablero de Planificación (Trello): https://trello.com/b/9MepU1rH/maderita
* Repositorio Oficial (GitHub): https://github.com/valentinamartinoli/Desarrollo_AplicacionesMoviles

---

## Características Principales

* Pantalla de Carga Personalizada: pantalla con el logo y un indicador de carga que se muestra unos segundos al abrir la app, controlada por `CargaViewModel` (`PantallaCarga`).
* Inicio de sesión y registro: login con sesión guardada en el celular y formulario de registro con validación de campos.
* Escáner e Identificación: módulo interactivo con animación de línea de escaneo y uso de la cámara del celular para tomar la foto, que muestra el resultado de la lectura o un error de lectura mediante modales.
* Catálogo de Maderas: muestrario interactivo en la pantalla principal (`HomeScreen`) cargado desde `MaderaRepositorio`, y ficha detallada de cada madera (`DetalleMadera`).
* Perfil de Usuario: visualización de información personal, historial de maderas escaneadas, edición de datos (`EditarPerfil`) y opciones de sesión desde `PerfilUsuario`.
* Diseño Verde/Crema Consistente: paleta de colores orgánicos y componentes limpios ajustados a la experiencia visual de la marca.

---

## Cómo ejecutar el proyecto

1. Clonar el repositorio: `git clone https://github.com/valentinamartinoli/Desarrollo_AplicacionesMoviles.git`
2. Abrir con Android Studio la carpeta **`Maderita`**. El proyecto Android está en esa subcarpeta, no en la raíz del repositorio.
3. Esperar a que termine la sincronización de Gradle (Sync Now).
4. Ejecutar la aplicación (Run) en un emulador o en un celular Android, con internet.

Requisitos: una versión reciente de Android Studio compatible con el Android Gradle Plugin 9.2.1, Kotlin 2.2.10 y un dispositivo con Android 7.0 (API 24) o superior. El Scanner usa la cámara del dispositivo y pide el permiso al momento de usarla.

---

## Pantallas de la Aplicación

| Pantalla | Archivo | Descripción |
| :--- | :--- | :--- |
| Carga | `PantallaCarga.kt` | Logo e indicador de carga al abrir la app |
| Inicio de sesión | `Login.kt` | Correo y contraseña validados con Xano y modal de inicio de sesión exitoso |
| Registro de usuario | `RegistroScreen.kt` | Formulario con validaciones y modal de registro exitoso |
| Home | `HomeScreen.kt` | Pantalla principal con accesos al escáner, al perfil y al catálogo de maderas |
| Escáner | `Scanner.kt` | Toma una foto con la cámara y muestra el resultado en un modal |
| Tipos de madera | `TiposMadera.kt` | Listado de los tipos de madera |
| Detalle de madera | `DetalleMadera.kt` | Ficha detallada de una madera |
| Perfil de usuario | `PerfilUsuario.kt` | Datos del usuario, maderas escaneadas y cierre de sesión |
| Editar perfil | `EditarPerfil.kt` | Edición de los datos del perfil |

---

## Sistema de Diseño y Paleta de Colores

El sistema visual utiliza una paleta en tonos verdes y crema extraídos de referencias naturales:

| Color | Código Hex | Uso en la Aplicación |
| :--- | :--- | :--- |
| **Fondo Crema** | `#F0F1E5` | Fondo principal de las pantallas |
| **Verde Pálido** | `#C2CFB0` | Tarjetas (`Cards`), contenedores y diálogos |
| **Verde Botón** | `#7EAA6E` | Botones de acción, bordes e indicadores de carga |
| **Texto Oscuro** | `#1F4D36` | Tipografía principal, títulos e iconos |

---

## Arquitectura y Tecnologías

* Lenguaje: Kotlin
* UI Framework: Jetpack Compose con Material 3
* Arquitectura: MVVM (Model-View-ViewModel). Las pantallas solo muestran datos y envían eventos al ViewModel; toda llamada a red está en un repositorio.
* Gestión de Estado: `StateFlow` y `mutableStateOf`
* Navegación: Jetpack Navigation Compose
* Asincronía: Kotlin Coroutines (`viewModelScope`)
* Base de datos local: Room (catálogo de maderas y sesión del usuario)
* Backend: Xano (API REST) consumido con Retrofit, OkHttp y Gson
* API externa: Open-Meteo (clima del día en el Home)
* Cámara: `ActivityResultContracts.TakePicture` con `FileProvider`
* Pruebas unitarias: JUnit 4, kotlinx-coroutines-test y MockWebServer

---

## Backend en Xano

La app usa dos grupos de la API de Xano (las URLs están en `remote/Apis.kt`, objeto `ClienteRetrofit`).

| Método | Endpoint | Uso en la app | Requiere token |
| :--- | :--- | :--- | :--- |
| GET | `/madera` | Catálogo de maderas (Home y Tipos de madera) | No |
| GET | `/madera/{id}` | Ficha de una madera | No |
| POST | `/auth/signup` | Registro de usuario | No |
| POST | `/auth/login` | Inicio de sesión (devuelve el token) | No |
| GET | `/auth/me` | Datos del usuario con sesión iniciada | Sí |
| PATCH | `/user/{id}` | Guardar los cambios de Editar perfil | Sí |
| POST | `/escaneo` | Guardar un escaneo con su foto | Sí |
| GET | `/escaneo` | Historial de maderas escaneadas del perfil | Sí |
| DELETE | `/escaneo/{id}` | Borrar un escaneo del historial | Sí |

La API externa Open-Meteo (`GET /v1/forecast`) se consume aparte, sin pasar por Xano.

---

## Base de datos local (Room)

* `maderas`: copia del catálogo de Xano. Permite ver las maderas sin internet después de la primera carga.
* `usuario`: usuario con la sesión iniciada y su token de Xano. Reemplaza a SharedPreferences.

La primera vez hay que abrir la app con internet para descargar el catálogo.

---

## Pruebas unitarias

Las pruebas están en `app/src/test/java/com/duoc/maderita`:

| Carpeta | Qué comprueba |
| :--- | :--- |
| `model` | Reglas de `ValidadorRegistro` y fotos de cada madera |
| `viewmodel` | Login, Registro, Carga, Catálogo, Perfil y Escáner con repositorios falsos |
| `repository` | Cada endpoint de Xano y la API del clima con un servidor local (MockWebServer) |

Para ejecutarlas: `.\gradlew.bat :app:testDebugUnitTest` en la carpeta `Maderita`, o clic derecho en `app/src/test/java` y **Run**.

---

## Inicio de sesión y sesión guardada

* Inicio de sesión: el usuario ingresa su correo y contraseña. La app los envía a Xano (`auth/login`), recibe un token y pide los datos del usuario (`auth/me`). Si algo falla se muestra un mensaje en la pantalla.
* Sesión guardada: el usuario y su token se guardan en Room. Si no cierra sesión, al volver a abrir la app entra directo al Home.
* Cierre de sesión: el botón "Cerrar Sesión" del perfil marca la sesión como cerrada en Room y vuelve al login.
* Registro de usuario: formulario con validación de nombre, correo, contraseña
