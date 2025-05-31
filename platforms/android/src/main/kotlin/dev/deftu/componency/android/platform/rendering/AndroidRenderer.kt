package dev.deftu.componency.android.platform.rendering

import android.graphics.*
import dev.deftu.componency.color.Color
import dev.deftu.componency.platform.rendering.ImageRenderer
import dev.deftu.componency.platform.rendering.Renderer
import dev.deftu.componency.platform.rendering.TextRenderer
import dev.deftu.componency.stroke.StrokeSide
import java.util.Stack

public class AndroidRenderer(
    private val canvasProvider: () -> Canvas?,
    private val tempDirProvider: () -> java.io.File
) : Renderer {

    private val _textRenderer = AndroidTextRenderer(canvasProvider, tempDirProvider)
    private val _imageRenderer = AndroidImageRenderer(canvasProvider)

    override val isDrawing: Boolean
        get() = canvasProvider() != null

    override val textRenderer: TextRenderer
        get() = _textRenderer

    override val imageRenderer: ImageRenderer
        get() = _imageRenderer

    private val scissorStack = Stack<Int>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun startFrame() {
        // Reset any global state if necessary
        scissorStack.clear()
    }

    override fun endFrame() {
        // Nothing special needed unless you want postprocessing
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
        val canvas = canvasProvider() ?: return

        configurePaint(color, fill = true)

        val rect = RectF(x1, y1, x2, y2)

        if (topLeftRadius == 0f && topRightRadius == 0f && bottomRightRadius == 0f && bottomLeftRadius == 0f) {
            canvas.drawRect(rect, paint)
        } else {
            val path = Path().apply {
                addRoundRect(
                    rect,
                    floatArrayOf(
                        topLeftRadius, topLeftRadius,
                        topRightRadius, topRightRadius,
                        bottomRightRadius, bottomRightRadius,
                        bottomLeftRadius, bottomLeftRadius
                    ),
                    Path.Direction.CW
                )
            }
            canvas.drawPath(path, paint)
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
        val canvas = canvasProvider() ?: return

        configurePaint(color, fill = false)
        paint.strokeWidth = strokeWidth

        val rect = RectF(x1, y1, x2, y2)

        if (topLeftRadius == 0f && topRightRadius == 0f && bottomRightRadius == 0f && bottomLeftRadius == 0f) {
            // Straight-line stroke
            if (strokeSides.contains(StrokeSide.TOP)) {
                canvas.drawLine(x1, y1, x2, y1, paint)
            }
            if (strokeSides.contains(StrokeSide.BOTTOM)) {
                canvas.drawLine(x1, y2, x2, y2, paint)
            }
            if (strokeSides.contains(StrokeSide.LEFT)) {
                canvas.drawLine(x1, y1, x1, y2, paint)
            }
            if (strokeSides.contains(StrokeSide.RIGHT)) {
                canvas.drawLine(x2, y1, x2, y2, paint)
            }
        } else {
            // Rounded rect stroke
            val path = Path().apply {
                addRoundRect(
                    rect,
                    floatArrayOf(
                        topLeftRadius, topLeftRadius,
                        topRightRadius, topRightRadius,
                        bottomRightRadius, bottomRightRadius,
                        bottomLeftRadius, bottomLeftRadius
                    ),
                    Path.Direction.CW
                )
            }
            paint.style = Paint.Style.STROKE
            canvas.drawPath(path, paint)
        }
    }

    override fun pushScissor(x1: Float, y1: Float, x2: Float, y2: Float) {
        val canvas = canvasProvider() ?: return
        val saveCount = canvas.save()

        val rect = RectF(x1, y1, x2, y2)
        canvas.clipRect(rect)

        scissorStack.push(saveCount)
    }

    override fun popScissor() {
        val canvas = canvasProvider() ?: return
        if (scissorStack.isNotEmpty()) {
            val saveCount = scissorStack.pop()
            canvas.restoreToCount(saveCount)
        }
    }

    private fun configurePaint(color: Color, fill: Boolean) {
        paint.reset()
        paint.isAntiAlias = true
        paint.style = if (fill) Paint.Style.FILL else Paint.Style.STROKE
        paint.color = android.graphics.Color.argb(
            (color.alpha * 255).toInt().coerceIn(0, 255),
            (color.red * 255).toInt().coerceIn(0, 255),
            (color.green * 255).toInt().coerceIn(0, 255),
            (color.blue * 255).toInt().coerceIn(0, 255)
        )
    }

}
