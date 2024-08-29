package dev.deftu.componency.lwjgl3.engine

import dev.deftu.componency.engine.Engine
import dev.deftu.componency.engine.InputEngine
import dev.deftu.componency.engine.RenderEngine

class Lwjgl3Engine : Engine() {

    override val renderEngine: RenderEngine
        get() = _renderEngine

    override val inputEngine: InputEngine
        get() = _inputEngine

    private val _renderEngine by lazy {
        Lwjgl3RenderEngine()
    }

    private val _inputEngine by lazy {
        Lwjgl3InputEngine()
    }

    fun resize(width: Int, height: Int) {
        _renderEngine.resize(width, height)
    }

    fun updateMousePosition(x: Float, y: Float) {
        _inputEngine.updateMousePosition(x, y)
    }

}
