import com.modrinth.minotaur.dependencies.DependencyType
import com.modrinth.minotaur.dependencies.ModDependency
import dev.deftu.gradle.utils.version.MinecraftVersions
import dev.deftu.gradle.utils.ModLoader
import dev.deftu.gradle.utils.includeOrShade

plugins {
    java
    kotlin("jvm")
    id("dev.deftu.gradle.multiversion")
    id("dev.deftu.gradle.tools")
    id("dev.deftu.gradle.tools.resources")
    id("dev.deftu.gradle.tools.publishing.maven")
    id("dev.deftu.gradle.tools.minecraft.loom")
    id("dev.deftu.gradle.tools.shadow")
    id("dev.deftu.gradle.tools.minecraft.releases")
}

kotlin.explicitApi()

toolkitMavenPublishing {
    artifactName.set("componency-minecraft")
}

toolkitLoomHelper {
    if (mcData.isForgeLike && mcData.version >= MinecraftVersions.VERSION_1_16_5) {
        useKotlinForForge()
    }
}

toolkitReleases {
    detectVersionType.set(true)

    modrinth {
        projectId.set("swfUx4gO")
        if (mcData.loader == ModLoader.FABRIC) {
            dependencies.addAll(listOf(
                ModDependency("Ha28R6CL", DependencyType.REQUIRED),                     // Fabric Language Kotlin
            ))
        } else if (mcData.version >= MinecraftVersions.VERSION_1_16_5) {
            dependencies.addAll(listOf(
                ModDependency("ordsPcFz", DependencyType.REQUIRED)                      // Kotlin for Forge
            ))
        }

        if (mcData.version >= MinecraftVersions.VERSION_1_16_5) {
            dependencies.add(ModDependency("T0Zb6DLv", DependencyType.REQUIRED))        // Textile
            dependencies.add(ModDependency("MaDESStl", DependencyType.REQUIRED))        // Omnicore
        }
    }
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
    api(includeOrShade("dev.deftu:isolated-lwjgl3-loader:0.3.2") {
        exclude(group = "org.apache")
        exclude(group = "org.intellij")
        exclude(group = "org.jetbrains")
    })

    // Minecraft
    modApi("dev.deftu:omnicore-$mcData:0.19.1")
}

tasks {
    fatJar {
        exclude("org/**/*")
        exclude("Log4j-config.xsd")
        exclude("Log4j-events.dtd")
        exclude("Log4j-events.xsd")
    }
}
