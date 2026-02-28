# AOS - Anime On Streaming 📺

Aplicación móvil para Android diseñada para la transmisión de Anime en vivo tipo "Estrenos". El objetivo principal es ofrecer una experiencia multimedia fluida y altamente interactiva donde los usuarios pueden visualizar contenido en tiempo real (streaming HLS) y participar con una comunidad de espectadores.

[🔗 Ver Prototipo en Figma](https://www.figma.com/design/xVtfqoJ19ZN7alHe9Z7CnO/Untitled?node-id=0-1&t=2nQWIMUKN56hgpiz-1)

## Características Principales

* **Streaming de Baja Latencia:** Reproducción de video en vivo utilizando LibVLC y Nginx RTMP.
* **Interacción en Tiempo Real:** Conexión bidireccional mediante WebSockets (Socket.io) para contabilizar espectadores concurrentes y registrar "Me gusta" de manera instantánea.
* **Identidad sin Fricción:** Sistema de interacciones únicas basado en el `ANDROID_ID` del dispositivo, eliminando la necesidad de un login tradicional y protegiendo la privacidad del usuario.

## Tech Stack

* **UI:** Jetpack Compose (Material 3).
* **Lenguaje:** Kotlin.
* **Arquitectura:** MVVM (Model-View-ViewModel) + Unidirectional Data Flow (UDF).
* **Inyección de Dependencias:** Hilt (Dagger) estandarizado a `javax.inject`.
* **Asincronismo y Reactividad:** Coroutines & Kotlin Flows (`callbackFlow`).
* **Red y Tiempo Real:** Retrofit (REST) y Socket.io.
* **Multimedia:** LibVLC.
* **Backend / Infraestructura (Contexto):** Node.js, Redis, Nginx RTMP.

## Arquitectura y Flujo de Datos

La aplicación implementa el patrón **Unidirectional Data Flow (UDF)** bajo una arquitectura **MVVM**. La comunicación con el backend es híbrida:
1.  **REST:** Para acciones transaccionales (ej. dar "Like").
2.  **WebSockets:** Para eventos asíncronos masivos (ej. conteo de espectadores).

### Flujo de Interacción en Tiempo Real
1.  **Suscripción:** Al iniciar la pantalla, el `ViewModel` activa el `Repository`.
2.  **Observación:** El `Repository` transforma los eventos de Socket.io en un `Flow` reactivo mediante `callbackFlow`.
3.  **Actualización:** El `ViewModel` recolecta el `Flow` y emite un nuevo `UiState` (Única fuente de verdad).
4.  **Renderizado:** Jetpack Compose detecta el cambio de estado y aplica una recomposición eficiente **solo en los widgets de contadores**, sin afectar la reproducción del video.

### 1. Detalles de la Serie
Muestra la sinopsis, estudio, idioma y año. El botón **"COMENZAR A VER"** indica que hay episodios normales disponibles.
![Detalles de la serie](ruta/a/tu/imagen_detalles.png)

### 2. Alerta de Estreno en Vivo
Cuando el servidor detecta un flujo de video (Nginx RTMP), el botón cambia a **"EN VIVO"**, invitando al usuario a unirse a la sala de streaming simultáneo.
![Alerta En Vivo](ruta/a/tu/imagen_envivo.png)

### 3. Reproductor y Sala Interactiva (Landscape)
Reproducción a pantalla completa (HLS). Una capa UI impulsada por Compose y WebSockets muestra:
* Indicador "En vivo" (superior derecha).
* Contador de espectadores en tiempo real (inferior izquierda).
* Botón interactivo de "Me gusta" (inferior derecha).

Desarrollado con Kotlin y Jetpack Compose.