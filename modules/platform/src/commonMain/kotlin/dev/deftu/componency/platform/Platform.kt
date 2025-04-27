package dev.deftu.componency.platform

import dev.deftu.componency.platform.audio.AudioEngine
import dev.deftu.componency.platform.font.FontLoader
import dev.deftu.componency.platform.image.ImageLoader
import dev.deftu.componency.platform.input.InputHandler
import dev.deftu.componency.platform.rendering.Renderer
import dev.deftu.componency.time.Clock

public interface Platform {

    public val viewportWidth: Float

    public val viewportHeight: Float

    public val pixelRatio: Float

    public val targetFramerate: Int
        get() = 500

    public val clock: Clock

    public val inputHandler: InputHandler

    public val renderer: Renderer

    // Optional

    public val fontLoader: FontLoader

    public val imageLoader: ImageLoader

    public val audioEngine: AudioEngine

}
