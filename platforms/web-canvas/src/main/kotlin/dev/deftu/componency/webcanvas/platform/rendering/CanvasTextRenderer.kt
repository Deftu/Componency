package dev.deftu.componency.webcanvas.platform.rendering

import dev.deftu.componency.font.Font
import dev.deftu.componency.platform.rendering.TextRenderer
import dev.deftu.kolor.Color
import dev.deftu.kolor.toCss
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.CanvasTextBaseline
import org.w3c.dom.TOP

public class CanvasTextRenderer(private val context: CanvasRenderingContext2D) : TextRenderer {

    override fun text(
        font: Font,
        text: String,
        x: Float,
        y: Float,
        color: Color,
        fontSize: Float
    ) {
        context.save()
        context.fillStyle = color.toCss() // implement toCss() to get CSS string like "#RRGGBB" or rgba()
        context.font = "${fontSize}px ${font.name}" // assuming font.name holds CSS font-family
        context.textBaseline = CanvasTextBaseline.TOP // or "alphabetic" if you prefer
        context.fillText(text, x.toDouble(), y.toDouble())
        context.restore()
    }

    override fun textSize(
        font: Font,
        text: String,
        fontSize: Float
    ): TextRenderer.TextSize {
        context.save()
        context.font = "${fontSize}px ${font.name}"
        val metrics = context.measureText(text)
        context.restore()

        // Metrics you can get:
        val width = metrics.width.toFloat()
        // No direct height measurement; estimate via fontSize
        val height = fontSize

        return TextRenderer.TextSize(width, height)
    }

}
