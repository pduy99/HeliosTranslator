import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.kotlinSerialization)
}

val buildConfigGenerator by tasks.registering(Sync::class) {
    var baseUrl = "YOUR_BASE_URL"
    try {
        val properties = Properties()
        val localProperties = File("./local.properties")
        if (localProperties.isFile) {
            InputStreamReader(FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
                properties.load(reader)
            }
        } else error("File from not found")
        baseUrl = properties.getProperty("BASE_URL")
    } catch (ignore: Exception) {

    }

    from(
        resources.text.fromString(
            """
        |package com.helios.kmptranslator
        |
        |object BuildKonfig {
        |  const val BASE_URL = "$baseUrl"
        |}
        |
      """.trimMargin()
        )
    ) {
        rename { "BuildKonfig.kt" } // set the file name
        into("com/helios/kmptranslator") // change the directory to match the package
    }

    into(layout.buildDirectory.dir("generated-src/kotlin/"))
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "16.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            kotlin.srcDir(
                buildConfigGenerator.map { it.destinationDir }
            )
            dependencies {
                implementation(libs.sqlDelightRuntime)
                implementation(libs.sqlDelightCoroutinesExtensions)
                implementation(libs.ktor.core)
                implementation(libs.ktor.serialization)
                implementation(libs.ktor.serialization.json)
                implementation(libs.ktor.client.logging)
                implementation(libs.kotlinDateTime)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.assertK)
                implementation(libs.turbine)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.android)
                implementation(libs.sqlDelight.android.driver)
                implementation(libs.slf4j.simple)
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.ktor.ios)
                implementation(libs.sqlDelight.native.driver)
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
    task("testClasses")
}

android {
    namespace = "com.helios.kmptranslator"
    compileSdk = 34
    defaultConfig {
        minSdk = 25
    }
}

sqldelight {
    database("TranslateDatabase") {
        packageName = "com.helios.kmptranslator.database"
        sourceFolders = listOf("sqldelight")
    }
}

tasks.named("build") {
    dependsOn(buildConfigGenerator)
}