plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
    id("maven-publish")
}

group = libs.versions.groupId.get()
version = libs.versions.version.get()

kotlin {
    android {
        publishLibraryVariants("release", "debug")
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
            dependencies {
                implementation(libs.androidx.core)
                implementation(libs.androidx.securityCrypto)
            }
        }
    }

    afterEvaluate {
        publishing {
            publications {
                create<MavenPublication>("release") {
                    groupId = libs.versions.groupId.get()
                    artifactId = libs.versions.artifactId.get()
                    version = libs.versions.version.get()
                }
            }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}
