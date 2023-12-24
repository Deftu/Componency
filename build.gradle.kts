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

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("dev.deftu:Statefulness:1.0.0")
    modImplementation("dev.deftu:TextCraft-${mcData.versionStr}-${mcData.loader.name}:1.1.1")
    modImplementation("dev.deftu:MultiCraft-${mcData.versionStr}-${mcData.loader.name}:0.1.0")

    // We need FAPI for testing purposes
    if (mcData.isFabric) {
        "modImplementation"("net.fabricmc.fabric-api:fabric-api:${mcData.fabricApiVersion}")
    }
}
