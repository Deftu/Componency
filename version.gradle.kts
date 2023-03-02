import xyz.deftu.gradle.utils.GameSide

plugins {
    `java-library`
    kotlin("jvm")
    id("xyz.deftu.gradle.multiversion")
    id("xyz.deftu.gradle.tools")
    id("xyz.deftu.gradle.tools.minecraft.loom")
}

loomHelper {
    disableRunConfigs(GameSide.SERVER)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("xyz.deftu:StateKt:1.0.1")
    modImplementation("xyz.deftu:MultiCraft-${mcData.versionStr}-${mcData.loader.name}:1.0.0")
    modImplementation("xyz.deftu:TextCraft-${mcData.versionStr}-${mcData.loader.name}:1.0.0")

    // We need FAPI for testing purposes
    if (mcData.isFabric) {
        modCompileOnly(modRuntimeOnly("net.fabricmc.fabric-api:fabric-api:${when (mcData.version) {
            11903 -> "0.75.1+1.19.3"
            11902 -> "0.61.0+1.19.2"
            11802 -> "0.58.0+1.18.2"
            11701 -> "0.46.1+1.17"
            11605 -> "0.41.3+1.16"
            11502 -> "0.28.5+1.15"
            else -> throw IllegalStateException("Invalid MC version: ${mcData.version}")
        }}")!!)
    }
}
