package dev.deftu.componency.lwjgl3.engine

import dev.deftu.componency.lwjgl3.engine.audio.Lwjgl3AudioEngine
import dev.deftu.componency.lwjgl3.engine.font.Lwjgl3FontLoader
import dev.deftu.componency.lwjgl3.engine.image.Lwjgl3ImageLoader
import dev.deftu.componency.lwjgl3.engine.input.Lwjgl3InputHandler
import dev.deftu.componency.lwjgl3.engine.rendering.Lwjgl3Renderer
import dev.deftu.componency.platform.Platform
import dev.deftu.componency.time.Clock
import dev.deftu.componency.time.DefaultClock

class Lwjgl3Platform @JvmOverloads constructor(
    window: Long,
    clock: Clock = DefaultClock,
) : Platform {

    override var viewportWidth: Float = 0f

    override var viewportHeight: Float = 0f

    override var pixelRatio: Float = 1f

    override val clock: Clock = clock

    override val inputHandler = Lwjgl3InputHandler(window)

    override val renderer = Lwjgl3Renderer(this)

    override val fontLoader = Lwjgl3FontLoader()

    override val imageLoader  = Lwjgl3ImageLoader()

    override val audioEngine = Lwjgl3AudioEngine()

}
