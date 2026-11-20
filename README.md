# Maderita

Maderita es una aplicación móvil nativa para Android desarrollada con Jetpack Compose y Kotlin. Su objetivo principal es ofrecer una herramienta intuitiva para el escaneo y la consulta de información detallada sobre distintos tipos de maderas (como Pino Radiata, Roble, Raulí y Coigüe), integrada con un sistema de perfiles de usuario y un diseño visual coherente.

---

## Enlaces del Proyecto

* Tablero de Planificación (Trello): https://trello.com/b/9MepU1rH/maderita
* Repositorio Oficial (GitHub): https://github.com/valentinamartinoli/Desarrollo_AplicacionesMoviles

---

## Características Principales

* Ícono personalizado: la app tiene su propio ícono de Maderita en el lanzador del celular, en lugar del ícono por defecto de Android.
* Pantalla de Carga Personalizada: pantalla con el logo y un indicador de carga que se muestra unos segundos al abrir la app, controlada por `CargaViewModel` (`PantallaCarga`).
* Inicio de sesión y registro: login con sesión guardada en el celular y formulario de registro con validación de campos. Al registrarse, el usuario elige su perfil (por ejemplo, Investigador/a o Estudiante) y la app lo envía a Xano.
* Escáner de maderas: usa la cámara del celular para tomar una foto, con una animación de línea de escaneo. La foto se reduce a JPEG y se guarda en Xano junto con el escaneo. **La identificación de la madera es simulada**: la app muestra una madera del catálogo elegida al azar, con un porcentaje de coincidencia entre 70% y 99%. No hay un modelo de reconocimiento de imágenes.
* Clima y ubicación: el Home muestra la temperatura y la humedad del día. Por defecto es el clima de Santiago, y el botón "Usar mi ubicación" pide el permiso de ubicación y muestra el clima del lugar donde está el celular.
* Catálogo de Maderas: muestrario interactivo en la pantalla principal (`HomeScreen`) cargado desde `MaderaRepositorio`, y ficha detallada de cada madera (`DetalleMadera`).
* Perfil de Usuario: visualización de información personal, historial de maderas escaneadas, edición de datos (`EditarPerfil`) y opciones de sesión desde `PerfilUsuario`.
* Diseño Verde/Crema Consistente: paleta de colores orgánicos y componentes limpios ajustados a la experiencia visual de la marca.

---

## Recursos nativos del dispositivo

| Recurso | Dónde se usa | Cómo funciona |
| :--- | :--- | :--- |
| Cámara | `Scanner.kt` | Pide el permiso `CAMERA` y abre la cámara con `ActivityResultContracts.TakePicture`. La foto se guarda en un archivo temporal compartido con `FileProvider`. |
| Ubicación (GPS) | `HomeScreen.kt` | El botón "Usar mi ubicación" pide el permiso `ACCESS_COARSE_LOCATION` y lee la última ubicación conocida con `LocationManager`. Con esas coordenadas se consulta el clima. |

Ambos permisos se piden en el momento de usarlos. Si el usuario los rechaza, la app no se cierra: muestra un mensaje de error en el Scanner o mantiene el clima de Santiago en el Home.

---

## Cómo ejecutar el proyecto

1. Clonar el repositorio: `git clone https://github.com/valentinamartinoli/Desarrollo_AplicacionesMoviles.git`
2. Abrir con Android Studio la carpeta **`Maderita`**. El proyecto Android está en esa subcarpeta, no en la raíz del repositorio.
3. Esperar a que termine la sincronización de Gradle (Sync Now).
4. Ejecutar la aplicación (Run) en un emulador o en un celular Android.

Requisitos: una versión reciente de Android Studio compatible con el Android Gradle Plugin 9.2.1, Kotlin 2.2.10 y un dispositivo con Android 7.0 (API 24) o superior. El Scanner usa la cámara y el Home usa la ubicación; ambos piden el permiso al momento de usarlos. La app necesita internet para el catálogo, el inicio de sesión y el clima.

---

## Generar el APK firmado (release)

El APK que se entrega está firmado con una llave propia. **La llave (`.jks`) y su clave no están en el repositorio**: se guardan fuera de la carpeta del proyecto y nunca se suben a GitHub.

