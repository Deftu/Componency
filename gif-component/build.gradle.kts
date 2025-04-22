toolkitMavenPublishing {
    artifactName.set("componency-gif-component")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":primitives"))
                implementation(project(":platform"))
                implementation(project(":"))
            }
        }
    }
}
