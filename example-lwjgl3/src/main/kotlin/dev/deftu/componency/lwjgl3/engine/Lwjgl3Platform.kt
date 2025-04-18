package dev.deftu.componency.lwjgl3.engine

import dev.deftu.componency.platform.Platform

class Lwjgl3Platform(private val handle: Long) : Platform {

    override var viewportWidth: Float = 0f

    override var viewportHeight: Float = 0f

    override var pixelRatio: Float = 1f

    override val inputHandler = Lwjgl3InputHandler(handle)

    override val renderer = Lwjgl3Renderer(this)

}
