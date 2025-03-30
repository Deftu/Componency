import groovy.lang.MissingPropertyException

pluginManagement {
    repositories {
        // Default repositories
        gradlePluginPortal()
        mavenCentral()

        // Repositories
        maven("https://maven.deftu.dev/releases")
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net")
        maven("https://repo.essential.gg/repository/maven-public")
        maven("https://server.bbkr.space/artifactory/libs-release/")
        maven("https://jitpack.io/")

        // Snapshots
        maven("https://maven.deftu.dev/snapshots")
        maven("https://s01.oss.sonatype.org/content/groups/public/")
        mavenLocal()
    }

    plugins {
        kotlin("jvm") version("2.0.0")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.8.+")
}

val projectName: String = extra["project.name"]?.toString()
    ?: throw MissingPropertyException("project.name has not been set.")
rootProject.name = projectName

// Platform
include(":primitives") // Primitive values such as colors, vectors, etc.
include(":platform") // Platform API

// Utilities
include(":text-input") // Text input component

// Defign
include(":defign") // Implementation of Deftu's design system

// Minecraft implementation
include(":minecraft")
project(":minecraft").buildFileName = "root.gradle.kts"
listOf(
    "1.8.9-fabric",
    "1.8.9-forge",

    "1.12.2-fabric",
    "1.12.2-forge",

    "1.16.5-forge",
    "1.16.5-fabric",

    "1.17.1-forge",
    "1.17.1-fabric",

    "1.18.2-forge",
    "1.18.2-fabric",

    "1.19.2-forge",
    "1.19.2-fabric",

    "1.19.4-forge",
    "1.19.4-fabric",

    "1.20.1-forge",
    "1.20.1-fabric",

    "1.20.4-forge",
    "1.20.4-neoforge",
    "1.20.4-fabric",

    "1.20.6-neoforge",
    "1.20.6-fabric",

    "1.21.1-neoforge",
    "1.21.1-fabric",

    "1.21.4-neoforge",
    "1.21.4-fabric"
).forEach { version ->
    include(":minecraft:$version")
    project(":minecraft:$version").apply {
        projectDir = file("minecraft/versions/$version")
        buildFileName = "../../build.gradle.kts"
    }
}

// Usage examples
include(":example-basic")
include(":example-lwjgl3")
