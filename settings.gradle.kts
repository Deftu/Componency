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

val projectName: String = extra["project.name"]?.toString()
    ?: throw MissingPropertyException("project.name has not been set.")
rootProject.name = projectName

include(":example-basic")
include(":example-lwjgl3")

// Minecraft implementation
include(":minecraft")
project(":minecraft").buildFileName = "root.gradle.kts"
listOf(
    "1.8.9-forge",

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

    "1.20.2-forge",
//    "1.20.2-neoforge",
    "1.20.2-fabric",

    "1.20.4-forge",
//    "1.20.4-neoforge",
    "1.20.4-fabric",

//    "1.20.6-neoforge",
    "1.20.6-fabric",

//    "1.21-neoforge",
    "1.21-fabric"
).forEach { version ->
    include(":minecraft:$version")
    project(":minecraft:$version").apply {
        projectDir = file("minecraft/versions/$version")
        buildFileName = "../../build.gradle.kts"
    }
}

include(":minecraft-tweaker")
project(":minecraft-tweaker").buildFileName = "root.gradle.kts"
listOf(
    "1.8.9-forge",
    "1.12.2-forge",
).forEach { version ->
    include(":minecraft-tweaker:$version")
    project(":minecraft-tweaker:$version").apply {
        projectDir = file("minecraft-tweaker/versions/$version")
        buildFileName = "../../build.gradle.kts"
    }
}
