package dev.deftu.componency.lwjgl3.platform.rendering

import dev.deftu.componency.font.Font
import dev.deftu.componency.platform.rendering.TextRenderer
import dev.deftu.kolor.Color
import org.lwjgl.nanovg.NanoVG
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Lwjgl3TextRenderer(
    private val renderer: Lwjgl3Renderer
) : TextRenderer {

    @Suppress("unused")
    internal class NvgFont(
        val id: Int,
        val buffer: ByteBuffer
    )

    private val fonts = mutableMapOf<Font, NvgFont>()

    var nvg = MemoryUtil.NULL

    override fun text(font: Font, text: String, x: Float, y: Float, color: Color, fontSize: Float) {
        if (color.isTransparent) {
            return
        }

        val fontId = getOrPopulateFont(font).id
        NanoVG.nvgFontFaceId(nvg, fontId)
        NanoVG.nvgTextAlign(nvg, NanoVG.NVG_ALIGN_LEFT or NanoVG.NVG_ALIGN_TOP)
        NanoVG.nvgFontSize(nvg, fontSize)

        // Calculate baseline Y for vertical center alignment
        val ascender = FloatArray(1)
        val descender = FloatArray(1)
        val lineHeight = FloatArray(1)
        NanoVG.nvgTextMetrics(nvg, ascender, descender, lineHeight)

        val baselineY = y + (lineHeight[0] - ascender[0]) / 2f

        // Draw background fill if needed
        val (width, height) = textSize(font, text, fontSize)
        NanoVG.nvgBeginPath(nvg)
        renderer.populateFillOrColor(color, x, y - lineHeight[0] / 2f, width, lineHeight[0])

        NanoVG.nvgText(nvg, x, baselineY, text)
    }

    override fun textSize(font: Font, text: String, fontSize: Float): TextRenderer.TextSize {
        val string = text.let { if (it.endsWith(" ")) "$it " else it }
        NanoVG.nvgFontFaceId(nvg, getOrPopulateFont(font).id)
        NanoVG.nvgFontSize(nvg, fontSize)

        val bounds = FloatArray(4)
        NanoVG.nvgTextAlign(nvg, NanoVG.NVG_ALIGN_LEFT or NanoVG.NVG_ALIGN_TOP)
        NanoVG.nvgTextBounds(nvg, 0f, 0f, string, bounds)

        val width = bounds[2] - bounds[0]

        val ascender = FloatArray(1)
        val descender = FloatArray(1)
        val lineHeight = FloatArray(1)
        NanoVG.nvgTextMetrics(nvg, ascender, descender, lineHeight)

        val height = lineHeight[0]

        return TextRenderer.TextSize(width, height)
    }


    private fun getOrPopulateFont(font: Font): NvgFont {
        return fonts.getOrPut(font) {
            val bytes = font.data
            val buffer = ByteBuffer.allocateDirect(bytes.size).order(ByteOrder.nativeOrder()).put(bytes).flip() as ByteBuffer
            val id = NanoVG.nvgCreateFontMem(
                nvg,
                font.name,
                buffer,
                false
            )

            NvgFont(id, buffer)
        }
    }

}
