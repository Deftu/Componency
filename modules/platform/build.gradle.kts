kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":modules:primitives"))
            }
        }
    }
}
