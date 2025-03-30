package dev.deftu.componency.lwjgl3.engine

import dev.deftu.componency.engine.Engine
import dev.deftu.componency.engine.InputEngine
import dev.deftu.componency.engine.RenderEngine

class Lwjgl3Engine(private val handle: Long) : Engine() {

    override val renderEngine = Lwjgl3RenderEngine()

    override val inputEngine = Lwjgl3InputEngine(handle)

    fun resize(width: Int, height: Int) {
        renderEngine.resize(width, height)
    }

    fun updateMousePosition(x: Float, y: Float) {
        inputEngine.updateMousePosition(x, y)
    }

}
