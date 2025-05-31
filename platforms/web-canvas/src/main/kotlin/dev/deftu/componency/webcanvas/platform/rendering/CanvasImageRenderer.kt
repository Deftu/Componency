package dev.deftu.componency.webcanvas.platform.rendering

import dev.deftu.componency.image.Image
import dev.deftu.componency.platform.rendering.ImageRenderer
import dev.deftu.componency.webcanvas.platform.image.CanvasImageLoader
import dev.deftu.componency.webcanvas.platform.image.CanvasPlatformImage
import dev.deftu.kolor.Color
import dev.deftu.kolor.toCss
import org.w3c.dom.CanvasRenderingContext2D

public class CanvasImageRenderer(private val context: CanvasRenderingContext2D) : ImageRenderer {

    override fun image(
        image: Image,
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

        // Apply tint color if not fully opaque white
        if (color.alpha < 1f || color.red != 1 || color.green != 1 || color.blue != 1) {
            context.fillStyle = color.toCss() // implement Color.toCss() for rgba()
            // We'll use a clipping path for rounded rect before drawing the image
        }

        // Create a rounded rectangle path for clipping
        createRoundedRectPath(
            context,
            x1,
            y1,
            width,
            height,
            topLeftRadius,
            topRightRadius,
            bottomRightRadius,
            bottomLeftRadius
        )

        context.clip()

        // Draw the image (assuming Image has an underlying HTMLImageElement or Canvas)
        val img = (CanvasImageLoader.convertEncodedToPlatform(image) as CanvasPlatformImage).element // You need to expose this or something similar

        context.drawImage(img, x1.toDouble(), y1.toDouble(), width.toDouble(), height.toDouble())

        // Apply tint overlay if color is not white or not fully opaque
        if (color.alpha < 1f || color.red != 1 || color.green != 1 || color.blue != 1) {
            context.fill()
        }

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
