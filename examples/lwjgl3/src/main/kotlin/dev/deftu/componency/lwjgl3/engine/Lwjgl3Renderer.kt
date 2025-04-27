package dev.deftu.componency.lwjgl3.engine

import dev.deftu.componency.color.Color
import dev.deftu.componency.color.GradientColor
import dev.deftu.componency.color.GradientType
import dev.deftu.componency.platform.Platform
import dev.deftu.componency.platform.rendering.Renderer
import dev.deftu.componency.stroke.StrokeSide
import org.lwjgl.nanovg.*
import org.lwjgl.system.MemoryUtil

class Lwjgl3Renderer(
    private val platform: Platform,
) : Renderer {

    override var isDrawing = false

    override val textRenderer = Lwjgl3TextRenderer(this)

    override val imageRenderer = Lwjgl3ImageRenderer(this)

    private var nvg = MemoryUtil.NULL
        set(value) {
            field = value
            if (value != MemoryUtil.NULL) {
                textRenderer.nvg = value
                imageRenderer.nvg = value
            }
        }

    private var nsvg = MemoryUtil.NULL
        set(value) {
            field = value
            if (value != MemoryUtil.NULL) {
                imageRenderer.nsvg = value
            }
        }

    internal lateinit var paint: NVGPaint
    private lateinit var color1: NVGColor
    private lateinit var color2: NVGColor

    fun initialize() {
        if (this.nvg == MemoryUtil.NULL) {
            val nvgHandle = NanoVGGL3.nvgCreate(NanoVGGL3.NVG_ANTIALIAS)
            if (nvgHandle == MemoryUtil.NULL) {
                throw IllegalStateException("Failed to create NanoVG context")
            }

            this.nvg = nvgHandle
        }

        if (this.nsvg == MemoryUtil.NULL) {
            val nsvgHandle = NanoSVG.nsvgCreateRasterizer()
            if (nsvgHandle == MemoryUtil.NULL) {
                throw IllegalStateException("Failed to create NanoSVG rasterizer")
            }

            this.nsvg = nsvgHandle
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

    fun close() {
        NanoVGGL3.nvgDelete(nvg)
        NanoSVG.nsvgDeleteRasterizer(nsvg)
    }

    override fun startFrame() {
        if (isDrawing) {
            throw IllegalStateException("Already drawing")
        }

        initialize()

        if (nvg == MemoryUtil.NULL) {
            return // Wait for initialization
        }

        NanoVG.nvgBeginFrame(nvg, platform.viewportWidth, platform.viewportHeight, platform.pixelRatio)
        isDrawing = true
    }

    override fun endFrame() {
        if (!isDrawing) {
            throw IllegalStateException("Not drawing")
        }

        NanoVG.nvgEndFrame(nvg)
        isDrawing = false
    }

    override fun stroke(x1: Float, y1: Float, x2: Float, y2: Float, color: Color, strokeWidth: Float, strokeSides: Set<StrokeSide>, topLeftRadius: Float, topRightRadius: Float, bottomRightRadius: Float, bottomLeftRadius: Float) {
        if (color.isTransparent) {
            return
        }

        val width = x2 - x1
        val height = y2 - y1

        NanoVG.nvgBeginPath(nvg)
        NanoVG.nvgRoundedRectVarying(nvg, x1, y1, width, height, topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius)
        NanoVG.nvgStrokeWidth(nvg, strokeWidth)
        NanoVG.nvgLineJoin(nvg, NanoVG.NVG_ROUND)
        NanoVG.nvgLineCap(nvg, NanoVG.NVG_ROUND)
        populateStrokeColor(color, x1, y1, width, height)
        NanoVG.nvgStroke(nvg)
    }

    override fun fill(x1: Float, y1: Float, x2: Float, y2: Float, color: Color, topLeftRadius: Float, topRightRadius: Float, bottomRightRadius: Float, bottomLeftRadius: Float) {
        if (color.isTransparent) {
            return
        }

        val width = x2 - x1
        val height = y2 - y1

        NanoVG.nvgBeginPath(nvg)
        NanoVG.nvgRoundedRectVarying(nvg, x1, y1, width, height, topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius)
        populateFillOrColor(color, x1, y1, width, height)
        NanoVG.nvgFill(nvg)
    }

    override fun pushScissor(x1: Float, y1: Float, x2: Float, y2: Float) {
        val width = x2 - x1
        val height = y2 - y1
        NanoVG.nvgScissor(nvg, x1, y1, width, height)
    }

    override fun popScissor() {
        NanoVG.nvgResetScissor(nvg)
    }

    fun populateNvgColor(color: Color, nvgColor: NVGColor) {
        NanoVG.nvgRGBA(color.red.toByte(), color.green.toByte(), color.blue.toByte(), color.alpha.toByte(), nvgColor)
    }

    fun populateStaticColor(color: Color) {
        populateNvgColor(color, color1)

        if (color is GradientColor) {
            populateNvgColor(color.secondColor, color2)
        }
    }

    fun populateColor(
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
                            nvg,
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
                            nvg,
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
                            nvg,
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
                    nvg,
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

    fun populateFillOrColor(color: Color, x: Float, y: Float, width: Float, height: Float) {
        if (populateColor(color, x, y, width, height)) {
            NanoVG.nvgFillPaint(nvg, paint)
        } else {
            NanoVG.nvgFillColor(nvg, color1)
        }
    }

    fun populateStrokeColor(color: Color, x: Float, y: Float, width: Float, height: Float) {
        if (populateColor(color, x, y, width, height)) {
            NanoVG.nvgStrokePaint(nvg, paint)
        } else {
            NanoVG.nvgStrokeColor(nvg, color1)
        }
    }

}
