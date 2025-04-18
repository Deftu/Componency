plugins {
    `java-library`
    kotlin("jvm")
    id("dev.deftu.gradle.tools")
}

dependencies {
    api(project(":primitives"))
    api(project(":platform"))
    api(project(":"))
}
