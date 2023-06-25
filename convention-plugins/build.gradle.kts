/*
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    `kotlin-dsl` // Is needed to turn our build logic written in Kotlin into Gralde Plugin
}

repositories {
    gradlePluginPortal() // To use 'maven-publish' and 'signing' plugins in our own plugin
}
