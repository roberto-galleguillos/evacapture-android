# EvaCapture

> **Origen:** proyecto desarrollado en septiembre de 2024, bajo una cuenta de GitHub institucional de la Universidad de Santiago de Chile (USACH) usada durante mis estudios de pregrado. Este repositorio es una edición de portafolio, migrada y curada de ese proyecto original, con historial nuevo. Republicada en mi cuenta personal en agosto de 2026.

Aplicación Android nativa en Kotlin para organización académica personal: registro de ramos, cálculo de nota final por porcentaje de teoría y laboratorio, y una pantalla de calendario en desarrollo.

Sin backend porque no lo necesita: es una app de uso individual, así que la persistencia con `SharedPreferences` es suficiente y evita la complejidad de una base de datos local (Room) para un caso de uso tan simple.

## Stack

- Kotlin
- Android SDK 34 (minSdk 24)
- AndroidX + Jetpack Compose (dependencias habilitadas; las pantallas actuales usan Views/XML clásico)
- Material Components
- Persistencia local con `SharedPreferences` (sin backend ni base de datos)
- Gradle Kotlin DSL

## Pantallas

| Activity | Estado | Descripción |
|---|---|---|
| `MainActivity` | Funcional | Menú principal: navega a Ramos y Calendario. El botón de ajustes está sin implementar. |
| `RamosActivity` | Funcional | Alta/listado de ramos (teoría / teoría+laboratorio), persistidos en `SharedPreferences`. |
| `NotasActivity` | Funcional | Cálculo de nota final a partir de notas y porcentajes de teoría/laboratorio. |
| `CalendarActivity` | Placeholder | Pantalla con navegación de vuelta, sin lógica de calendario todavía. |

## Ejecutar en local

```bash
./gradlew assembleDebug
```

Requiere Android SDK 34 instalado (o Android Studio, que lo resuelve automáticamente). No requiere backend ni configuración adicional: toda la persistencia es local al dispositivo.

## Estructura

```text
app/src/main/java/com/galeca/evacapture/   Activities (Main, Ramos, Notas, Calendar)
app/src/main/res/                          layouts, drawables, strings
app/src/test/, app/src/androidTest/        tests unitarios / instrumentados (plantilla por defecto)
```
