plugins {
    java
    kotlin("jvm")
    id("dev.deftu.gradle.multiversion")
    id("dev.deftu.gradle.tools")
    id("dev.deftu.gradle.tools.publishing.maven")
    id("dev.deftu.gradle.tools.minecraft.loom")
}

kotlin.explicitApi()

toolkitMavenPublishing {
    artifactName.set("componency-minecraft-tweaker-$mcData")
}

tasks {

    val generateVersionInfo by creating(WriteProperties::class.java) {
        destinationFile.set(layout.buildDirectory.file("/jars/versions.properties"))
        property("version", rootProject.version)
    }

    jar {
        from(generateVersionInfo)
        setOf(
            project(":"),
            project(":minecraft:$mcData")
        ).forEach { project ->
            from(project.tasks.jar)
        }
    }

}
