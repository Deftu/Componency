plugins {
    java
    kotlin("jvm")
    id("dev.deftu.gradle.tools")
    id("dev.deftu.gradle.tools.publishing.maven")
}

kotlin.explicitApi()

toolkitMavenPublishing {
    artifactName.set("componency-platform")
}

dependencies {
    api(project(":primitives"))
}
