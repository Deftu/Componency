toolkitMavenPublishing {
    artifactName.set("componency-text-input")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":"))
            }
        }
    }
}
