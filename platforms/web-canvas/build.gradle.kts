plugins {
    id(libs.plugins.kotlin.js.get().pluginId)
    alias(libs.plugins.dgt.tools)
}

kotlin {
    explicitApi()

    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }

                outputFileName = "web-canvas.js"
            }

            binaries.executable()
        }
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
