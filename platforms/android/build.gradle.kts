import dev.deftu.gradle.utils.ProjectData

plugins {
    alias(libs.plugins.android.library)
    id(libs.plugins.kotlin.android.get().pluginId)
    alias(libs.plugins.dgt.tools)
    alias(libs.plugins.dgt.publishing.maven)
}

kotlin {
    explicitApi()
}

android {
    namespace = "dev.deftu.componency.android" // Set your module's package
    compileSdk = 35 // or whatever is latest

    defaultConfig {
        minSdk = 21
        //noinspection OldTargetApi
        targetSdk = 34
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(kotlin("stdlib"))
}

publishing {
    publications {
        create<MavenPublication>("release") {
            afterEvaluate {
                // Android requires waiting until after evaluation to access components
                from(components["release"])

                val projectData = ProjectData.from(rootProject)
                groupId = projectData.group
                artifactId = "componency-platform-${project.name}"
                version = projectData.version
            }
        }
    }
}
