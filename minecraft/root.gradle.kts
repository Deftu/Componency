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
    val neoforge_1_21 = createNode("1.21-neoforge", 1_21_00, "srg")
    val fabric_1_21 = createNode("1.21-fabric", 1_21_00, "yarn")

    val neoforge_1_20_06 = createNode("1.20.6-neoforge", 1_20_06, "srg")
    val fabric_1_20_06 = createNode("1.20.6-fabric", 1_20_06, "yarn")

    val forge_1_20_04 = createNode("1.20.4-forge", 1_20_04, "srg")
    val neoforge_1_20_04 = createNode("1.20.4-neoforge", 1_20_04, "srg")
    val fabric_1_20_04 = createNode("1.20.4-fabric", 1_20_04, "yarn")

    val forge_1_20_02 = createNode("1.20.2-forge", 1_20_02, "srg")
    val neoforge_1_20_02 = createNode("1.20.2-neoforge", 1_20_02, "srg")
    val fabric_1_20_02 = createNode("1.20.2-fabric", 1_20_02, "yarn")

    val forge_1_20_01 = createNode("1.20.1-forge", 1_20_01, "srg")
    val fabric_1_20_01 = createNode("1.20.1-fabric", 1_20_01, "yarn")

    val forge_1_19_04 = createNode("1.19.4-forge", 1_19_04, "srg")
    val fabric_1_19_04 = createNode("1.19.4-fabric", 1_19_04, "yarn")

    val forge_1_19_02 = createNode("1.19.2-forge", 1_19_02, "srg")
    val fabric_1_19_02 = createNode("1.19.2-fabric", 1_19_02, "yarn")

    val forge_1_18_02 = createNode("1.18.2-forge", 1_18_02, "srg")
    val fabric_1_18_02 = createNode("1.18.2-fabric", 1_18_02, "yarn")

    val forge_1_17_01 = createNode("1.17.1-forge", 1_17_01, "srg")
    val fabric_1_17_01 = createNode("1.17.1-fabric", 1_17_01, "yarn"
    )
    val forge_1_16_05 = createNode("1.16.5-forge", 1_16_05, "srg")
    val fabric_1_16_05 = createNode("1.16.5-fabric", 1_16_05, "yarn")

    val forge_1_12_02 = createNode("1.12.2-forge", 1_12_02, "srg")

    val forge_1_08_09 = createNode("1.8.9-forge", 1_08_09, "srg")

    neoforge_1_21.link(fabric_1_21)
    fabric_1_21.link(fabric_1_20_06)
    neoforge_1_20_06.link(fabric_1_20_06)
    fabric_1_20_06.link(fabric_1_20_04)
    forge_1_20_04.link(fabric_1_20_04)
    neoforge_1_20_04.link(fabric_1_20_04)
    fabric_1_20_04.link(fabric_1_20_02)
    forge_1_20_02.link(fabric_1_20_02)
    neoforge_1_20_02.link(fabric_1_20_02)
    fabric_1_20_02.link(fabric_1_20_01)
    forge_1_20_01.link(fabric_1_20_01)
    fabric_1_20_01.link(fabric_1_19_04)
    forge_1_19_04.link(fabric_1_19_04)
    fabric_1_19_04.link(fabric_1_19_02)
    forge_1_19_02.link(fabric_1_19_02)
    fabric_1_19_02.link(fabric_1_18_02)
    forge_1_18_02.link(fabric_1_18_02)
    fabric_1_18_02.link(fabric_1_17_01)
    forge_1_17_01.link(fabric_1_17_01)
    fabric_1_17_01.link(fabric_1_16_05)
    fabric_1_16_05.link(forge_1_16_05)
    forge_1_16_05.link(forge_1_12_02)
    forge_1_12_02.link(forge_1_08_09)
}
