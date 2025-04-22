package dev.deftu.componency.lwjgl3.engine

import dev.deftu.componency.platform.Platform
import dev.deftu.componency.time.Clock
import dev.deftu.componency.time.DefaultClock

class Lwjgl3Platform(handle: Long) : Platform {

    override var viewportWidth: Float = 0f

    override var viewportHeight: Float = 0f

    override var pixelRatio: Float = 1f

    override val clock: Clock = DefaultClock

    override val inputHandler = Lwjgl3InputHandler(handle)

    override val renderer = Lwjgl3Renderer(this)

    override val fontLoader = Lwjgl3FontLoader()

    override val imageLoader  = Lwjgl3ImageLoader()

    override val audioEngine = Lwjgl3AudioEngine()

}
