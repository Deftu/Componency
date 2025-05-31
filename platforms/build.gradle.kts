import dev.deftu.gradle.tools.publishing.MavenPublishingExtension
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

subprojects {
    apply(plugin = rootProject.libs.plugins.dgt.tools.get().pluginId)
    apply(plugin = rootProject.libs.plugins.dgt.publishing.maven.get().pluginId)

    extensions.configure<MavenPublishingExtension>("toolkitMavenPublishing") {
        artifactName.set("componency-platform-${project.name}")
    }

    pluginManager.withPlugin(rootProject.libs.plugins.kotlin.multiplatform.get().pluginId) {
        extensions.configure<KotlinMultiplatformExtension> {
            explicitApi()

            // --- JVM (Desktop, Android, Server) ---
            jvm {
                @OptIn(ExperimentalKotlinGradlePluginApi::class)
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8)
                }

                withJava()
                withSourcesJar()
            }

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

            if (project.findProperty("componency.skipNative")?.toString()?.toBoolean() != true) {
                // --- Native (Commonly Used Platforms) ---
                linuxX64()       // Desktop Linux
                mingwX64()       // Windows native
                macosX64()       // macOS Intel
                macosArm64()     // macOS Apple Silicon

                iosArm64()       // iOS devices
                iosSimulatorArm64() // iOS simulator for Apple Silicon
            }

            sourceSets {
                commonMain.dependencies {
                    implementation(project(":modules:primitives"))
                    implementation(project(":modules:platform"))
                    implementation(project(":modules:core"))

                    implementation(rootProject.libs.kolor)
                    implementation(rootProject.libs.stateful)
                    implementation(rootProject.libs.textile)
                }

                commonTest.dependencies {
                    implementation(kotlin("test"))
                }

                jvmMain.dependencies {
                    implementation(kotlin("reflect"))
                }

                jvmTest.dependencies {
                    implementation(kotlin("test-junit"))
                }

                jsTest.dependencies {
                    implementation(kotlin("test-js"))
                }
            }
        }

        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(8))
            }
        }
    }

    fun Project.applyNonKmpDependencies() {
        dependencies {
            "implementation"(project(":modules:primitives"))
            "implementation"(project(":modules:platform"))
            "implementation"(project(":modules:core"))

            "implementation"(rootProject.libs.kolor)
            "implementation"(rootProject.libs.stateful)
            "implementation"(rootProject.libs.textile)
        }
    }

    pluginManager.withPlugin(rootProject.libs.plugins.kotlin.jvm.get().pluginId) {
        applyNonKmpDependencies()
    }

    pluginManager.withPlugin(rootProject.libs.plugins.kotlin.js.get().pluginId) {
        applyNonKmpDependencies()
    }

    pluginManager.withPlugin(rootProject.libs.plugins.kotlin.android.get().pluginId) {
        applyNonKmpDependencies()
    }

    pluginManager.withPlugin("java") {
        applyNonKmpDependencies()
    }
}

