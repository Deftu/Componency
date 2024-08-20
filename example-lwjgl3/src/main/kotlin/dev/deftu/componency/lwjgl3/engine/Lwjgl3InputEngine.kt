package dev.deftu.componency.lwjgl3.engine

import dev.deftu.componency.engine.InputEngine
import dev.deftu.componency.input.Key
import dev.deftu.componency.input.MouseButton

class Lwjgl3InputEngine : InputEngine {

    override val mouseX: Float
        get() = _mouseX

    override val mouseY: Float
        get() = _mouseY

    private var _mouseX: Float = 0f
    private var _mouseY: Float = 0f

    override fun isMouseButtonDown(button: MouseButton): Boolean {
        // Map the button to the GLFW button...
        return false
    }

    override fun isKeyDown(key: Key): Boolean {
        // Map the key to the GLFW key...
        return false
    }

    fun updateMousePosition(x: Float, y: Float) {
        _mouseX = x
        _mouseY = y
    }

}
