package dev.deftu.componency.platform.rendering

import dev.deftu.componency.color.Color

public interface Renderer {

    public val isDrawing: Boolean

    public val textRenderer: TextRenderer

    public val imageRenderer: ImageRenderer

    public fun startFrame()

    public fun endFrame()

    public fun fill(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        color: Color,
        topLeftRadius: Float,
        topRightRadius: Float,
        bottomRightRadius: Float,
        bottomLeftRadius: Float
    )

    public fun fill(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        color: Color
    ) {
        fill(x1, y1, x2, y2, color, 0f, 0f, 0f, 0f)
    }

    public fun stroke(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        color: Color,
        strokeWidth: Float,
        topLeftRadius: Float,
        topRightRadius: Float,
        bottomRightRadius: Float,
        bottomLeftRadius: Float
    )

    public fun stroke(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        color: Color,
        strokeWidth: Float
    ) {
        stroke(x1, y1, x2, y2, color, strokeWidth, 0f, 0f, 0f, 0f)
    }

    public fun pushScissor(x1: Float, y1: Float, x2: Float, y2: Float)

    public fun popScissor()

}
