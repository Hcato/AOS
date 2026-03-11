import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
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

            resValue("string", "app_name", "Assistant Virtual (DEV)")
        }

        create("prod") {
            dimension = "environment"

            buildConfigField("String", "BASE_URL", "\"http://44.193.188.243:3000/\"")

            buildConfigField("String", "BASE_URL_STREAMING", "\"http://18.210.61.62/hls/estreno-123.m3u8\"")

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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)   //viewModel()
    implementation(libs.com.squareup.retrofit2.retrofit)        // Retrofit
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")  // JSON
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation(libs.io.coil.kt.coil.compose)
    implementation(libs.androidx.navigation.compose)// Navigation
    implementation("io.github.sceneview:sceneview:2.2.1") //SceneView 3D
    implementation("androidx.media:media:1.7.0") //TTS Voice
    implementation("com.google.mlkit:translate:17.0.2") //Translate
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    implementation("androidx.datastore:datastore-preferences:1.1.1") //DataStore
    implementation(libs.hilt.android)                               // Implementación de Hilt
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.compose.ui.text)
    implementation(libs.androidx.compose.foundation.layout)                    // Integración con Jetpack Compose
    ksp(libs.hilt.compiler)                                         // KSP
    implementation("org.videolan.android:libvlc-all:3.6.0") //video
    implementation("io.socket:socket.io-client:2.1.0") //Socket
    implementation(libs.androidx.navigation.common.ktx)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.compose.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
