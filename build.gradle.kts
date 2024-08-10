plugins {
    `java-library`
    kotlin("jvm")
    val dgtVersion = "2.6.0"
    id("dev.deftu.gradle.tools") version(dgtVersion)
    id("dev.deftu.gradle.tools.resources") version(dgtVersion)
    id("dev.deftu.gradle.tools.publishing.maven") version(dgtVersion)
}

kotlin.explicitApi()

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api("dev.deftu:stateful:0.2.0")
    api("dev.deftu:textile:0.5.2")
}
