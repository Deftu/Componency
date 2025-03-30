package dev.deftu.componency.lwjgl3.engine

import dev.deftu.componency.color.Color
import dev.deftu.componency.color.GradientColor
import dev.deftu.componency.color.GradientType
import dev.deftu.componency.engine.RenderEngine
import dev.deftu.componency.font.Font
import dev.deftu.componency.image.Image
import dev.deftu.componency.styling.StrokeSide
import dev.deftu.textile.TextHolder
import org.lwjgl.nanovg.*
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Lwjgl3RenderEngine : RenderEngine {

    @Suppress("unused")
    internal class NvgFont(
        val id: Int,
        val buffer: ByteBuffer
    )

    override val viewportWidth: Int
        get() = _width

    override val viewportHeight: Int
        get() = _height

    override val pixelRatio = 1f

    override val animationFps = 244

    private var _width: Int = 0
    private var _height: Int = 0

    private var nvgHandle = MemoryUtil.NULL
    private var nsvgHandle = MemoryUtil.NULL
    private var isDrawing = false

    private lateinit var paint: NVGPaint
    private lateinit var color1: NVGColor
    private lateinit var color2: NVGColor

    private val fonts = mutableMapOf<Font, NvgFont>()

    override fun initialize() {
        if (this.nvgHandle == MemoryUtil.NULL) {
            val nvgHandle = NanoVGGL3.nvgCreate(NanoVGGL3.NVG_ANTIALIAS)
            if (nvgHandle == MemoryUtil.NULL) {
                throw IllegalStateException("Failed to create NanoVG context")
            }

            this.nvgHandle = nvgHandle
        }

        if (this.nsvgHandle == MemoryUtil.NULL) {
            val nsvgHandle = NanoSVG.nsvgCreateRasterizer()
            if (nsvgHandle == MemoryUtil.NULL) {
                throw IllegalStateException("Failed to create NanoSVG rasterizer")
            }

            this.nsvgHandle = nsvgHandle
        }

        if (!::paint.isInitialized) {
            this.paint = NVGPaint.create()
        }

        if (!::color1.isInitialized) {
            this.color1 = NVGColor.create()
        }

        if (!::color2.isInitialized) {
            this.color2 = NVGColor.create()
        }
    }

    override fun destroy() {
        NanoVGGL3.nvgDelete(nvgHandle)
        NanoSVG.nsvgDeleteRasterizer(nsvgHandle)
    }

    override fun startFrame() {
        if (isDrawing) {
            throw IllegalStateException("Already drawing")
        }

        initialize()

        if (nvgHandle == MemoryUtil.NULL) {
            return // Wait for initialization
        }

        NanoVG.nvgBeginFrame(nvgHandle, viewportWidth.toFloat(), viewportHeight.toFloat(), pixelRatio)
        isDrawing = true
    }

    override fun endFrame() {
        if (!isDrawing) {
            throw IllegalStateException("Not drawing")
        }

        NanoVG.nvgEndFrame(nvgHandle)
        isDrawing = false
    }

    override fun stroke(x1: Float, y1: Float, x2: Float, y2: Float, color: Color, strokeWidth: Float, strokeSides: Set<StrokeSide>, topLeftRadius: Float, topRightRadius: Float, bottomRightRadius: Float, bottomLeftRadius: Float) {
        if (color.isTransparent) {
            return
        }

        val width = x2 - x1
        val height = y2 - y1

        NanoVG.nvgBeginPath(nvgHandle)
        NanoVG.nvgRoundedRectVarying(nvgHandle, x1, y1, width, height, topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius)
        NanoVG.nvgStrokeWidth(nvgHandle, strokeWidth)
        NanoVG.nvgLineJoin(nvgHandle, NanoVG.NVG_ROUND)
        NanoVG.nvgLineCap(nvgHandle, NanoVG.NVG_ROUND)
        populateStrokeColor(color, x1, y1, width, height)
        NanoVG.nvgStroke(nvgHandle)
    }

    override fun fill(x1: Float, y1: Float, x2: Float, y2: Float, color: Color, topLeftRadius: Float, topRightRadius: Float, bottomRightRadius: Float, bottomLeftRadius: Float) {
        if (color.isTransparent) {
            return
        }

        val width = x2 - x1
        val height = y2 - y1

        NanoVG.nvgBeginPath(nvgHandle)
        NanoVG.nvgRoundedRectVarying(nvgHandle, x1, y1, width, height, topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius)
        populateFillOrColor(color, x1, y1, width, height)
        NanoVG.nvgFill(nvgHandle)
    }

    override fun text(font: Font, text: TextHolder<*, *>, x: Float, y: Float, color: Color, fontSize: Float) {
        if (color.isTransparent) {
            return
        }

        val (width, height) = textSize(font, text, fontSize)

        NanoVG.nvgBeginPath(nvgHandle)
        NanoVG.nvgFontSize(nvgHandle, fontSize)
        NanoVG.nvgFontFaceId(nvgHandle, getOrPopulateFont(font).id)
        NanoVG.nvgTextAlign(nvgHandle, NanoVG.NVG_ALIGN_LEFT or NanoVG.NVG_ALIGN_TOP)
        populateFillOrColor(color, x, y, width, height)
        NanoVG.nvgText(nvgHandle, x, y, text.asUnformattedString())
    }

    override fun textSize(font: Font, text: TextHolder<*, *>, fontSize: Float): Pair<Float, Float> {
        val string = text.asUnformattedString().let { if (it.endsWith(" ")) "$it " else it }

        val output = FloatArray(4)
        NanoVG.nvgFontFaceId(nvgHandle, getOrPopulateFont(font).id)
        NanoVG.nvgTextAlign(nvgHandle, NanoVG.NVG_ALIGN_LEFT or NanoVG.NVG_ALIGN_TOP)
        NanoVG.nvgFontSize(nvgHandle, fontSize)
        NanoVG.nvgTextBounds(nvgHandle, 0f, 0f, string, output)

        val width = output[2] - output[0]
        val height = output[3] - output[1]
        return width to height
    }

    override fun image(image: Image, x1: Float, y1: Float, x2: Float, y2: Float, color: Color, topLeftRadius: Float, topRightRadius: Float, bottomRightRadius: Float, bottomLeftRadius: Float) {
        TODO()
    }

    override fun pushScissor(x1: Float, y1: Float, x2: Float, y2: Float) {
        val width = x2 - x1
        val height = y2 - y1
        NanoVG.nvgScissor(nvgHandle, x1, y1, width, height)
    }

    override fun popScissor() {
        NanoVG.nvgResetScissor(nvgHandle)
    }

    private fun getOrPopulateFont(font: Font): NvgFont {
        return fonts.getOrPut(font) {
            val bytes = font.data
            val buffer = ByteBuffer.allocateDirect(bytes.size).order(ByteOrder.nativeOrder()).put(bytes).flip() as ByteBuffer
            val id = NanoVG.nvgCreateFontMem(
                nvgHandle,
                font.name,
                buffer,
                false
            )

            NvgFont(id, buffer)
        }
    }

    private fun populateNvgColor(color: Color, nvgColor: NVGColor) {
        NanoVG.nvgRGBA(color.red.toByte(), color.green.toByte(), color.blue.toByte(), color.alpha.toByte(), nvgColor)
    }

    private fun populateStaticColor(color: Color) {
        populateNvgColor(color, color1)

        if (color is GradientColor) {
            populateNvgColor(color.secondColor, color2)
        }
    }

    private fun populateColor(
        color: Color,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
    ): Boolean {
        populateStaticColor(color)
        if (color !is GradientColor) {
            return false
        }

        when (val type = color.type) {
            is GradientType.Linear -> {
                val direction = type.direction
                when (direction) {
                    GradientType.LinearDirection.HORIZONTAL -> {
                        NanoVG.nvgLinearGradient(
                            nvgHandle,
                            x,
                            y,
                            x + width,
                            y,
                            color1,
                            color2,
                            paint
                        )
                    }

                    GradientType.LinearDirection.VERTICAL -> {
                        NanoVG.nvgLinearGradient(
                            nvgHandle,
                            x,
                            y,
                            x,
                            y + height,
                            color1,
                            color2,
                            paint
                        )
                    }

                    GradientType.LinearDirection.DIAGONAL -> {
                        NanoVG.nvgLinearGradient(
                            nvgHandle,
                            x,
                            y,
                            x + width,
                            y + height,
                            color1,
                            color2,
                            paint
                        )
                    }
                }
            }

            is GradientType.Radial -> {
                val (centerX, centerY) = type.center
                NanoVG.nvgRadialGradient(
                    nvgHandle,
                    if (centerX == -1f) x + width / 2 else centerX,
                    if (centerY == -1f) y + height / 2 else centerY,
                    type.innerRadius,
                    type.outerRadius,
                    color1,
                    color2,
                    paint
                )
            }
        }

        return true
    }

    private fun populateFillOrColor(color: Color, x: Float, y: Float, width: Float, height: Float) {
        if (populateColor(color, x, y, width, height)) {
            NanoVG.nvgFillPaint(nvgHandle, paint)
        } else {
            NanoVG.nvgFillColor(nvgHandle, color1)
        }
    }

    private fun populateStrokeColor(color: Color, x: Float, y: Float, width: Float, height: Float) {
        if (populateColor(color, x, y, width, height)) {
            NanoVG.nvgStrokePaint(nvgHandle, paint)
        } else {
            NanoVG.nvgStrokeColor(nvgHandle, color1)
        }
    }

    fun resize(width: Int, height: Int) {
        _width = width
        _height = height
    }

}
