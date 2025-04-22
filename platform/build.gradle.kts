toolkitMavenPublishing {
    artifactName.set("componency-platform")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":primitives"))
            }
        }
    }
}
