import dev.deftu.gradle.utils.ModData
import dev.deftu.gradle.utils.ProjectData

plugins {
    id("dev.deftu.gradle.multiversion-root")
}

subprojects {
    val projectData = ProjectData.from(rootProject)
    ModData.populateFrom(project, projectData)
}

preprocess {
    val forge_1_12_02 = createNode("1.12.2-forge", 1_12_02, "srg")
    val forge_1_08_09 = createNode("1.8.9-forge", 1_08_09, "srg")

    forge_1_12_02.link(forge_1_08_09)
}
