package dev.deftu.componency.lwjgl3.engine

import dev.deftu.componency.color.Color
import dev.deftu.componency.font.Font
import dev.deftu.componency.platform.rendering.TextRenderer
import dev.deftu.textile.TextHolder
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

        val (width, height) = textSize(font, text, fontSize)

        NanoVG.nvgBeginPath(nvg)
        NanoVG.nvgFontSize(nvg, fontSize)
        NanoVG.nvgFontFaceId(nvg, getOrPopulateFont(font).id)
        NanoVG.nvgTextAlign(nvg, NanoVG.NVG_ALIGN_LEFT or NanoVG.NVG_ALIGN_TOP)
        renderer.populateFillOrColor(color, x, y, width, height)
        NanoVG.nvgText(nvg, x, y, text)
    }

    override fun textSize(font: Font, text: String, fontSize: Float): TextRenderer.TextSize {
        val string = text.let { if (it.endsWith(" ")) "$it " else it }

        val output = FloatArray(4)
        NanoVG.nvgFontFaceId(nvg, getOrPopulateFont(font).id)
        NanoVG.nvgTextAlign(nvg, NanoVG.NVG_ALIGN_LEFT or NanoVG.NVG_ALIGN_TOP)
        NanoVG.nvgFontSize(nvg, fontSize)
        NanoVG.nvgTextBounds(nvg, 0f, 0f, string, output)

        val width = output[2] - output[0]
        val height = output[3] - output[1]
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
