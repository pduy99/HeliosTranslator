plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "com.helios.kmptranslator.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.helios.kmptranslator.android"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
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
    implementation(platform("androidx.compose:compose-bom:2024.01.00"))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.material.extended.icons)

    // Hilt
    implementation(libs.hiltAndroid)
    kapt(libs.hiltAndroidCompiler)
    kapt(libs.hiltCompiler)
    implementation(libs.hiltNavigationCompose)

    // Coil
    implementation(libs.coilCompose)

    // Ktor
    implementation(libs.ktor.android)

    kaptAndroidTest(libs.hiltAndroidCompiler)
    debugImplementation(libs.compose.ui.tooling)
    androidTestImplementation(libs.hiltTesting)
}