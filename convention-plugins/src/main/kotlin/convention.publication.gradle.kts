/*
 * Copyright (c) 2023 Adrian Witaszak - CharLEE-X. Use of this source code is governed by the Apache 2.0 license.
 */

import java.util.Properties

plugins {
    `maven-publish`
    signing
    id("org.jetbrains.dokka")
}

ext["signing.key"] = null
ext["signing.password"] = null
ext["ossrhUsername"] = null
ext["ossrhPassword"] = null

val secretPropsFile = project.rootProject.file("local.properties")
if (secretPropsFile.exists()) {
    secretPropsFile.reader().use {
        Properties().apply {
            load(it)
        }
    }.onEach { (name, value) ->
        ext[name.toString()] = value
    }
} else {
    ext["signing.key"] = System.getenv("SIGNING_KEY")
    ext["signing.password"] = System.getenv("SIGNING_PASSWORD")
    ext["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
    ext["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
}

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

val signingTasks = tasks.withType<Sign>()
tasks.withType<AbstractPublishToMaven>().configureEach {
    dependsOn(signingTasks)
}

afterEvaluate {
    publishing {
        repositories {
            maven {
                name = "sonatype"
                setUrl("https://s01.oss.sonatype.org/service/local/")
                credentials {
                    username = getExtraString("ossrhUsername")
                    password = getExtraString("ossrhPassword")
                }
            }
        }

        publications.withType<MavenPublication> {
            artifact(javadocJar.get())

            pom {
                name.set(AppConfig.projectName)
                description.set(AppConfig.description)
                url.set(AppConfig.url)

                licenses {
                    license {
                        name.set(AppConfig.Licence.name)
                        url.set(AppConfig.Licence.url)
                    }
                }
                developers {
                    developer {
                        id.set(AppConfig.Developer.id)
                        name.set(AppConfig.Developer.name)
                        email.set(AppConfig.Developer.email)
                    }
                }
                scm {
                    url.set(AppConfig.url)
                }
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(
        getExtraString("signing.key"),
        getExtraString("signing.password")
    )
    sign(publishing.publications)
}

fun getExtraString(name: String): String = ext[name]?.toString()
    ?: findProperty(name)?.toString()
    ?: System.getenv(name)?.toString()
    ?: error("Property '$name' not found")
