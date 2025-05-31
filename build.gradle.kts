import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply(false)

    alias(libs.plugins.android.library) apply(false)

    alias(libs.plugins.dgt.tools) apply(false)
    alias(libs.plugins.dgt.publishing.maven) apply(false)

}

subprojects {
    plugins.withId(rootProject.libs.plugins.kotlin.multiplatform.get().pluginId) {
        extensions.configure<KotlinMultiplatformExtension> {
            val moduleName = "componency-${project.name}"

            targets.withType<KotlinNativeTarget>().configureEach {
                // Apply unique module name
                compilations.configureEach {
                    compileTaskProvider.configure {
                        compilerOptions.freeCompilerArgs.add("-module-name=$moduleName")
                    }
                }

                // Only create framework binaries for Apple targets
                if (konanTarget.family == Family.IOS || konanTarget.family == Family.OSX ||
                    konanTarget.family == Family.TVOS || konanTarget.family == Family.WATCHOS) {

                    binaries.framework {
                        baseName = "componency-${project.name}"
                    }
                }
            }
        }
    }

    plugins.withId("maven-publish") {
        extensions.configure<PublishingExtension> {
            publications.withType<MavenPublication>().configureEach {
                val base = "componency-${project.name}"

                // Keep the KMP suffixes like -jvm, -js, etc.
                val suffix = name.removePrefix(project.name).removePrefix("kotlinMultiplatform")
                artifactId = if (suffix.isNotEmpty()) "$base-$suffix" else base
            }
        }
    }
}
