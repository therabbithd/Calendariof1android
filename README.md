# Universal Motorsport Timing Calendar (UMTC)

An Android application built with modern technologies to keep track of the Formula 1 race calendar.

## 📱 Pantallas de la Aplicación

La aplicación consta de tres pantallas principales, todas diseñadas con una estética "Motorsport Dark" premium:

1.  **Calendario de Carreras (`F1RaceListScreen`)**:
    *   Muestra una lista completa de las rondas del campeonato.
    *   Cada carrera se presenta en una tarjeta con el número de ronda destacado, nombre del gran premio, circuito y fecha.
    *   Navegación directa a los detalles de cada carrera.

2.  **Detalle de Carrera (`F1RaceDetailScreen`)**:
    *   Muestra información expandida del gran premio seleccionado.
    *   Incluye una tarjeta de detalles del circuito con su ubicación.
    *   **Cronograma de Sesiones**: Un timeline visual que detalla los horarios de Prácticas, Clasificación, Sprint y el propio Gran Premio.

3.  **Inicio de Sesión (`LoginScreen`)**:
    *   Interfaz para la autenticación de usuarios en la plataforma.

## 🛠️ Tecnologías Utilizadas

Este proyecto utiliza el stack tecnológico más moderno recomendado para el desarrollo de Android:

*   **[Jetpack Compose](https://developer.android.com/compose)**: Toolkit moderno para construir interfaces de usuario nativas de forma declarativa.
*   **[Material Design 3](https://m3.material.io/)**: Última evolución del sistema de diseño de Google, adaptado con colores personalizados para una estética de competición.
*   **[Hilt (Dagger)](https://developer.android.com/training/dependency-injection/hilt-android)**: Repositorio oficial de Google para la inyección de dependependencias.
*   **[Retrofit](https://square.github.io/retrofit/) & [Gson](https://github.com/google/gson)**: Cliente HTTP para el consumo de APIs REST y parseo de datos JSON.
*   **[Room Database](https://developer.android.com/training/data-storage/room)**: Capa de abstracción sobre SQLite para la persistencia de datos local.
*   **[Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)**: Para la gestión de tareas asíncronas y flujos de datos reactivos.
*   **[Navigation Compose](https://developer.android.com/develop/ui/compose/navigation)**: Gestión de la navegación entre pantallas mediante rutas tipadas.
*   **[Kotlinx Serialization](https://kotlinlang.org/docs/serialization.html)**: Utilizado para la serialización de datos y navegación segura.
*   **[Coil](https://coil-kt.github.io/coil/)**: Librería de carga de imágenes optimizada para Kotlin y Compose.
*   **[KSP (Kotlin Symbol Processing)](https://kotlinlang.org/docs/ksp-overview.html)**: Para la generación de código eficiente en tiempo de compilación (usado por Room e Hilt).

## 🏗️ Arquitectura

La aplicación sigue los principios de la **Arquitectura Limpia (Clean Architecture)** y el patrón **MVVM (Model-View-ViewModel)**:

*   **UI Layer**: Manejada por Compose y ViewModels.
*   **Domain Layer**: Lógica de negocio y modelos de datos.
*   **Data Layer**: Repositorios que gestionan la obtención de datos desde la API (Remoto) o la base de datos (Local/Room).
