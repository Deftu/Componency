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

    includeBuild("build-logic")
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

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

val projectName: String = extra["project.name"]?.toString()
    ?: throw MissingPropertyException("project.name has not been set.")
rootProject.name = projectName

// Base modules
setOf(
    "common",
    "layout",
    "canvas",
    "tree",
    "input",
    "animation",
    "a11y",
    "platform",
    "navigation",
    "media",
    "assets",
).forEach { moduleName ->
    val modulePath = ":$moduleName"
    include(modulePath)
    file(moduleName).prepare()
}

":text".let { projectPath ->
    setOf(
        "common",
        "cache",
        "shaper",
        "shaper-harfbuzz",
        "shaper-platform",
    ).forEach { moduleName ->
        val modulePath = "$projectPath-$moduleName"
        include(modulePath)
        file(modulePath.removePrefix(":")).prepare()
    }
}

":renderer".let { projectPath ->
    setOf(
        "api",
        "skia",
        "opengl",
        "nanovg",
        "vulkan",
        "webcanvas",
        "webgpu",
    ).forEach { moduleName ->
        val modulePath = "$projectPath-$moduleName"
        include(modulePath)
        file(modulePath.removePrefix(":")).prepare()
    }
}

":platforms".let { projectPath ->
    setOf(
        "android",
        "ios",
        "desktop",
        "swing",
        "web",
        // TODO: "minecraft"
    ).forEach { moduleName ->
        val modulePath = "$projectPath-$moduleName"
        include(modulePath)
        file(modulePath.removePrefix(":")).prepare()
    }
}

":devtools".let { projectPath ->
    setOf(
        "inspector",
        "profiler",
        "snapshot",
        "conformance",
    ).forEach { moduleName ->
        val modulePath = "$projectPath-$moduleName"
        include(modulePath)
        file(modulePath.removePrefix(":")).prepare()
    }
}

":examples".let { projectPath ->
    setOf(
        "basic",
        "glfw-opengl",
        "glfw-nanovg",
        "web-canvas",
        "android",
        // TODO: "minecraft"
    ).forEach { moduleName ->
        val modulePath = "$projectPath-$moduleName"
        include(modulePath)
        file(modulePath.removePrefix(":")).prepare()
    }
}

fun File?.prepare() {
    this ?: return
    if (!exists()) {
        mkdirs()
    }

    val gitkeep = resolve(".gitkeep")
    if (!gitkeep.exists()) {
        gitkeep.createNewFile()
    }
}
