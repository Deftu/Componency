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
    implementation("dev.deftu:stateful:0.1.0")
    modImplementation("dev.deftu:textful-${mcData.versionStr}-${mcData.loader.name}:0.1.1")
    modImplementation("dev.deftu:multicraft-${mcData.versionStr}-${mcData.loader.name}:0.1.1")

    for (module in listOf(
        "nanovg",
        "tinyfd"
    )) {
        val dep = "org.lwjgl:lwjgl-$module:3.3.1"
        implementation(dep) {
            isTransitive = false
        }

        for (natives in listOf(
            "windows",
            "windows-arm64",
            "linux",
            "macos",
            "macos-arm64"
        )) {
            implementation("$dep:natives-$natives") {
                isTransitive = false
            }
        }
    }

    // We need FAPI for testing purposes
    if (mcData.isFabric) {
        "modCompileOnly"("modRuntimeOnly"("net.fabricmc.fabric-api:fabric-api:${mcData.fabricApiVersion}")!!)
    }
}
