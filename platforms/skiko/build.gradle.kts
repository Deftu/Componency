import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id(libs.plugins.kotlin.multiplatform.get().pluginId)
    alias(libs.plugins.dgt.tools)
}

kotlin {
    explicitApi()

    jvm {
        withJava()
    }

    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }

                outputFileName = "skiko.js"
            }

            binaries.executable()
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }

                outputFileName = "skiko.wasm.js"
            }

            binaries.executable()
        }
    }

    macosX64()
    macosArm64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.skiko.core)
        }

        jvmMain.dependencies {
            val osName = System.getProperty("os.name")
            val hostOs = when {
                osName == "Mac OS X" -> "macos"
                osName.startsWith("Win") -> "windows"
                osName.startsWith("Linux") -> "linux"
                else -> error("Unsupported OS: $osName")
            }

            val hostArch = when (val osArch = System.getProperty("os.arch")) {
                "x86_64", "amd64" -> "x64"
                "aarch64" -> "arm64"
                else -> error("Unsupported arch: $osArch")
            }

            with(libs.skiko.awt.get()) {
                implementation("${this.group}:${this.name}-$hostOs-$hostArch:${this.version}")
            }
        }

        jsMain.dependencies {
            implementation(libs.skiko.js)
        }

        wasmJsMain.dependencies {
            implementation(libs.skiko.js)
        }
    }
}
