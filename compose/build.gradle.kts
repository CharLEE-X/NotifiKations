import com.android.build.gradle.internal.tasks.factory.dependsOn

/**
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.compose")
}

group = ProjectConfig.groupId + ".${ProjectConfig.artifactId}.compose"
version = ProjectConfig.version

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()
    jvmToolchain(ProjectConfig.jvmTargetInt)

    android()
    ios()
    iosSimulatorArm64()

    cocoapods {
        summary = ProjectConfig.description
        homepage = ProjectConfig.url
        version = ProjectConfig.version
        ios.deploymentTarget = ProjectConfig.iosDevelopmentTarget
        framework {
            baseName = project.name
            isStatic = true
        }
    }

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.notifications)
                implementation(compose.ui)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.animation)
                implementation(compose.animationGraphics)
                implementation(compose.material)
                implementation(compose.materialIconsExtended)
                implementation(libs.kotlin.dateTime)
                implementation("com.charleex.notifikations:notifications:0.0.1")
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
    compileSdk = ProjectConfig.androidCompileSdk
    defaultConfig {
        minSdk = ProjectConfig.androidMinSdk
    }
    compileOptions {
        sourceCompatibility = ProjectConfig.javaVersion
        targetCompatibility = ProjectConfig.javaVersion
    }
}
