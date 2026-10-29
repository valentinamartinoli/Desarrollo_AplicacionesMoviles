# Maderita

Maderita es una aplicación móvil nativa para Android desarrollada con Jetpack Compose y Kotlin. Su objetivo principal es ofrecer una herramienta intuitiva para el escaneo, identificación y consulta de información detallada sobre distintos tipos de maderas (como Pino Radiata, Roble, Raulí y Coigüe), integrada con un sistema de perfiles de usuario y un diseño visual coherente.

---

## Enlaces del Proyecto

* Tablero de Planificación (Trello): https://trello.com/b/9MepU1rH/maderita
* Repositorio Oficial (GitHub): https://github.com/valentinamartinoli/Desarrollo_AplicacionesMoviles

---

## Características Principales

* Pantalla de Carga (Splash Screen) Personalizada: Animación de carga sincronizada mediante CargaViewModel y la API de AndroidX SplashScreen con el logo principal (`@drawable/logo_splash`).
* Escáner e Identificación: Módulo interactivo con animación de línea de escaneo para procesar lecturas de madera y desplegar información detallada o simular errores de captura.
* Catálogo de Maderas: Muestrario interactivo en la pantalla principal (`HomeScreen`) cargado desde `MaderaRepositorio`.
* Perfil de Usuario: Visualización de información personal, historial de maderas escaneadas y opciones de sesión desde `PerfilUsuario`.
* Diseño Verde/Crema Consistente: Paleta de colores orgánicos y componentes limpios ajustados a la experiencia visual de la marca.

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
* Arquitectura: MVVM (Model-View-ViewModel)
* Gestión de Estado: `StateFlow` y `mutableStateOf`
* Navegación: Jetpack Navigation Compose
* Asincronía: Kotlin Coroutines (`viewModelScope`)
* Splash Screen: `androidx.core:core-splashscreen`

---

## Estructura del Proyecto

```text
com.duoc.maderita
│
├── model/
│   ├── Madera.kt
│   └── Usuario.kt
│
├── repository/
│   ├── MaderaRepositorio.kt
│   └── UsuarioRepositorio.kt
│
├── viewmodel/
│   └── CargaViewModel.kt
│
├── ui/
│   ├── HomeScreen.kt
│   ├── Scanner.kt
│   ├── PerfilUsuario.kt
│   ├── PantallaCargaLogo.kt
│   └── theme/
│       └── MaderitaTheme.kt
│
└── MainActivity.kt
