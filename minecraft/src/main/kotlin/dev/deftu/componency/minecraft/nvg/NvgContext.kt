package dev.deftu.componency.minecraft.nvg

//#if MC >= 1.16.5
//$$ import org.lwjgl.opengl.GL
//#else
import org.lwjgl.opengl.GLContext
//#endif

import org.lwjgl.nanovg.NanoVGGL2
import org.lwjgl.nanovg.NanoVGGL3
import org.lwjgl.system.MemoryUtil

public class NvgContext : AutoCloseable {

    public val isOpenGl3: Boolean
        get() {
            //#if MC >= 1.16.5
            //$$ return GL.getCapabilities().OpenGL30
            //#else
            return GLContext.getCapabilities().OpenGL30
            //#endif
        }

    public var handle: Long = 0L
        private set

    public val isCreated: Boolean
        get() = this.handle != MemoryUtil.NULL

    init {
        val handle = when (isOpenGl3) {
            true -> NanoVGGL3.nvgCreate(NanoVGGL3.NVG_ANTIALIAS)
            false -> NanoVGGL2.nvgCreate(NanoVGGL2.NVG_ANTIALIAS)
        }

        if (handle == MemoryUtil.NULL) {
            throw IllegalStateException("Failed to create NanoVG context")
        }

        this.handle = handle
    }

    override fun close() {
        if (this.handle == MemoryUtil.NULL) {
            return
        }

        when (isOpenGl3) {
            true -> NanoVGGL3.nvgDelete(this.handle)
            false -> NanoVGGL2.nvgDelete(this.handle)
        }

        this.handle = MemoryUtil.NULL
    }

}
