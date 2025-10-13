plugins {
    id("dev.deftu.componency.build.componency-multiplatform")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.common)
        }
    }
}
