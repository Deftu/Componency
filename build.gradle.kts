import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    kotlin("multiplatform") version("2.0.10")
    val dgtVersion = "2.33.3"
    id("dev.deftu.gradle.tools") version(dgtVersion)
    id("dev.deftu.gradle.tools.publishing.maven") version(dgtVersion)
}

allprojects {
    if (
        !project.displayName.contains("minecraft") &&
        !project.displayName.contains("example")
    ) {
        println("Applying Kotlin Multiplatform set up to ${project.displayName}")

        apply(plugin = "org.jetbrains.kotlin.multiplatform")
        apply(plugin = "dev.deftu.gradle.tools")
        apply(plugin = "dev.deftu.gradle.tools.publishing.maven")

        kotlin {
            explicitApi()

            // --- JVM (Desktop, Android, Server) ---
            jvm()

            // --- JavaScript (Browser, Node.js) ---
            js(IR) {
                generateTypeScriptDefinitions()
                binaries.executable()
                browser()
                nodejs()
            }

            // --- WebAssembly (Experimental) ---
            @OptIn(ExperimentalWasmDsl::class)
            wasmJs {
                generateTypeScriptDefinitions()
                binaries.executable()
                browser {
                    commonWebpackConfig {
                        devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                            static = (static ?: mutableListOf()).apply {
                                add(rootProject.path)
                            }
                        }
                    }
                }
            }

            // --- Native (Commonly Used Platforms) ---
            linuxX64()       // Desktop Linux
            mingwX64()       // Windows native
            macosX64()       // macOS Intel
            macosArm64()     // macOS Apple Silicon

            iosArm64()       // iOS devices
            iosSimulatorArm64() // iOS simulator for Apple Silicon

            sourceSets {
                val commonMain by getting {
                    dependencies {
                        implementation("dev.deftu:stateful:0.4.0")
                        implementation("dev.deftu:textile:0.14.1")
                    }
                }

                val commonTest by getting {
                    dependencies {
                        implementation(kotlin("test"))
                    }
                }

                val jvmMain by getting {
                    dependencies {
                        implementation(kotlin("reflect"))
                    }
                }

                val jvmTest by getting {
                    dependencies {
                        implementation(kotlin("test-junit"))
                    }
                }

                val jsTest by getting {
                    dependencies {
                        implementation(kotlin("test-js"))
                    }
                }
            }
        }
    }
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":primitives"))
                implementation(project(":platform"))
            }
        }
    }
}
