import xyz.deftu.gradle.utils.GameSide

plugins {
    `java-library`
    kotlin("jvm")
    id("xyz.deftu.gradle.multiversion")
    id("xyz.deftu.gradle.tools")
    id("xyz.deftu.gradle.tools.minecraft.api")
    id("xyz.deftu.gradle.tools.minecraft.loom")
    id("xyz.deftu.gradle.tools.minecraft.releases")
}

toolkitLoomApi.setupTestClient()
toolkitLoomHelper.disableRunConfigs(GameSide.SERVER)

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("xyz.deftu:StateKt:1.0.4")
    modImplementation("xyz.deftu:MultiCraft-${mcData.versionStr}-${mcData.loader.name}:1.0.0")
    modImplementation("xyz.deftu:TextCraft-${mcData.versionStr}-${mcData.loader.name}:1.0.1")

    // We need FAPI for testing purposes
    if (mcData.isFabric) {
        modCompileOnly(modRuntimeOnly("net.fabricmc.fabric-api:fabric-api:${mcData.fabricApiVersion}")!!)
    }
}
