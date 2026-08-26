plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
    alias(libs.plugins.google.ksp)
}

android {
    namespace = "ru.andmar.flint"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "ru.andmar.flint"
        minSdk = 26
        targetSdk = 37
        versionCode = 16
        versionName = "1.3.1"

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
    }
    flavorDimensions += listOf("data")
    productFlavors {
        create("firebase") {
            dimension = "data"
        }
        create("room") {
            dimension = "data"
            applicationIdSuffix = ".room"
            versionNameSuffix = "-room"
        }
    }

    tasks.configureEach {
        // Если имя текущей задачи сборки содержит "room" и относится к Google Services, отключаем её
        if (name.contains("room", ignoreCase = true) && name.contains("GoogleServices", ignoreCase = true)) {
            enabled = false
        }
    }
}

dependencies {
    // --- ВЕРСИЯ (ROOM) ---
    "roomImplementation"(libs.androidx.room.runtime)
    "roomImplementation"(libs.androidx.room.ktx)
    "kspRoom"(libs.androidx.room.compiler)

    // --- ВЕРСИЯ (FIREBASE) ---
    "firebaseImplementation"(platform(libs.firebase.bom))
    "firebaseImplementation"(libs.firebase.analytics)
    "firebaseImplementation"(libs.firebase.auth)
    "firebaseImplementation"(libs.firebase.firestore)

    // --- ОБЩИЕ ЗАВИСИМОСТИ ---
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlinx.serialization.json)


    implementation(libs.accompanist.permissions)


    //WorkManager
    implementation(libs.androidx.work.runtime.ktx)

    //Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)


    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Preview
    implementation(libs.androidx.compose.ui.tooling.preview)

    // --- ТЕСТЫ И ОТЛАДКА ---
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}