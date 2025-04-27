import com.modrinth.minotaur.dependencies.DependencyType
import com.modrinth.minotaur.dependencies.ModDependency
import dev.deftu.gradle.utils.version.MinecraftVersions
import dev.deftu.gradle.utils.ModLoader
import dev.deftu.gradle.utils.includeOrShade

plugins {
    java
    id(libs.plugins.kotlin.jvm.get().pluginId)
    id(libs.plugins.dgt.multiversion.versioned.get().pluginId)
    id(libs.plugins.dgt.tools.get().pluginId)
    id(libs.plugins.dgt.resources.get().pluginId)
    id(libs.plugins.dgt.publishing.maven.get().pluginId)
    id(libs.plugins.dgt.minecraft.loom.get().pluginId)
    id(libs.plugins.dgt.shadow.get().pluginId)
    id(libs.plugins.dgt.minecraft.releases.get().pluginId)
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
    mavenCentral()
}

dependencies {
    api(includeOrShade(project(":modules:core"))!!)

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
    modApi("dev.deftu:omnicore-$mcData:0.25.0")
    implementation(kotlin("stdlib-jdk8"))
}

tasks {
    fatJar {
        exclude("org/**/*")
        exclude("Log4j-config.xsd")
        exclude("Log4j-events.dtd")
        exclude("Log4j-events.xsd")
    }
}
kotlin {
    jvmToolchain(8)
}