package dev.deftu.componency.engine

import dev.deftu.componency.color.Color
import dev.deftu.componency.font.Font
import dev.deftu.componency.image.Image
import dev.deftu.componency.styling.StrokeSide
import dev.deftu.textile.TextHolder
import kotlin.math.abs
import kotlin.math.round
import kotlin.math.sign

public interface RenderEngine {

    public val viewportWidth: Int

    public val viewportHeight: Int

    public val pixelRatio: Float

    public val animationFps: Int

    public fun roundToPixel(value: Float): Float {
        return round(value * pixelRatio).let { pixel ->
            if (pixel == 0f && abs(value) > 0.001f) {
                sign(value)
            } else {
                pixel
            }
        } / pixelRatio
    }

    public fun initialize()

    public fun destroy()

    public fun startFrame()

    public fun endFrame()

    public fun stroke(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        color: Color,
        strokeWidth: Float,
        strokeSides: Set<StrokeSide>,
        topLeftRadius: Float = 0f,
        topRightRadius: Float = 0f,
        bottomRightRadius: Float = 0f,
        bottomLeftRadius: Float = 0f
    )

    public fun fill(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        color: Color,
        topLeftRadius: Float = 0f,
        topRightRadius: Float = 0f,
        bottomRightRadius: Float = 0f,
        bottomLeftRadius: Float = 0f
    )

    public fun text(font: Font, text: TextHolder<*, *>, x: Float, y: Float, color: Color, fontSize: Float)

    public fun textSize(font: Font, text: TextHolder<*, *>, fontSize: Float): Pair<Float, Float>

    public fun image(
        image: Image,
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        color: Color,
        topLeftRadius: Float = 0f,
        topRightRadius: Float = 0f,
        bottomRightRadius: Float = 0f,
        bottomLeftRadius: Float = 0f
    )

    public fun pushScissor(x1: Float, y1: Float, x2: Float, y2: Float)

    public fun popScissor()

}
