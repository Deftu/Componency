package dev.deftu.componency.minecraft.tweaker

import net.minecraft.launchwrapper.ITweaker
import net.minecraft.launchwrapper.Launch
import net.minecraft.launchwrapper.LaunchClassLoader
import net.minecraftforge.common.ForgeVersion
import net.minecraftforge.fml.relauncher.CoreModManager
import java.io.File
import java.util.Properties

/**
 * This tweaker exists solely as a homemade solution to implement Jar-in-Jar dependency management in legacy Forge.
 *
 * Usually, once Forge notices that a mod is a coremod or has the same tweaker as another mod, it will simply load the tweaker and move on.
 * Our tweaker here is a bit different, as it will forcibly load the mod containing as per usual and inject the necessary files into the classpath.
 *
 * If you need to use another tweaker in conjunction with this one, you will either need to merge the two by extending this one or you will need to load both via [delegation](https://moddev.nea.moe/tweakers/#delegating-tweakers).
 */
public open class ComponencyTweaker : ITweaker {

    public companion object {

        private const val BASE_VERSION_KEY = "componency-legacy-lwjgl3-base-version"
        private const val MINECRAFT_VERSION_KEY = "componency-legacy-lwjgl3-minecraft-version"

        private const val BASE_JAR_NAME = "componency-%s.jar"
        private const val MINECRAFT_JAR_NAME = "componency-minecraft-%s-%s.jar"

        private var baseJarVersion: Long? = null
        private var minecraftJarVersion: Long? = null

    }

    private val selfJar: File? by lazy {
        val url = this::class.java.protectionDomain.codeSource.location
        if (url == null || url.protocol != "file") {
            return@lazy null
        }

        File(url.toURI()).absoluteFile
    }

    private val platform: String by lazy {
        val mcVersion = ForgeVersion.mcVersion
        "$mcVersion-forge"
    }

    private val formattedBaseJarName by lazy {
        BASE_JAR_NAME.format(baseJarVersion)
    }

    private val formattedMinecraftJarName by lazy {
        MINECRAFT_JAR_NAME.format(minecraftJarVersion, platform)
    }

    private val globalBaseVersion: Long
        get() {
            val version = Launch.blackboard[BASE_VERSION_KEY]
            if (version == null) {
                return Long.MIN_VALUE
            }

            if (version !is Long) {
                return Long.MAX_VALUE
            }

            return version
        }

    private val globalMinecraftVersion: Long
        get() {
            val version = Launch.blackboard[MINECRAFT_VERSION_KEY]
            if (version == null) {
                return Long.MIN_VALUE
            }

            if (version !is Long) {
                return Long.MAX_VALUE
            }

            return version
        }

    init {
        val versionProperties = try {
            val properties = this::class.java.getResourceAsStream("/jars/versions.properties")
            properties?.use {
                val versionProperties = Properties()
                versionProperties.load(it)
                versionProperties
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            null
        }

        (versionProperties?.getProperty("base", "0.0.0") ?: "0.0.0").let { version ->
            baseJarVersion = getHighestVersion(version, baseJarVersion)
        }

        (versionProperties?.getProperty("minecraft", "0.0.0") ?: "0.0.0").let { version ->
            minecraftJarVersion = getHighestVersion(version, minecraftJarVersion)
        }
    }

    override fun getLaunchTarget(): String = "net.minecraft.client.main.Main"

    override fun acceptOptions(
        args: List<String>,
        gameDir: File?,
        assetsDir: File?,
        profile: String?
    ) {
        attemptUpdateGlobalVersions()
        forciblyLoadAsMod()
    }

    override fun getLaunchArguments(): Array<out String> {
        unpackFiles()
        injectFiles()

        return arrayOf()
    }

    @Suppress("UNCHECKED_CAST")
    override fun injectIntoClassLoader(classLoader: LaunchClassLoader) {
    }

    @Suppress("UNCHECKED_CAST")
    private fun forciblyLoadAsMod() {
        val selfJar = selfJar
        if (selfJar != null) {
            CoreModManager.getReparseableCoremods().add(selfJar.path)
            CoreModManager.getIgnoredMods().remove(selfJar.path)
        }
    }

    private fun attemptUpdateGlobalVersions() {
        if ((baseJarVersion?.compareTo(globalBaseVersion) ?: 0) > 0) {
            Launch.blackboard[BASE_VERSION_KEY] = baseJarVersion
        }

        if ((minecraftJarVersion?.compareTo(globalMinecraftVersion) ?: 0) > 0) {
            Launch.blackboard[MINECRAFT_VERSION_KEY] = minecraftJarVersion
        }
    }

    private fun getHighestVersion(checkingVersion: String, originalVersion: Long?): Long {
        val components = checkingVersion.split(".")
        require(components.size == 3) { "Invalid version: $checkingVersion" }

        var versionComparable = 0L
        for (component in components) {
            versionComparable *= 10_000
            versionComparable += component.toLong()
        }

        if (originalVersion != null) {
            if (versionComparable > originalVersion) {
                return versionComparable
            }
        } else {
            return versionComparable
        }

        return originalVersion
    }

    private fun unpackFiles() {
        val extractedFileLocations = mutableMapOf(
            formattedBaseJarName to File("componency/$formattedBaseJarName"),
            formattedMinecraftJarName to File("componency/$formattedMinecraftJarName")
        ).apply { forEach { (_, file) -> file.parentFile.mkdirs() } }

        this::class.java.getResourceAsStream("/jars/$formattedBaseJarName")?.use { input ->
            extractedFileLocations[formattedBaseJarName]?.outputStream()?.use { output ->
                input.copyTo(output)
            }
        }

        this::class.java.getResourceAsStream("/jars/$formattedMinecraftJarName")?.use { input ->
            extractedFileLocations[formattedMinecraftJarName]?.outputStream()?.use { output ->
                input.copyTo(output)
            }
        }
    }

    private fun injectFiles() {
        val baseJar = File("componency/$formattedBaseJarName")
        val minecraftJar = File("componency/$formattedMinecraftJarName")

        if (baseJar.exists()) {
            Launch.classLoader.addURL(baseJar.toURI().toURL())
        }

        if (minecraftJar.exists()) {
            Launch.classLoader.addURL(minecraftJar.toURI().toURL())
        }
    }

}
