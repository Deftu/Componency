package dev.deftu.componency.lwjgl3.engine

import dev.deftu.componency.engine.RenderEngine
import dev.deftu.componency.font.Font
import dev.deftu.textile.TextHolder
import java.awt.Color

class Lwjgl3RenderEngine : RenderEngine {

    override val viewportWidth: Int
        get() = _width

    override val viewportHeight: Int
        get() = _height

    override val animationFps: Int = 244

    private var _width: Int = 0
    private var _height: Int = 0

    override fun startFrame() {
        // Begin a new frame... (clear, etc)
    }

    override fun endFrame() {
        // End the frame... (swap buffers, etc)
    }

    override fun fill(x1: Float, y1: Float, x2: Float, y2: Float, color: Color, radius: Float) {
        // Render your rectangle...
    }

    override fun text(x: Float, y: Float, text: TextHolder, color: Color, font: Font) {
        // Render your text...
    }

    fun resize(width: Int, height: Int) {
        _width = width
        _height = height
    }

}
