plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.wolt"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.wolt"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    // Allow references to generated code
    kapt {
        correctErrorTypes = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/gradle/incremental.annotation.processors"
        }
    }
}

dependencies {
    // Retrofit2 and Serialization
    implementation(libs.retrofit)
    implementation(libs.retrofit2.kotlinx.serialization.converter) // Retrofit Kotlinx converter
    implementation(libs.kotlinx.serialization.json) // Kotlinx Serialization JSON
    implementation(libs.converter.gson) // Gson converter
    implementation(libs.gson) // Gson library

    // OkHTTP
    implementation(libs.okhttp)
    implementation(libs.okhttp.interceptor)

    // Compose
    implementation(libs.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom.v20250100))
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    //Coil-Kt (image loading library for Android)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Hilt (Dependency Injection)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // Room (Database)
    implementation(libs.androidx.room.ktx)

    // AndroidX Core and Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom.v20250100))
    androidTestImplementation(libs.ui.test.junit4)

    // MultiDex (for large apps)
    implementation(libs.androidx.multidex)

    // Desugaring
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}



