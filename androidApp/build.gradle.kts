plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "com.helios.sunverta.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.helios.sunverta.android"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(projects.shared)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.material3.windowSizeClass)
    implementation(libs.compose.material3.adaptiveNavigationSuite)
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.material.extended.icons)

    // Hilt
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.ext.compiler)

    // Coil
    implementation(libs.coilCompose)

    // Ktor
    implementation(libs.ktor.android)

    // Lottie
    implementation(libs.lottieCompose)

    kspAndroidTest(libs.hiltAndroidCompiler)
    debugImplementation(libs.compose.ui.tooling)
    androidTestImplementation(libs.hiltTesting)
}