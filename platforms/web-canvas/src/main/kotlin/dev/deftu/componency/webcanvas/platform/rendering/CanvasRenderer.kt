package dev.deftu.componency.webcanvas.platform.rendering

import dev.deftu.componency.platform.rendering.ImageRenderer
import dev.deftu.componency.platform.rendering.Renderer
import dev.deftu.componency.stroke.StrokeSide
import dev.deftu.kolor.Color
import dev.deftu.kolor.toCss
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement

public class CanvasRenderer(
    private val element: HTMLCanvasElement,
    private val context: CanvasRenderingContext2D,
) : Renderer {

    override var isDrawing: Boolean = false
        private set

    override val textRenderer: CanvasTextRenderer = CanvasTextRenderer(context)

    override val imageRenderer: ImageRenderer = CanvasImageRenderer(context)

    override fun startFrame() {
        context.save()
        context.clearRect(0.0, 0.0, element.width.toDouble(), element.height.toDouble())
        this.isDrawing = true
    }

    override fun endFrame() {
        context.restore()
        this.isDrawing = false
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
        val width = x2 - x1
        val height = y2 - y1

        context.save()
        context.fillStyle = color.toCss()

        // Use your rounded rect helper or write it inline
        createRoundedRectPath(context, x1, y1, width, height, topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius)
        context.fill()
        context.restore()
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
        // For simplicity, stroke all sides (implement selective stroke if needed)
        val width = x2 - x1
        val height = y2 - y1

        context.save()
        context.strokeStyle = color.toCss()
        context.lineWidth = strokeWidth.toDouble()

        createRoundedRectPath(context, x1, y1, width, height, topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius)
        context.stroke()
        context.restore()
    }

    override fun pushScissor(x1: Float, y1: Float, x2: Float, y2: Float) {
        val convertedX1 = x1.toDouble()
        val convertedY1 = y1.toDouble()
        val convertedX2 = x2.toDouble()
        val convertedY2 = y2.toDouble()

        context.save()
        context.beginPath()
        context.rect(convertedX1, convertedY1, convertedX2 - convertedX1, convertedY2 - convertedY1)
        context.clip()
    }

    override fun popScissor() {
        context.restore()
    }

    private fun createRoundedRectPath(
        ctx: CanvasRenderingContext2D,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        radiusTL: Float,
        radiusTR: Float,
        radiusBR: Float,
        radiusBL: Float
    ) {
        val convertedX = x.toDouble()
        val convertedY = y.toDouble()

        ctx.beginPath()
        ctx.moveTo(convertedX + radiusTL, convertedY)
        ctx.lineTo(convertedX + width - radiusTR, convertedY)
        ctx.quadraticCurveTo(convertedX + width, convertedY, convertedX + width, convertedY + radiusTR)
        ctx.lineTo(convertedX + width, convertedY + height - radiusBR)
        ctx.quadraticCurveTo(convertedX + width, convertedY + height, convertedX + width - radiusBR, convertedY + height)
        ctx.lineTo(convertedX + radiusBL, convertedY + height)
        ctx.quadraticCurveTo(convertedX, convertedY + height, convertedX, convertedY + height - radiusBL)
        ctx.lineTo(convertedX, convertedY + radiusTL)
        ctx.quadraticCurveTo(convertedX, convertedY, convertedX + radiusTL, convertedY)
        ctx.closePath()
    }

}
