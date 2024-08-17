import dev.deftu.gradle.utils.MinecraftVersion

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

val lwjglVersion = when {
    mcData.version >= MinecraftVersion.VERSION_1_20_6 -> "3.3.3"
    mcData.version >= MinecraftVersion.VERSION_1_20_2 -> "3.3.2"
    mcData.version >= MinecraftVersion.VERSION_1_19_2 -> "3.3.1"
    mcData.version >= MinecraftVersion.VERSION_1_16_5 -> "3.2.2"
    else -> "3.3.3"
}

preprocess {
    vars.put("LWJGL", run {
        val regex = "(?<major>\\d+).(?<minor>\\d+).?(?<patch>\\d+)?".toRegex()
        val match = regex.find(lwjglVersion) ?: throw IllegalArgumentException("Invalid version format: $lwjglVersion")
        val groups = match.groups

        val major = groups["major"]?.value?.toInt() ?: throw IllegalArgumentException("Invalid version format: $version")
        val minor = groups["minor"]?.value?.toInt() ?: throw IllegalArgumentException("Invalid version format: $version")
        val patch = groups["patch"]?.value?.toInt() ?: 0

        major * 10000 + minor * 100 + patch
    })
}

toolkitMavenPublishing {
    artifactName.set("componency-minecraft-$mcData")
}

repositories {
    maven("https://repo.polyfrost.org/releases")
}

dependencies {
    api(project(":"))

    // LWJGL
    api("org.lwjgl:lwjgl-nanovg:$lwjglVersion")
    api("org.lwjgl:lwjgl-stb:$lwjglVersion")
    api("dev.deftu:lwjgl3-bootstrap:0.1.0") {
        exclude(module = "lwjgl-opengl")
    }

    // Minecraft
    modApi("dev.deftu:omnicore-$mcData:0.10.0")
}
