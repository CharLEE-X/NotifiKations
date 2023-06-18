plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose")
}

group = "com.charleex.application"
version = "1.0-SNAPSHOT"

android {
    namespace = project.group.toString()
    compileSdk = 33
    defaultConfig {
        applicationId = project.group.toString()
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeMultiplatform.get()
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":compose"))
    implementation(compose.ui)
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.material)
}
