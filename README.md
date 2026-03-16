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

## 🗺️ Maps Integration

The application uses Google Maps API to display race circuits. To enable this feature you need to provide a **Maps API Key**.

1. Obtain an API key from the Google Cloud Console (enable Maps SDK for Android).
2. Add the key to your environment file:

```
MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY
```


## ⚙️ Detalles de Implementación

### 🏁 Visualización de Carreras
La lista de carreras se gestiona en `F1RaceListScreen`, donde cada Gran Premio se representa mediante el componente `RaceCard`. Este componente destaca:
- El **número de ronda** en un bloque de color primario.
- El **nombre del GP y circuito** con tipografía enfatizada.
- Información de **ubicación y fecha** acompañada de iconos descriptivos.
- El uso de `LazyColumn` para una carga eficiente de la lista.

### 🕒 Gestión de Zonas Horarias
Para garantizar que el usuario nunca pierda una sesión, la aplicación implementa una lógica de conversión automática en `DateUtils.kt`:
1. Los datos se reciben en formato **UTC** desde la API de Ergast.
2. Se utiliza `SimpleDateFormat` configurado con `TimeZone.getTimeZone("UTC")` para el parseo inicial.
3. El resultado se formatea utilizando `TimeZone.getDefault()`, adaptando automáticamente las horas y fechas a la configuración regional del dispositivo del usuario.

### 🚩 Sistema de Banderas Dinámico
En lugar de almacenar cientos de recursos locales, la aplicación utiliza un mapeo inteligente en `FlagMapping.kt`:
- Un diccionario asocia nombres de países con sus códigos **ISO 3166-1 alpha-2**.
- Las imágenes se cargan dinámicamente desde la CDN **[flagcdn.com](https://flagcdn.com/)**.
- Esto reduce el tamaño del APK y permite actualizaciones rápidas de los activos visuales.

### 🌦️ Integración de Clima (Open-Meteo)
La aplicación integra datos meteorológicos en tiempo real para cada sesión (Prácticas, Clasificación, Carrera):
- **API**: Se utiliza **[Open-Meteo](https://open-meteo.com/)**, una API de código abierto que no requiere claves privadas.
- **Lógica**: Se envían las coordenadas exactas del circuito (latitud/longitud) y el rango de fechas de la sesión.
- **UI**: Los códigos meteorológicos de WMO se mapean a iconos visuales (`WbSunny`, `Cloud`, `WaterDrop`, etc.) y se muestra la temperatura prevista en grados Celsius.

### 📅 Calendario de Eventos Personalizado
Se ha implementado un sistema de calendario desde cero para gestionar la densidad de eventos de la F1:
- **Interfaz a Medida**: Un componente `CalendarGrid` que permite navegar por meses y visualizar los días del mes actual.
- **Agrupación Inteligente**: En `CalendarViewModel`, todas las sesiones (Libres, Clasificación, Sprint y Carrera) se agrupan por fecha para una consulta rápida.
- **Indicadores Visuales**: Los días con eventos programados muestran un punto rojo distintivo, permitiendo al usuario identificar rápidamente los fines de semana de carrera.
- **Navegación Contextual**: Al seleccionar un día, se despliega una lista de sesiones que vinculan directamente al detalle específico de la ronda correspondiente.

---

## ✅ Cumplimiento de Requisitos

Este proyecto ha sido desarrollado cumpliendo estrictamente con los siguientes requisitos técnicos:

1.  **Lenguaje Kotlin Idiomático**: Uso de las mejores prácticas de Kotlin, incluyendo `StateFlow`, corrutinas, clases selladas (`Sealed Classes`) y navegación segura con rutas tipadas.
2.  **Diseño Material 3**: Implementación completa de **Material Design 3**, utilizando componentes modernos como `Scaffold`, `TopAppBar`, `ModalNavigationDrawer` y el sistema de colores dinámicos.
3.  **Vistas y Layouts Versátiles**: Se demuestra el conocimiento de diversos elementos de UI:
    *   **Layouts**: `Box`, `Column`, `Row`, `LazyVerticalGrid` (Galeria), `LazyRow` (Fotos de carrera).
    *   **Componentes**: `Switch` (Notificaciones), `DropdownMenu` (Selector de idioma), `Cards`, `Buttons`, `OutlinedTextField`, entre otros.
4.  **Internacionalización (i18n)**: Aplicación totalmente traducida a dos idiomas: **Español** e **Inglés**.
5.  **Arquitectura Android**: Estructura sólida basada en:
    *   **Patrón Repositorio**: Para la gestión de fuentes de datos.
    *   **ViewModel**: Para la gestión del estado de la UI.
    *   **Hilt**: Para una inyección de dependencias robusta.
6.  **Uso de Observables**: Gestión reactiva del estado utilizando `Flow` y `StateFlow`.
7.  **Galería y Cámara**:
    *   **CameraX**: Implementado para capturar fotos personalizadas de las carreras.
    *   **MediaStore**: Uso de `GalleryHelper` para acceder a la galería del dispositivo.
8.  **Permisos en Tiempo de Ejecución**: Comprobación y solicitud proactiva de permisos para Cámara, Almacenamiento y Notificaciones.
9.  **Jetpack Navigation Component**: Navegación moderna y segura entre pantallas mediante el sistema de rutas tipadas.
10. **WorkerManager y Notificaciones**:
    *   **WorkManager**: Implementado en `SessionNotificationWorker` para tareas en segundo plano.
    *   **Notifications**: Sistema de alertas mediante `NotificationHelper`.