### 1. Crear la llave (una sola vez)

```bash
keytool -genkeypair -v -keystore maderita.jks -alias maderita -keyalg RSA -keysize 2048 -validity 9125
```

`keytool` viene con el JDK de Android Studio (`Android Studio.app/Contents/jbr/Contents/Home/bin/keytool` en Mac). En un sistema en español, la confirmación se responde con `sí`, no con `yes`. La clave del almacén también se usa como clave de la llave.

### 2. Compilar el APK release sin firmar

En la carpeta `Maderita`:

```bash
# Mac o Linux (si ./gradlew no tiene permiso de ejecución, usar: sh ./gradlew assembleRelease)
./gradlew assembleRelease

# Windows
.\gradlew.bat assembleRelease
```

El resultado queda en `app/build/outputs/apk/release/app-release-unsigned.apk`.

### 3. Alinear y firmar

Las herramientas están en la carpeta `build-tools` del SDK de Android (en Mac, `~/Library/Android/sdk/build-tools/<versión>`; en Windows, `%LOCALAPPDATA%\Android\Sdk\build-tools\<versión>`).

```bash
zipalign -f -p 4 app/build/outputs/apk/release/app-release-unsigned.apk app/build/outputs/apk/release/app-release-alineado.apk

apksigner sign --ks maderita.jks --ks-key-alias maderita --out maderita-release.apk app/build/outputs/apk/release/app-release-alineado.apk
```

`apksigner` pide la clave de la llave. En Windows los programas se llaman `zipalign.exe` y `apksigner.bat`.

### 4. Verificar e instalar

```bash
apksigner verify --verbose maderita-release.apk
adb install maderita-release.apk
```

La verificación debe decir `Verifies` y `Verified using v2 scheme: true`. Si ya hay una versión de debug instalada, se debe desinstalar antes (`adb uninstall com.duoc.maderita`), porque está firmada con otra llave.

Otra forma, desde Android Studio: **Build → Generate Signed App Bundle or APK… → APK**, y elegir la llave `maderita.jks`.

---

## Pantallas de la Aplicación

| Pantalla | Archivo | Descripción |
| :--- | :--- | :--- |
| Carga | `PantallaCarga.kt` | Logo e indicador de carga al abrir la app |
| Inicio de sesión | `Login.kt` | Correo y contraseña validados con Xano y modal de inicio de sesión exitoso |
| Registro de usuario | `RegistroScreen.kt` | Formulario con validaciones, selección de perfil y modal de registro exitoso |
| Home | `HomeScreen.kt` | Pantalla principal con el clima del día (con opción de usar la ubicación), accesos al escáner, al perfil y al catálogo de maderas |
| Escáner | `Scanner.kt` | Toma una foto con la cámara, la guarda en Xano y muestra la madera del resultado en un modal |
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
* Backend: Xano (API REST) consumido con Retrofit y Gson
* API externa: Open-Meteo (clima del día en el Home)
* Cámara: `ActivityResultContracts.TakePicture` con `FileProvider`
* Ubicación: `LocationManager` con el permiso `ACCESS_COARSE_LOCATION`
* Pruebas unitarias: JUnit 4, kotlinx-coroutines-test y MockWebServer

---

## Backend en Xano

La app usa dos grupos de la API de Xano (las URLs están en `remote/Apis.kt`, objeto `ClienteRetrofit`).

| Método | Endpoint | Uso en la app | Requiere token |
| :--- | :--- | :--- | :--- |
| GET | `/madera` | Catálogo de maderas (Home y Tipos de madera) | No |
| GET | `/madera/{id}` | Ficha de una madera | No |
| POST | `/auth/signup` | Registro de usuario (nombre, correo, contraseña y perfil) | No |
| POST | `/auth/login` | Inicio de sesión (devuelve el token) | No |
| GET | `/auth/me` | Datos del usuario con sesión iniciada | Sí |
| PATCH | `/user/{id}` | Guardar los cambios de Editar perfil | Sí |
| POST | `/escaneo` | Guardar un escaneo con su foto | Sí |
| GET | `/escaneo` | Historial de maderas escaneadas del perfil | Sí |
| DELETE | `/escaneo/{id}` | Borrar un escaneo del historial | Sí |

