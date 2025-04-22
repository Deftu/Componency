toolkitMavenPublishing {
    artifactName.set("componency-defign")
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
