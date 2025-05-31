import groovy.lang.MissingPropertyException

pluginManagement {
    repositories {
        // Default repositories
        gradlePluginPortal()
        mavenCentral()
        google()

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
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        maven("https://maven.deftu.dev/releases")
        maven("https://maven.deftu.dev/snapshots")
        mavenLocal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.8.+")
}

val projectName: String = extra["project.name"]?.toString()
    ?: throw MissingPropertyException("project.name has not been set.")
rootProject.name = projectName

// Core modules
":modules".let { baseProjectPath ->
    val basePath = baseProjectPath.removePrefix(":")
    file(basePath).takeIf { !it.exists() }?.mkdirs()
    include(baseProjectPath)
    logger.lifecycle("Including core modules at $basePath")

    setOf(
        // Platform
        "primitives", "platform",

        // Core
        "core",

        // Utilities
        "navigation", "gifs", "videos",
        // "text-input", // TODO: Re-enable when text input is ready

        // Design
        // "defign", // TODO: Re-enable when design system is ready
    ).map { "$baseProjectPath:$it" }.forEach { moduleProjectPath ->
        val moduleName = moduleProjectPath.removePrefix(":").replace(":", "/")
        file(moduleName).takeIf { !it.exists() }?.mkdirs()
        include(moduleProjectPath)
        logger.lifecycle("Including core module project '$moduleName' at ${project(moduleProjectPath).projectDir}")
    }
}

// Examples
":examples".let { baseProjectPath ->
    val basePath = baseProjectPath.removePrefix(":")
    file(basePath).takeIf { !it.exists() }?.mkdirs()
    logger.lifecycle("Including example projects at $basePath")

    include(baseProjectPath)

    setOf(
        // Basic example
        "basic",

        // LWJGL3 example
        "lwjgl3",
    ).map { "$baseProjectPath:$it" }.forEach { exampleProjectPath ->
        val exampleName = exampleProjectPath.removePrefix(":").replace(":", "/")
        file(exampleName).takeIf { !it.exists() }?.mkdirs()
        include(exampleProjectPath)
        logger.lifecycle("Including example project '$exampleName' at ${project(exampleProjectPath).projectDir}")
    }
}

// Platforms
":platforms".let { baseProjectPath ->
    val basePath = baseProjectPath.removePrefix(":")
    file(basePath).takeIf { !it.exists() }?.mkdirs()
    logger.lifecycle("Including platform projects at $basePath")

    include(baseProjectPath)

    setOf(
        "swing", "android", "ios",
        "desktop", "lwjgl3-nanovg", "skiko",
        "web-canvas", "web-webgpu", "web-compat",
    ).map { "$baseProjectPath:$it" }.forEach { platformProjectPath ->
        val platformName = platformProjectPath.removePrefix(":").replace(":", "/")
        file(platformName).takeIf { !it.exists() }?.mkdirs()
        include(platformProjectPath)
        logger.lifecycle("Including platform project '$platformName' at ${project(platformProjectPath).projectDir}")
    }
}

// Minecraft platform
//include(":minecraft")
//project(":minecraft").buildFileName = "root.gradle.kts"
//listOf(
//    "1.8.9-fabric",
//    "1.8.9-forge",
//
//    "1.12.2-fabric",
//    "1.12.2-forge",
//
//    "1.16.5-forge",
//    "1.16.5-fabric",
//
//    "1.17.1-forge",
//    "1.17.1-fabric",
//
//    "1.18.2-forge",
//    "1.18.2-fabric",
//
//    "1.19.2-forge",
//    "1.19.2-fabric",
//
//    "1.19.4-forge",
//    "1.19.4-fabric",
//
//    "1.20.1-forge",
//    "1.20.1-fabric",
//
//    "1.20.4-forge",
//    "1.20.4-neoforge",
//    "1.20.4-fabric",
//
//    "1.20.6-neoforge",
//    "1.20.6-fabric",
//
//    "1.21.1-neoforge",
//    "1.21.1-fabric",
//
//    "1.21.4-neoforge",
//    "1.21.4-fabric"
//).forEach { version ->
//    include(":minecraft:$version")
//    project(":minecraft:$version").apply {
//        projectDir = file("minecraft/versions/$version")
//        buildFileName = "../../build.gradle.kts"
//    }
//}
