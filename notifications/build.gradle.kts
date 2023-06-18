plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
}

group = "com.charleex.notifications"
version = "1.0-SNAPSHOT"

kotlin {
    android {
        publishAllLibraryVariants()
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
                implementation(libs.kotlin.uuid)
                implementation(libs.kotlin.dateTime)
                implementation(libs.kotlin.serialization)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.core)
            }
        }
        val androidUnitTest by getting {
            dependsOn(commonTest)
            dependencies {}
        }
        val androidInstrumentedTest by getting {
            dependsOn(commonTest)
        }
    }
}

android {
    namespace = project.group.toString()
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}
