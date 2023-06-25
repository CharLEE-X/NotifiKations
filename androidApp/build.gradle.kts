plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose")
}

group = libs.versions.groupId.get()
version = libs.versions.version.get()

android {
    namespace = project.group.toString()
    compileSdk = 33
    defaultConfig {
        applicationId = project.group.toString()
        minSdk = 26
        targetSdk = targetSdk
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
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
