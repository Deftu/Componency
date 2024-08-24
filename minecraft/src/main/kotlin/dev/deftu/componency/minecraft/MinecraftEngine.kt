package dev.deftu.componency.minecraft

//#if MC >= 1.16.5
//$$ import org.lwjgl.opengl.GL
//#else
import org.lwjgl.opengl.GLContext
//#endif

import dev.deftu.componency.engine.Engine
import dev.deftu.componency.minecraft.api.NanoVgApi
import dev.deftu.componency.minecraft.api.StbApi
import dev.deftu.lwjgl.isolatedloader.Lwjgl3Manager

public object MinecraftEngine : Engine() {

    override val renderEngine: MinecraftRenderEngine by lazy {
        val nanoVg = Lwjgl3Manager.getIsolated(NanoVgApi::class.java, "dev.deftu.componency.minecraft.impl.NanoVgImpl", isOpenGl3)
        val stb = Lwjgl3Manager.getIsolated(StbApi::class.java, "dev.deftu.componency.minecraft.impl.StbImpl")
        MinecraftRenderEngine(nanoVg, stb)
    }

    override val inputEngine: MinecraftInputEngine = MinecraftInputEngine

    private val isOpenGl3: Boolean
        get() {
            //#if MC >= 1.16.5
            //$$ return GL.getCapabilities().OpenGL30
            //#else
            return GLContext.getCapabilities().OpenGL30
            //#endif
        }

    init {
        Lwjgl3Manager.initialize(this::class.java.classLoader, arrayOf("nanovg", "stb"))
        Lwjgl3Manager.getClassLoader().addLoadingException("dev.deftu.componency.minecraft.api.") // Ensure that the API classes aren't isolated.
    }

    @JvmStatic
    public fun preload() {
        // no-op - Only serves as a purpose to call the constructor.
    }

}
