plugins {
    java
    kotlin("jvm")
    id("dev.deftu.gradle.tools")
    id("dev.deftu.gradle.tools.publishing.maven")
}

kotlin.explicitApi()

toolkitMavenPublishing {
    artifactName.set("componency-defign")
}

dependencies {
    api(project(":primitives"))
    api(project(":platform"))
    api(project(":"))
}
