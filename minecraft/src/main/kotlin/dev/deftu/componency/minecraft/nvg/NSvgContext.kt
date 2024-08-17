package dev.deftu.componency.minecraft.nvg

import org.lwjgl.nanovg.NanoSVG
import org.lwjgl.system.MemoryUtil

public class NSvgContext : AutoCloseable {

    public var handle: Long = 0L
        private set

    public val isCreated: Boolean
        get() = this.handle != MemoryUtil.NULL

    init {
        val handle = NanoSVG.nsvgCreateRasterizer()

        if (handle == MemoryUtil.NULL) {
            throw IllegalStateException("Failed to create NanoVG context")
        }

        this.handle = handle
    }

    override fun close() {
        if (this.handle == MemoryUtil.NULL) {
            return
        }

        NanoSVG.nsvgDeleteRasterizer(this.handle)
        this.handle = MemoryUtil.NULL
    }

}
