plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.compose")
}

group = libs.versions.groupId.get()
version = libs.versions.version.get()

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    ios()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        framework {
            baseName = "compose"
            isStatic = true
        }
    }

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":notifications"))
                implementation(compose.ui)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.animation)
                implementation(compose.animationGraphics)
                implementation(compose.material)
                implementation(compose.materialIconsExtended)
                implementation(libs.kotlin.dateTime)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.activity)
                implementation(libs.androidx.material)
                implementation(compose.preview)
            }
        }
        val iosMain by getting
    }
}

android {
    namespace = project.group.toString()
    compileSdk = 33
    defaultConfig {
        minSdk = 26
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}
