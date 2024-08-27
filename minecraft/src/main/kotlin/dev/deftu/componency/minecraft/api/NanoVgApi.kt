package dev.deftu.componency.minecraft.api

import java.nio.ByteBuffer

public interface NanoVgApi {

    @Suppress("PropertyName")
    public interface NanoVgConstants {

        public val NVG_ROUND: Int
        public val NVG_ALIGN_LEFT: Int
        public val NVG_ALIGN_TOP: Int

    }

    public val constants: NanoVgConstants

    public val handle: Long
    public val svgHandle: Long

    /**
     * If this instance is not set up already, it will set up the instance (initializes it's NVG and NSVG context).
     *
     * @throws IllegalStateException if the instance failed to set up.
     *
     * @since 0.2.0
     * @author Deftu
     */
    public fun maybeSetup()

    /**
     * Starts a new frame.
     */
    public fun beginFrame(width: Float, height: Float, scale: Float)

    /**
     * Ends the current frame.
     */
    public fun endFrame()

    /**
     * Creates a new NanoVG paint and returns the address.
     */
    public fun createPaint(): Long

    /**
     * Fills the paint with the item at the address.
     */
    public fun fillPaint(address: Long)

    public fun getPaintColor(address: Long): Long

    public fun createColor(): Long

    public fun fillColor(address: Long)

    public fun rgba(address: Long, rgba: Int)

    public fun beginPath()

    public fun fill()

    public fun roundedRectVarying(x: Float, y: Float, w: Float, h: Float, tl: Float, tr: Float, br: Float, bl: Float)

    public fun lineJoin(join: Int)

    public fun lineCap(cap: Int)

    public fun stroke()

    public fun strokeWidth(width: Float)

    public fun strokePaint(address: Long)

    public fun strokeColor(address: Long)

    public fun createFont(name: String, buffer: ByteBuffer): Int

    public fun fontSize(size: Float)

    public fun fontFaceId(id: Int)

    public fun textAlign(align: Int)

    public fun text(x: Float, y: Float, text: String)

    public fun textBounds(x: Float, y: Float, text: String, bounds: FloatArray)

    public fun createImage(width: Float, height: Float, buffer: ByteBuffer): Int

    public fun scissor(x: Float, y: Float, w: Float, h: Float)

    public fun resetScissor()

    public fun imagePattern(x: Float, y: Float, w: Float, h: Float, angle: Float, image: Int, alpha: Float, address: Long)

    public fun linearGradient(address: Long, x0: Float, y0: Float, x1: Float, y1: Float, startColor: Long, endColor: Long)

    public fun radialGradient(address: Long, cx: Float, cy: Float, inr: Float, outr: Float, startColor: Long, endColor: Long)

    public fun svgBounds(address: Long): Pair<Float, Float>

    public fun parseSvg(data: ByteBuffer): Triple<Long, Float, Float>

    public fun rasterizeSvg(address: Long, x: Float, y: Float, w: Int, h: Int, scale: Float, stride: Int, data: ByteBuffer)

}
