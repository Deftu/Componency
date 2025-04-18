package dev.deftu.componency.platform

import dev.deftu.componency.platform.input.InputHandler
import dev.deftu.componency.platform.rendering.Renderer

public interface Platform {

    public val viewportWidth: Float

    public val viewportHeight: Float

    public val pixelRatio: Float

    public val targetFramerate: Int
        get() = 500

    public val inputHandler: InputHandler

    public val renderer: Renderer

}
