/**
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    kotlin("multiplatform") version libs.versions.kotlin apply false
    kotlin("plugin.serialization") version libs.versions.kotlin apply false
    id("com.android.application") version libs.versions.agp apply false
    id("com.android.library") version libs.versions.agp apply false
    id("org.jetbrains.compose") version libs.versions.composeMultiplatform apply false
    id("org.jetbrains.dokka") version libs.versions.dokka apply false
    id("org.jlleitschuh.gradle.ktlint") version libs.versions.ktLint
    id("io.gitlab.arturbosch.detekt") version libs.versions.detekt
}

allprojects {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

subprojects {
    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    configure<KtlintExtension> {
        filter {
            exclude { element -> element.file.path.contains("/build/") }
        }
        debug.set(false)
        outputToConsole.set(true)
    }

    apply(plugin = "io.gitlab.arturbosch.detekt")
    detekt {
        parallel = true
        config.setFrom(files(rootProject.file("tools/detekt/config.yml")))
        autoCorrect = true
    }

    tasks.withType<Detekt>().configureEach {
        jvmTarget = ProjectConfig.jvmTarget
        parallel = true
        reports {
            xml.required.set(false)
            html.required.set(false)
            txt.required.set(false)
            sarif.required.set(false)
        }
        exclude { it.file.absolutePath.contains("resources/") }
        exclude { it.file.absolutePath.contains("build/") }
        include("**/*.kt")
        include("**/*.kt")
    }

    tasks.withType<DetektCreateBaselineTask>().configureEach {
        this.jvmTarget = ProjectConfig.jvmTarget
        exclude { it.file.absolutePath.contains("resources/") }
        exclude { it.file.absolutePath.contains("build/") }
        include("**/*.kt")
    }

    tasks.register("detektAll") {
        group = "verification"
        description = "Runs all detekt tasks"
        dependsOn(tasks.withType<Detekt>())
    }
}
