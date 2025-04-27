subprojects {
    pluginManager.withPlugin("java") {
        dependencies {
            "implementation"(project(":modules:primitives"))
            "implementation"(project(":modules:platform"))
            "implementation"(project(":modules:core"))
        }
    }
}
