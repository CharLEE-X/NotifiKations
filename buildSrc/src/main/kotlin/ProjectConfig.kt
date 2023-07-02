import org.gradle.api.JavaVersion

/**
 * Copyright (c) 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

object ProjectConfig {
    const val projectName = "NotifiKations"
    const val groupId = "com.charleeex"
    const val artifactId = "notifikations"
    const val version = "0.0.1"
    const val description = "Kotlin Multiplatform library (Android + iOS)"
    const val url = "https://github.com/CharLEE-X/notifiKations"

    const val iosDevelopmentTarget = "14.1"

    const val jvmTargetInt = 17
    const val jvmTarget = jvmTargetInt.toString()

    const val androidMinSdk = 26
    const val androidCompileSdk = 33
    const val androidTargetSdk = androidCompileSdk
    val javaVersion = JavaVersion.VERSION_17
}
