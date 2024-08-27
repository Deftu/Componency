import dev.deftu.gradle.utils.includeOrShade

plugins {
    java
    kotlin("jvm")
    id("dev.deftu.gradle.multiversion")
    id("dev.deftu.gradle.tools")
    id("dev.deftu.gradle.tools.resources")
    id("dev.deftu.gradle.tools.shadow")
    id("dev.deftu.gradle.tools.publishing.maven")
    id("dev.deftu.gradle.tools.minecraft.loom")
}

kotlin.explicitApi()

toolkitMavenPublishing {
    artifactName.set("componency-minecraft-$mcData")
}

repositories {
    maven("https://repo.polyfrost.org/releases")
}

dependencies {
    api(includeOrShade(project(":"))!!)

    // LWJGL
    val lwjglVersion = "3.3.3"
    api("org.lwjgl:lwjgl-nanovg:$lwjglVersion")
    api("org.lwjgl:lwjgl-stb:$lwjglVersion")
    api("dev.deftu:isolated-lwjgl3-loader:0.3.2") {
        exclude(module = "lwjgl-opengl")
    }

    // Minecraft
    modApi("dev.deftu:omnicore-$mcData:0.10.0")
}
