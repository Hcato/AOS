import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.secrets.gradle)
    alias(libs.plugins.jetbrainsKotlinSerialization)
    // Activa Hilt y KSP
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.hcato.hakai"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.hcato.hakai"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
        resValues = true
    }
    flavorDimensions.add("environment")
    productFlavors {

        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"

            buildConfigField("String", "BASE_URL", "\"http://44.193.188.243:3000/\"")

            buildConfigField("String", "AUTH_BASE_URL", "\"http://18.211.7.123:8000/\"") // <-- Nueva

            buildConfigField("String", "BASE_URL_STREAMING", "\"http://18.210.61.62/hls/estreno-123.m3u8\"")

            buildConfigField("String", "BASE_URL_STREAMING2", "\"http://18.206.107.28/Prueba_Miku_360part7_injected.mp4\"")

            resValue("string", "app_name", "Assistant Virtual (DEV)")
        }

        create("prod") {
            dimension = "environment"

            buildConfigField("String", "BASE_URL", "\"http://44.193.188.243:3000/\"")

            buildConfigField("String", "BASE_URL_STREAMING", "\"http://18.210.61.62/hls/estreno-123.m3u8\"")

            buildConfigField("String", "BASE_URL_STREAMING2", "\"http://18.206.107.28/Prueba_Miku_360part7_injected.mp4\"")

            resValue("string", "app_name", "AOS"
            )
        }
    }
}
ksp {
    arg("hilt.disableModulesHaveInstallInCheck", "true")
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}
secrets {
    propertiesFileName = "local.properties"
    defaultPropertiesFileName = "local.default.properties"
    ignoreList.add("sdk.dir")
}
dependencies {
    // AndroidX & Compose Base
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.compose.ui.text)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.animation.core)
    implementation(libs.androidx.runtime)
    implementation(libs.androidx.compose.runtime)

    // Retrofit & Network
    implementation(libs.com.squareup.retrofit2.retrofit)
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("io.socket:socket.io-client:2.1.0")

    // Media & Video
    implementation("androidx.media3:media3-exoplayer:1.2.0")
    implementation("androidx.media3:media3-ui:1.2.0")
    implementation("androidx.media3:media3-exoplayer-hls:1.2.0")
    implementation("org.videolan.android:libvlc-all:3.6.0")
    implementation("androidx.media:media:1.7.0")

    // --- FIREBASE (Única sección, sin duplicados y sin -ktx) ---
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-messaging")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    // Hilt (Inyección de dependencias)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // Room (Base de Datos)
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Navigation & DataStore
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.common.ktx)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Utils & Workers
    implementation(libs.io.coil.kt.coil.compose)
    implementation("io.github.sceneview:sceneview:2.2.1")
    implementation("com.google.mlkit:translate:17.0.2")
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
