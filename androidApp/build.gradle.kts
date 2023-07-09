plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose")
}

group = ProjectConfig.groupId + ".${ProjectConfig.artifactId}.android"
version = ProjectConfig.version

android {
    namespace = project.group.toString()
    compileSdk = ProjectConfig.androidCompileSdk
    defaultConfig {
        applicationId = project.group.toString()
        minSdk = ProjectConfig.androidMinSdk
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
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = ProjectConfig.javaVersion
        targetCompatibility = ProjectConfig.javaVersion
    }
    kotlinOptions {
        jvmTarget = ProjectConfig.jvmTarget
    }
}

dependencies {
    implementation(projects.compose)
    implementation(compose.ui)
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.material)
}
