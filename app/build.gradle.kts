plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
    alias(libs.plugins.google.ksp)
}

android {
    namespace = "com.andmar.flint"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.andmar.flint"
        minSdk = 24
        targetSdk = 37
        versionCode = 5
        versionName = "1.1"

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
            applicationIdSuffix = ".rustore"
            versionNameSuffix = "-rustore"
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
    "roomImplementation"(libs.androidx.room.runtime)
    "kspRoom"(libs.androidx.room.compiler)
    "roomImplementation"(libs.androidx.room.ktx)

    "firebaseImplementation"(platform(libs.firebase.bom))
    "firebaseImplementation"(libs.firebase.analytics)
    "firebaseImplementation"(libs.firebase.auth)
    "firebaseImplementation"(libs.firebase.firestore)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}