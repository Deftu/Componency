package dev.deftu.componency.lwjgl3.engine

import dev.deftu.componency.engine.InputEngine

class Lwjgl3InputEngine : InputEngine {

    override val mouseX: Float
        get() = _mouseX

    override val mouseY: Float
        get() = _mouseY

    private var _mouseX: Float = 0f
    private var _mouseY: Float = 0f

    fun updateMousePosition(x: Float, y: Float) {
        _mouseX = x
        _mouseY = y
    }

}
