package dev.deftu.componency.lwjgl3.engine

import dev.deftu.componency.color.Color
import dev.deftu.componency.engine.RenderEngine
import dev.deftu.componency.font.Font
import dev.deftu.componency.image.Image
import dev.deftu.componency.styling.StrokeSide
import dev.deftu.textile.TextHolder

class Lwjgl3RenderEngine : RenderEngine {

    override val viewportWidth: Int
        get() = _width

    override val viewportHeight: Int
        get() = _height

    override val pixelRatio: Float = 1f

    override val animationFps: Int = 244

    private var _width: Int = 0
    private var _height: Int = 0

    override fun initialize() {
        // Initialize your render engine...
    }

    override fun destroy() {
        // Destroy your render engine...
    }

    override fun startFrame() {
        // Begin a new frame... (clear, etc)
    }

    override fun endFrame() {
        // End the frame... (swap buffers, etc)
    }

    override fun stroke(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        color: Color,
        strokeWidth: Float,
        strokeSides: Set<StrokeSide>,
        topLeftRadius: Float,
        topRightRadius: Float,
        bottomRightRadius: Float,
        bottomLeftRadius: Float
    ) {
        // Render your stroke...
    }

    override fun fill(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        color: Color,
        topLeftRadius: Float,
        topRightRadius: Float,
        bottomRightRadius: Float,
        bottomLeftRadius: Float
    ) {
        // Render your rectangle...
    }

    override fun text(font: Font, text: TextHolder, x: Float, y: Float, color: Color, fontSize: Float) {
        // Render your text...
    }

    override fun textSize(font: Font, text: TextHolder, fontSize: Float): Pair<Float, Float> {
        // Calculate the size of the text...
        return 0f to 0f
    }

    override fun image(
        image: Image,
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        color: Color,
        topLeftRadius: Float,
        topRightRadius: Float,
        bottomRightRadius: Float,
        bottomLeftRadius: Float
    ) {
        // Render your image...
    }

    override fun pushScissor(x1: Float, y1: Float, x2: Float, y2: Float) {
        // Push scissor...
    }

    override fun popScissor() {
        // Pop scissor...
    }

    fun resize(width: Int, height: Int) {
        _width = width
        _height = height
    }

}
