//#if MC <= 1.12.2
package dev.deftu.componency.minecraft

import cc.polyfrost.lwjgl.bootstrap.Lwjgl3Bootstrap
import dev.deftu.omnicore.OmniCore
import net.minecraftforge.fml.common.Mod

@Mod(modid = "componency")
public class ComponencyMinecraftMod {

    private companion object {

        const val VERSION_REGEX = "(?<major>\\d+).(?<minor>\\d+).?(?<patch>\\d+)?"

        private fun match(version: String): Int {
            val match = VERSION_REGEX.toRegex().find(version) ?: throw IllegalArgumentException("Invalid version format: $version")
            val groups = match.groups

            val major = groups["major"]?.value?.toInt() ?: throw IllegalArgumentException("Invalid version format: $version")
            val minor = groups["minor"]?.value?.toInt() ?: throw IllegalArgumentException("Invalid version format: $version")
            val patch = groups["patch"]?.value?.toInt() ?: 0

            return major * 10000 + minor * 100 + patch
        }

    }

    init {
        Lwjgl3Bootstrap.INSTANCE.initialize(match(OmniCore.minecraftVersion), null)
    }

}
//#endif
