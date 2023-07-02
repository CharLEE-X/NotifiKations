/**
 * Copyright 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
//    id("convention.publication")
    id("org.jetbrains.dokka") version "1.8.20"
    id("maven-publish")
    id("signing")
}

group = ProjectConfig.groupId + ".${ProjectConfig.artifactId}"
version = ProjectConfig.version

kotlin {
    jvmToolchain(ProjectConfig.jvmTargetInt)

    android {
        publishLibraryVariants("release")
    }
    ios {
        binaries {
            framework {
                baseName = project.name
            }
        }
    }
    iosSimulatorArm64()

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.coroutines.core)
                implementation(libs.kotlin.uuid)
                implementation(libs.kotlin.dateTime)
                implementation(libs.kotlin.serialization)
                implementation(libs.kotlin.kermit)
                implementation(libs.multiplatformSettings)
            }
        }
        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.androidx.core)
                implementation(libs.androidx.securityCrypto)
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by getting {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }
}

android {
    namespace = project.group.toString()
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

tasks.check.dependsOn(tasks.detektAll)

val dokkaOutputDir = "$buildDir/dokka"

tasks.dokkaHtml {
    outputDirectory.set(file(dokkaOutputDir))
}

val deleteDokkaOutputDir by tasks.register<Delete>("deleteDokkaOutputDirectory") {
    delete(dokkaOutputDir)
}

val javadocJar = tasks.register<Jar>("javadocJar") {
    dependsOn(deleteDokkaOutputDir, tasks.dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaOutputDir)
}

publishing {
    repositories {
        maven {
            name = "Sonatype"
            setUrl {
                val repositoryId =
                    System.getenv("SONATYPE_REPOSITORY_ID") ?: error("Missing env variable: SONATYPE_REPOSITORY_ID")
                "https://oss.sonatype.org/service/local/staging/deployByRepositoryId/${repositoryId}/"
            }
            credentials {
                username = System.getenv("SONATYPE_USERNAME")
                password = System.getenv("SONATYPE_PASSWORD")
            }
        }
        maven {
            name = "Snapshot"
            setUrl { "https://oss.sonatype.org/content/repositories/snapshots/" }
            credentials {
                username = System.getenv("SONATYPE_USERNAME")
                password = System.getenv("SONATYPE_PASSWORD")
            }
        }
    }

    publications {
        withType<MavenPublication> {
            artifact(javadocJar)
            pom {
                name.set(ProjectConfig.projectName)
                description.set(ProjectConfig.description)
                url.set(ProjectConfig.url)
                licenses {
                    license {
                        name.set("MIT license")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                issueManagement {
                    system.set("Github")
                    url.set("https://github.com/CharLEE-X/notifiKations/issues")
                }
                scm {
                    connection.set("https://github.com/CharLEE-X/notifiKations.git")
                    url.set("")
                }
                developers {
                    developer {
                        name.set("Adrian Witaszak")
                        id.set("charlee-dev")
                        email.set("adrianwitaszak@gmail.com")
                    }
                }
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(
        System.getenv("GPG_PRIVATE_KEY"),
        System.getenv("GPG_PRIVATE_PASSWORD")
    )
    sign(publishing.publications)
}
