package dev.deftu.componency.lwjgl3.platform

import dev.deftu.componency.lwjgl3.platform.audio.Lwjgl3AudioEngine
import dev.deftu.componency.lwjgl3.platform.font.Lwjgl3FontLoader
import dev.deftu.componency.lwjgl3.platform.image.Lwjgl3ImageLoader
import dev.deftu.componency.lwjgl3.platform.input.Lwjgl3InputHandler
import dev.deftu.componency.lwjgl3.platform.rendering.Lwjgl3Renderer
import dev.deftu.componency.platform.Platform
import dev.deftu.componency.time.Clock
import dev.deftu.componency.time.DefaultClock

class Lwjgl3Platform @JvmOverloads constructor(
    window: Long,
    override val clock: Clock = DefaultClock,
) : Platform {

    override var viewportWidth: Float = 0f

    override var viewportHeight: Float = 0f

    override var pixelRatio: Float = 1f

    override val inputHandler = Lwjgl3InputHandler(window)

    override val renderer = Lwjgl3Renderer(this)

    override val fontLoader = Lwjgl3FontLoader()

    override val imageLoader  = Lwjgl3ImageLoader()

    override val audioEngine = Lwjgl3AudioEngine()

}