La API externa Open-Meteo (`GET /v1/forecast`) se consume aparte, sin pasar por Xano. Recibe la latitud y la longitud del lugar y devuelve la temperatura y la humedad actuales.

---

## Base de datos local (Room)

* `maderas`: copia del catálogo de Xano. Permite ver las maderas sin internet después de la primera carga.
* `usuario`: usuario con la sesión iniciada y su token de Xano. Reemplaza a SharedPreferences.

La primera vez hay que abrir la app con internet para descargar el catálogo.

---

## Pruebas unitarias

Hay 61 pruebas unitarias en `app/src/test/java/com/duoc/maderita`:

| Carpeta | Qué comprueba |
| :--- | :--- |
| `model` | Reglas de `ValidadorRegistro` y fotos de cada madera |
| `viewmodel` | Login, Registro, Carga, Catálogo, Perfil y Escáner con repositorios falsos |
| `repository` | Cada endpoint de Xano y la API del clima con un servidor local (MockWebServer) |

Para ejecutarlas, en la carpeta `Maderita`:

```bash
# Mac o Linux (si ./gradlew no tiene permiso de ejecución, usar: sh ./gradlew :app:testDebugUnitTest)
./gradlew :app:testDebugUnitTest

# Windows
.\gradlew.bat :app:testDebugUnitTest
```

También se pueden correr desde Android Studio: en la vista **Project**, clic derecho sobre el paquete `com.duoc.maderita` dentro de `test [unitTest]` y **Run Tests**.

Estas pruebas usan un servidor falso, por lo que no detectan cambios en la configuración real de Xano. Por eso la app también se prueba a mano contra Xano antes de cada entrega.

---

## Inicio de sesión y sesión guardada

* Inicio de sesión: el usuario ingresa su correo y contraseña. La app los envía a Xano (`auth/login`), recibe un token y pide los datos del usuario (`auth/me`). Si algo falla se muestra un mensaje en la pantalla.
* Sesión guardada: el usuario y su token se guardan en Room. Si no cierra sesión, al volver a abrir la app entra directo al Home.
* Cierre de sesión: el botón "Cerrar Sesión" del perfil marca la sesión como cerrada en Room y vuelve al login.
* Registro de usuario: formulario con validación de nombre, correo, contraseña (mínimo 8 caracteres y un número), confirmación de contraseña, perfil y términos (`RegistroScreen`, `RegistroViewModel` y `ValidadorRegistro`). Al validar, crea la cuenta en Xano (`auth/signup`) y envía el perfil elegido en el campo `perfil`.

---

## Estructura del Proyecto

```text
com.duoc.maderita
│
├── model/
│   ├── ImagenesMadera.kt
│   ├── Madera.kt
│   ├── Usuario.kt
│   └── ValidadorRegistro.kt
│
├── local/          (Room: entidades, DAOs y base de datos)
├── remote/         (Retrofit: APIs de Xano, API del clima y sus modelos)
│
├── repository/
│   ├── ClimaRepositorio.kt
│   ├── EscaneoRepositorio.kt
│   ├── MaderaRepositorio.kt
│   └── UsuarioRepositorio.kt
│
├── viewmodel/
│   ├── CargaViewModel.kt
│   ├── CatalogoViewModel.kt
│   ├── ClimaViewModel.kt
│   ├── DetalleMaderaViewModel.kt
│   ├── EscanerViewModel.kt
│   ├── FabricaViewModel.kt
│   ├── LoginViewModel.kt
│   ├── PerfilViewModel.kt
│   └── RegistroViewModel.kt
│
├── ui/
│   ├── ComponentesFormulario.kt
│   ├── DetalleMadera.kt
│   ├── EditarPerfil.kt
│   ├── HomeScreen.kt
│   ├── Login.kt
│   ├── Navegacion.kt
│   ├── PantallaCarga.kt
│   ├── PerfilUsuario.kt
│   ├── RegistroScreen.kt
│   ├── Scanner.kt
│   ├── TiposMadera.kt
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
│
├── MaderitaApp.kt
└── MainActivity.kt
```
