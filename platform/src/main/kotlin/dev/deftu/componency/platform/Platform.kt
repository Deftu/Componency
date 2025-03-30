package dev.deftu.componency.platform

import dev.deftu.componency.platform.input.InputHandler
import dev.deftu.componency.platform.rendering.Renderer

public interface Platform {

    public val viewportWidth: Float

    public val viewportHeight: Float

    public val inputHandler: InputHandler

    public val renderer: Renderer

}
