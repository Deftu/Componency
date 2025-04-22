toolkitMavenPublishing {
    artifactName.set("componency-navigation")
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
