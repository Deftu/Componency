plugins {
    java
    kotlin("jvm")
    val dgtVersion = "2.26.0"
    id("dev.deftu.gradle.tools") version(dgtVersion)
    id("dev.deftu.gradle.tools.java") version(dgtVersion)
    id("dev.deftu.gradle.tools.kotlin") version(dgtVersion)
    id("dev.deftu.gradle.tools.resources") version(dgtVersion)
    id("dev.deftu.gradle.tools.publishing.maven") version(dgtVersion)
}

kotlin.explicitApi()

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":primitives"))
    implementation(project(":platform"))
    api("dev.deftu:stateful:0.3.0")
    api("dev.deftu:textile:0.11.1")
}
