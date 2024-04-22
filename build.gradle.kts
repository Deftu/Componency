import dev.deftu.gradle.utils.GameSide

plugins {
    `java-library`
    kotlin("jvm")
    id("dev.deftu.gradle.multiversion")
    id("dev.deftu.gradle.tools")
    id("dev.deftu.gradle.tools.resources")
    id("dev.deftu.gradle.tools.maven-publishing")
    id("dev.deftu.gradle.tools.minecraft.api")
    id("dev.deftu.gradle.tools.minecraft.loom")
    id("dev.deftu.gradle.tools.minecraft.releases")
}

toolkitLoomApi.setupTestClient()
toolkitLoomHelper.disableRunConfigs(GameSide.SERVER)
kotlin.explicitApi()

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("dev.deftu:stateful:0.1.0")
    modImplementation("dev.deftu:textile-${mcData.versionStr}-${mcData.loader.name}:0.1.0")
    modImplementation("dev.deftu:omnicore-${mcData.versionStr}-${mcData.loader.name}:0.1.0")

    // We need FAPI for testing purposes
    if (mcData.isFabric) {
        "modCompileOnly"("modRuntimeOnly"("net.fabricmc.fabric-api:fabric-api:${mcData.fabricApiVersion}")!!)
    }
}
