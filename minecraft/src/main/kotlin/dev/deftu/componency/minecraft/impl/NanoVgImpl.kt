package dev.deftu.componency.minecraft.impl

import dev.deftu.componency.color.Color
import dev.deftu.componency.minecraft.api.NanoVgApi
import org.lwjgl.nanovg.*
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
private typealias JBoolean = java.lang.Boolean

public class NanoVgImpl(
    private val isOpenGl3: JBoolean
) : NanoVgApi {

    private inner class NanoVgConstantsImpl : NanoVgApi.NanoVgConstants {

        override val NVG_ROUND: Int
            get() = NanoVG.NVG_ROUND

        override val NVG_ALIGN_LEFT: Int
            get() = NanoVG.NVG_ALIGN_LEFT

        override val NVG_ALIGN_TOP: Int
            get() = NanoVG.NVG_ALIGN_TOP

    }

    override val constants: NanoVgApi.NanoVgConstants by lazy {
        NanoVgConstantsImpl()
    }

    override var handle: Long = -1L
        private set
    override var svgHandle: Long = -1L
        private set

    private val PIXELS by lazy {
        MemoryUtil.memAlloc(3).put(112).put(120).put(0).flip() as ByteBuffer
    }

    override fun maybeSetup() {
        // First, initialize the NanoVG context
        val handle = when (isOpenGl3.booleanValue()) {
            true -> NanoVGGL3.nvgCreate(NanoVGGL3.NVG_ANTIALIAS)
            false -> NanoVGGL2.nvgCreate(NanoVGGL2.NVG_ANTIALIAS)
        }

        if (handle == MemoryUtil.NULL) {
            throw IllegalStateException("Failed to create NanoVG context")
        }

        // Then, initialize the NanoSVG context
        val svgHandle = NanoSVG.nsvgCreateRasterizer()
        if (svgHandle == MemoryUtil.NULL) {
            throw IllegalStateException("Failed to create NanoSVG context")
        }

        this.handle = handle
        this.svgHandle = svgHandle
    }

    override fun beginFrame(width: Float, height: Float, scale: Float) {
        NanoVG.nvgBeginFrame(handle, width, height, scale)
    }

    override fun endFrame() {
        NanoVG.nvgEndFrame(handle)
    }

    override fun createPaint(): Long {
        return NVGPaint.malloc().address()
    }

    override fun fillPaint(address: Long) {
        NanoVG.nvgFillPaint(handle, NVGPaint.create(address))
    }

    override fun getPaintColor(address: Long): Long {
        return NVGPaint.create(address).innerColor().address()
    }

    override fun createColor(): Long {
        return NVGColor.malloc().address()
    }

    override fun fillColor(address: Long) {
        NanoVG.nvgFillColor(handle, NVGColor.create(address))
    }

    override fun rgba(address: Long, rgba: Int) {
        NanoVG.nvgRGBA(
            (rgba shr 16 and 0xFF).toByte(),
            (rgba shr 8 and 0xFF).toByte(),
            (rgba and 0xFF).toByte(),
            (rgba shr 24 and 0xFF).toByte(),
            NVGColor.create(address)
        )
    }

    override fun beginPath() {
        NanoVG.nvgBeginPath(handle)
    }

    override fun fill() {
        NanoVG.nvgFill(handle)
    }

    override fun roundedRectVarying(
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        tl: Float,
        tr: Float,
        br: Float,
        bl: Float
    ) {
        NanoVG.nvgRoundedRectVarying(handle, x, y, w, h, tl, tr, br, bl)
    }

    override fun lineJoin(join: Int) {
        NanoVG.nvgLineJoin(handle, join)
    }

    override fun lineCap(cap: Int) {
        NanoVG.nvgLineCap(handle, cap)
    }

    override fun stroke() {
        NanoVG.nvgStroke(handle)
    }

    override fun strokeWidth(width: Float) {
        NanoVG.nvgStrokeWidth(handle, width)
    }

    override fun strokePaint(address: Long) {
        NanoVG.nvgStrokePaint(handle, NVGPaint.create(address))
    }

    override fun strokeColor(address: Long) {
        NanoVG.nvgStrokeColor(handle, NVGColor.create(address))
    }

    override fun createFont(name: String, buffer: ByteBuffer): Int {
        return NanoVG.nvgCreateFontMem(handle, name, buffer, false)
    }

    override fun fontSize(size: Float) {
        NanoVG.nvgFontSize(handle, size)
    }

    override fun fontFaceId(id: Int) {
        NanoVG.nvgFontFaceId(handle, id)
    }

    override fun textAlign(align: Int) {
        NanoVG.nvgTextAlign(handle, align)
    }

    override fun text(x: Float, y: Float, text: String) {
        NanoVG.nvgText(handle, x, y, text)
    }

    override fun textBounds(x: Float, y: Float, text: String, bounds: FloatArray) {
        NanoVG.nvgTextBounds(handle, x, y, text, bounds)
    }

    override fun createImage(width: Float, height: Float, buffer: ByteBuffer): Int {
        return NanoVG.nvgCreateImageRGBA(handle, width.toInt(), height.toInt(), NanoVG.NVG_IMAGE_FLIPY, buffer)
    }

    override fun scissor(x: Float, y: Float, w: Float, h: Float) {
        NanoVG.nvgScissor(handle, x, y, w, h)
    }

    override fun resetScissor() {
        NanoVG.nvgResetScissor(handle)
    }

    override fun imagePattern(
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        angle: Float,
        image: Int,
        alpha: Float,
        address: Long
    ) {
        NanoVG.nvgImagePattern(handle, x, y, w, h, angle, image, alpha, NVGPaint.create(address))
    }

    override fun linearGradient(address: Long, x0: Float, y0: Float, x1: Float, y1: Float, startColor: Long, endColor: Long) {
        NanoVG.nvgLinearGradient(handle, x0, y0, x1, y1, NVGColor.create(startColor), NVGColor.create(endColor), NVGPaint.create(address))
    }

    override fun radialGradient(address: Long, cx: Float, cy: Float, inr: Float, outr: Float, startColor: Long, endColor: Long) {
        NanoVG.nvgRadialGradient(handle, cx, cy, inr, outr, NVGColor.create(startColor), NVGColor.create(endColor), NVGPaint.create(address))
    }

    override fun svgBounds(address: Long): Pair<Float, Float> {
        val svg = NSVGImage.create(address)
        return svg.width() to svg.height()
    }

    override fun parseSvg(data: ByteBuffer): Triple<Long, Float, Float> {
        val result = NanoSVG.nsvgParse(data, PIXELS, 96f) ?: throw IllegalStateException("Failed to parse SVG data")
        return Triple(result.address(), result.width(), result.height())
    }

    override fun rasterizeSvg(
        address: Long,
        x: Float,
        y: Float,
        w: Int,
        h: Int,
        scale: Float,
        stride: Int,
        data: ByteBuffer
    ) {
        NanoSVG.nsvgRasterize(svgHandle, NSVGImage.create(address), x, y, scale, data, w, h, stride)
    }

}
