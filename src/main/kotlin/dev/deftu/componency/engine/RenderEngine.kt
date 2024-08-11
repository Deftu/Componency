package dev.deftu.componency.engine

import dev.deftu.componency.font.Font
import dev.deftu.textile.TextHolder
import java.awt.Color

public interface RenderEngine {

    public val viewportWidth: Int

    public val viewportHeight: Int

    public val animationFps: Int

    public fun startFrame()

    public fun endFrame()

    public fun fill(x1: Float, y1: Float, x2: Float, y2: Float, color: Color, radius: Float)

    public fun text(x: Float, y: Float, text: TextHolder, color: Color, font: Font)

}
