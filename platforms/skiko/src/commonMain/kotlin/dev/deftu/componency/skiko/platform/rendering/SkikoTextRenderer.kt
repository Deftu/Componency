package dev.deftu.componency.skiko.platform.rendering

import dev.deftu.componency.font.Font
import dev.deftu.componency.platform.rendering.TextRenderer
import dev.deftu.kolor.Color
import org.jetbrains.skia.Data
import org.jetbrains.skia.Typeface
import org.jetbrains.skia.Font as SkikoFont

public class SkikoTextRenderer(private val renderer: SkikoRenderer) : TextRenderer {

    private val fonts = mutableMapOf<Font, SkikoFont>()

    override fun text(
        font: Font,
        text: String,
        x: Float,
        y: Float,
        color: Color,
        fontSize: Float
    ) {
        val font = font(font, fontSize)

        with(renderer) {
            paint.reset()
            paint.setARGB(color.alpha, color.red, color.green, color.blue)
            paint.setAlphaf(color.alpha.toFloat() / 255f)
            paint.isAntiAlias = true

            val renderedY = y - font.metrics.ascent
            canvas.drawString(
                text,
                x,
                renderedY,
                font,
                paint
            )
        }
    }

    override fun textSize(
        font: Font,
        text: String,
        fontSize: Float
    ): TextRenderer.TextSize {
        val skikoFont = font(font, fontSize)
        val bounds = skikoFont.measureText(text, renderer.paint.reset().apply { isAntiAlias = true })
        return TextRenderer.TextSize(bounds.width, bounds.height)
    }

    private fun font(font: Font, fontSize: Float): SkikoFont {
        val cached = this.fonts[font]
        if (cached != null && cached.size == fontSize) {
            return cached
        }

        val typeface = try {
            Typeface.makeFromData(Data.makeFromBytes(font.data))
        } catch (e: Exception) {
            Typeface.makeDefault()
        }

        val skikoFont = SkikoFont(typeface, fontSize)
        this.fonts[font] = skikoFont
        return skikoFont
    }

}
