plugins {
    java
    kotlin("jvm")
    id("dev.deftu.gradle.tools")
    id("dev.deftu.gradle.tools.publishing.maven")
}

kotlin.explicitApi()

toolkitMavenPublishing {
    artifactName.set("componency-video-component")
}

dependencies {
    api(project(":primitives"))
    api(project(":platform"))
    api(project(":"))

    api("org.bytedeco:javacv-platform:1.5.11")
}
