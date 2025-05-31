package dev.deftu.componency.android.platform.rendering

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import dev.deftu.componency.color.Color
import dev.deftu.componency.font.Font
import dev.deftu.componency.platform.rendering.TextRenderer
import java.io.File
import java.nio.ByteBuffer
import android.graphics.fonts.Font as AndroidFont
import android.graphics.fonts.FontFamily
import android.graphics.fonts.FontStyle
import android.graphics.Typeface.CustomFallbackBuilder

public class AndroidTextRenderer(
    private val canvasProvider: () -> Canvas?,
    private val tempDirProvider: () -> File
) : TextRenderer {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val typefaceCache = mutableMapOf<Font, Typeface>()

    override fun text(font: Font, text: String, x: Float, y: Float, color: Color, fontSize: Float) {
        val canvas = canvasProvider() ?: return

        configurePaint(font, fontSize, color)
        canvas.drawText(text, x, y, paint)
    }

    override fun textSize(font: Font, text: String, fontSize: Float): TextRenderer.TextSize {
        configurePaint(font, fontSize, null)

        val width = paint.measureText(text)
        val fontMetrics = paint.fontMetrics
        val height = fontMetrics.descent - fontMetrics.ascent

        return TextRenderer.TextSize(width, height)
    }

    private fun configurePaint(font: Font, fontSize: Float, color: Color?) {
        paint.textSize = fontSize

        if (color != null) {
            paint.color = android.graphics.Color.argb(
                (color.alpha * 255).toInt().coerceIn(0, 255),
                (color.red * 255).toInt().coerceIn(0, 255),
                (color.green * 255).toInt().coerceIn(0, 255),
                (color.blue * 255).toInt().coerceIn(0, 255)
            )
        }

        paint.letterSpacing = font.letterSpacing
        paint.typeface = getTypeface(font)
    }

    private fun getTypeface(font: Font): Typeface {
        return typefaceCache.getOrPut(font) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                try {
                    val byteBuffer = font.data.asDirectBuffer()

                    val androidFont = AndroidFont.Builder(byteBuffer)
                        .setWeight(font.weight.value)
                        .setSlant(if (font.isItalic) FontStyle.FONT_SLANT_ITALIC else FontStyle.FONT_SLANT_UPRIGHT)
                        .build()

                    val fontFamily = FontFamily.Builder(androidFont).build()

                    CustomFallbackBuilder(fontFamily).build()
                } catch (e: Exception) {
                    e.printStackTrace()
                    createTypefaceViaTempFile(font)
                }
            } else {
                createTypefaceViaTempFile(font)
            }
        }
    }

    private fun createTypefaceViaTempFile(font: Font): Typeface {
        val tempFile = File(tempDirProvider(), "${font.name.hashCode()}.ttf")
        if (!tempFile.exists()) {
            tempFile.writeBytes(font.data)
        }
        return Typeface.createFromFile(tempFile)
    }

    private fun ByteArray.asDirectBuffer(): ByteBuffer {
        val buffer = ByteBuffer.allocateDirect(this.size)
        buffer.put(this)
        buffer.flip()
        return buffer
    }

}
