# SunVerta

SunVerta is a comprehensive translation application built with Kotlin Compose Multiplatform (KMP),
offering multiple translation methods:

- Text Translation: Direct text input translation
- Image Translation: Camera-based OCR and translation
- Conversation Translation: Real-time conversation translation

The project utilizes Kotlin Multiplatform to share business logic and data layers across platforms,
while maintaining native UI experiences through Jetpack Compose (Android) and SwiftUI (iOS). This
architecture ensures optimal performance and platform-specific design guidelines on each supported
platform.

## Platforms

| ![](https://img.shields.io/badge/Android-black.svg?style=for-the-badge&logo=android) | ![](https://img.shields.io/badge/iOS-black.svg?style=for-the-badge&logo=apple) |
|:------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------:|
|                                          ✅                                           |                                Underdevelopment                                |

## Features and Showcase

### Text Translation

https://github.com/user-attachments/assets/522a4095-fbbe-4468-ab15-241d2c4e9941

### Image Translation

https://github.com/user-attachments/assets/2a19682b-f3bc-448e-86c1-93b19626e2eb

### Conversation Translation

https://github.com/user-attachments/assets/49edbeac-cb45-4ba9-addd-264bd467f2d8

### Translation History
<img src="https://github.com/user-attachments/assets/a56c99d2-9dda-4ab2-8821-6be82fcd4eac" width="295" alt="Translation History Screen">

## Architecture

SunVerta follows Clean Architecture principles with a Multiplatform-first approach, organizing code
into the following layers:

```
shared/                         # Root shared module for cross-platform code
├── core/                       # Core functionality shared across features
│   ├── data/                   # Data layer implementation
│   │   ├── datasource/         # Abstract data source interfaces
│   │   ├── repository/         # Repository implementations
│   │   └── model/              # Data models
│   ├── network/                # Network-related implementations
│   │   ├── dto/                # Data Transfer Objects for API
│   │   └── DataSourceImpl      # Network data source implementation
│   ├── database/               # Local database implementations
│   │   └── DataSourceImpl      # Database data source implementation
│   ├── domain/                 # Business logic layer
│   │   ├── model/              # Domain models
│   │   ├── usecase/            # Business logic use cases
│   └── presentation/           # Shared UI logic/components
├── features/                   # Feature modules
│   └── featureA/               # Individual feature module
│       ├── ViewModel           # Shared ViewModel 
│       ├── UiState             # Shared UiState
│       └── UiEvent             # Shared UiEvent
├── sqldelight/                 # Database configuration
│     └── database/             # Database setup and queries
├── androidApp/                 # Android-specific implementation
│   ├── core/                   # Core functionality shared across features specific to Android
│   ├── app/                    # Android app configuration
│   │   ├── di/                 # Dependency injection setup
│   │   └── navigation/         # Navigation configuration
│   │       └── NavHost.kt      # App NavHost
│   └── features/               # Android feature implementations
│       └── featureA/           # Platform-specific feature code
            ├── di/             # Dependency injection setup for feature
│           ├── components/     # Composables in feature
│           ├── navigation/     # Feature navigation route
│           └── presentation/  
│               ├── ViewModel   # Feature ViewModel
│               └── Screen      # Composable screen
└── iosApp/                     # iOS-specific implementation  
```

## Built with

- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) - The Kotlin Multiplatform
  technology is designed to simplify the development of cross-platform projects.
- [Jetpack Compose](https://developer.android.com/compose) - The Android’s recommended modern
  toolkit for building native UI. It simplifies and accelerates UI development on Android. Quickly
  bring your app to life with less code, powerful tools, and intuitive Kotlin APIs.
- [Ktor](https://ktor.io/) - Ktor is a framework for building HTTP clients for Kotlin.
- [SQLDelight](https://github.com/cashapp/sqldelight) - SQLDelight is an open-source library
  developed by Cash App (formerly Square, Inc.) for working with SQL databases in Kotlin-based
  Android and multi-platform applications.
- [CameraX](https://developer.android.com/training/camerax) - CameraX is a Jetpack library for
  camera integration in Android apps.
- [MLKit](https://developers.google.com/ml-kit/vision/translate) - MLKit is a library that provides
  machine learning capabilities for translation, object detection, and image classification.
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - Hilt is a
  dependency injection library for Android that reduces the boilerplate of doing manual dependency
  injection in your project
- [Type Safe Compose Navigation](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-navigation-routing.html) -
  Type safety in Kotlin DSL and Navigation Compose
- [Kotlinx-serialization](https://github.com/Kotlin/kotlinx.serialization) - Kotlin multiplatform /
  multi-format serialization.
- [Material3](https://m3.material.io/) - Material 3 is the latest version of Google’s open-source
  design system.
- [Kermit](https://github.com/Touchlab/Kermit) - a Kotlin Multiplatform centralized logging utility.
- [CircleCI](https://circleci.com/) - A CI/CD tool that helps automate workflows.

## Build and Run
Follow these steps to build and run the project locally:
1. **Clone the Repository**
2. **Open in Android Studio**
3. **Get your own DeepL free API Key**
   - Visit [DeepL Support](https://support.deepl.com/hc/en-us/articles/360020695820-API-Key-for-DeepL-s-API#h_01HM9MFQ195GTHM93RRY63M18W) for more information.
5. **Configure Local Properties**
   - Create a local.properties file in the root project directory.
   - Add the following properties:
     
     ```
      BASE_URL=deepl-api-end-point
      API_KEY=your-api-key-here
     ```
6.  **Build and Run**

## Contributing 🤝

Feel free to contribute to this project by submitting issues, pull requests, or providing valuable feedback. Your
contributions are always welcome! 🙌
