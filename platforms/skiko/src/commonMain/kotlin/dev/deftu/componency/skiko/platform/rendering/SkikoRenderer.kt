package dev.deftu.componency.skiko.platform.rendering

import dev.deftu.componency.platform.rendering.ImageRenderer
import dev.deftu.componency.platform.rendering.Renderer
import dev.deftu.componency.platform.rendering.TextRenderer
import dev.deftu.componency.stroke.StrokeSide
import dev.deftu.kolor.Color
import dev.deftu.kolor.NamedColors
import org.jetbrains.skia.*

public class SkikoRenderer(
    private val initialWidth: Int,
    private val initialHeight: Int,
) : Renderer {

    override val isDrawing: Boolean
        get() = TODO("Not yet implemented")

    override val textRenderer: TextRenderer
        get() = TODO("Not yet implemented")

    override val imageRenderer: ImageRenderer
        get() = TODO("Not yet implemented")

    private lateinit var context: DirectContext
    private lateinit var renderTarget: BackendRenderTarget
    private lateinit var surface: Surface

    public val canvas: Canvas
        get() = surface.canvas

    public val paint: Paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = NamedColors.BLACK.rgb // Default color, can be changed later
    }

    private var activeFilter: ColorFilter? = null

    private fun initialize(): Boolean {
        if (!this::context.isInitialized) {
            this.context = DirectContext.makeGL()
        }

        if (this::context.isInitialized && !this::renderTarget.isInitialized) {
            this.renderTarget = BackendRenderTarget.makeGL(
                width = initialWidth,
                height = initialHeight,
                sampleCnt = 0,
                stencilBits = 8,
                fbId = 0,
                fbFormat = FramebufferFormat.GR_GL_RGBA8,
            )
        }

        if (this::renderTarget.isInitialized && !this::surface.isInitialized) {
            this.surface = Surface.makeFromBackendRenderTarget(
                context = context,
                rt = renderTarget,
                origin = SurfaceOrigin.BOTTOM_LEFT,
                colorFormat = SurfaceColorFormat.RGBA_8888,
                colorSpace = ColorSpace.sRGB,
            ) ?: throw IllegalStateException("Failed to create Skia surface")
        }

        return this::surface.isInitialized
    }

    override fun startFrame() {
        if (!initialize()) {
            return // We cannot proceed with our environment all broken
        }

        this.context.resetGLAll()
        this.paint.reset()
        this.canvas.clear(NamedColors.WHITE.rgb) // Clear the canvas with white color
        this.canvas.saveLayer(null, this.paint.apply {
            this.colorFilter = this@SkikoRenderer.activeFilter
        })
    }

    override fun endFrame() {
        this.canvas.restore()
        this.context.flush()
    }

    override fun fill(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        color: Color,
        topLeftRadius: Float,
        topRightRadius: Float,
        bottomRightRadius: Float,
        bottomLeftRadius: Float
    ) {
        this.paint.reset()
        this.paint.setARGB(color.alpha, color.red, color.green, color.blue)
        this.paint.setAlphaf(color.alpha.toFloat() / 255f)
        this.paint.isAntiAlias = true

        if (
            topLeftRadius > 0f ||
            topRightRadius > 0f ||
            bottomRightRadius > 0f ||
            bottomLeftRadius > 0f
        ) {
            this.canvas.drawRRect(RRect.Companion.makeXYWH(
                x1, y1, x2 - x1, y2 - y1,
                topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius
            ), this.paint)
        } else {
            this.canvas.drawRect(Rect.makeXYWH(x1, y1, x2 - x1, y2 - y1), this.paint)
        }
    }

    override fun stroke(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        color: Color,
        strokeWidth: Float,
        strokeSides: Set<StrokeSide>,
        topLeftRadius: Float,
        topRightRadius: Float,
        bottomRightRadius: Float,
        bottomLeftRadius: Float
    ) {
        this.paint.reset()
        this.paint.setARGB(color.alpha, color.red, color.green, color.blue)
        this.paint.setAlphaf(color.alpha.toFloat() / 255f)
        this.paint.isAntiAlias = true
        this.paint.strokeWidth = strokeWidth

        val width = x2 - x1
        val height = y2 - y1

        val rrect = if (
            topLeftRadius > 0f ||
            topRightRadius > 0f ||
            bottomRightRadius > 0f ||
            bottomLeftRadius > 0f
        ) {
            RRect.makeComplexXYWH(
                x1, y1, width, height,
                floatArrayOf(
                    topLeftRadius, topLeftRadius,
                    topRightRadius, topRightRadius,
                    bottomRightRadius, bottomRightRadius,
                    bottomLeftRadius, bottomLeftRadius
                )
            )
        } else {
            null
        }

        if (strokeSides.size == 4) {
            if (rrect != null) {
                canvas.drawRRect(rrect, paint)
            } else {
                canvas.drawRect(Rect.makeXYWH(x1, y1, width, height), paint)
            }
        } else {
            // Custom per-side logic. Can use drawLine() or path.
            if (strokeSides.contains(StrokeSide.TOP)) {
                canvas.drawLine(x1, y1, x2, y1, paint)
            }

            if (strokeSides.contains(StrokeSide.RIGHT)) {
                canvas.drawLine(x2, y1, x2, y2, paint)
            }

            if (strokeSides.contains(StrokeSide.BOTTOM)) {
                canvas.drawLine(x2, y2, x1, y2, paint)
            }

            if (strokeSides.contains(StrokeSide.LEFT)) {
                canvas.drawLine(x1, y2, x1, y1, paint)
            }
        }
    }

    override fun pushScissor(x1: Float, y1: Float, x2: Float, y2: Float) {
        this.canvas.save()
        this.canvas.clipRect(Rect.makeXYWH(x1, y1, x2 - x1, y2 - y1))
    }

    override fun popScissor() {
        this.canvas.restore()
    }

}
