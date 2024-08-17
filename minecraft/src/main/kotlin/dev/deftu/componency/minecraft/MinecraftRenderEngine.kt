package dev.deftu.componency.minecraft

import dev.deftu.componency.color.Color
import dev.deftu.componency.color.GradientColor
import dev.deftu.componency.color.GradientType
import dev.deftu.componency.engine.RenderEngine
import dev.deftu.componency.font.Font
import dev.deftu.componency.image.Image
import dev.deftu.componency.image.ImageType
import dev.deftu.componency.minecraft.nvg.NSvgContext
import dev.deftu.componency.minecraft.nvg.NvgContext
import dev.deftu.componency.styling.StrokeSide
import dev.deftu.omnicore.client.render.OmniRenderState
import dev.deftu.omnicore.client.render.OmniResolution
import dev.deftu.omnicore.client.render.OmniTextureManager
import dev.deftu.omnicore.client.shaders.BlendState
import dev.deftu.textile.TextHolder
import org.lwjgl.nanovg.NSVGImage
import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NVGPaint
import org.lwjgl.nanovg.NanoSVG
import org.lwjgl.nanovg.NanoVG
import org.lwjgl.opengl.GL11
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.abs

public object MinecraftRenderEngine : RenderEngine {

    @Suppress("unused")
    internal class NvgFont(
        val handle: Int,
        val buffer: ByteBuffer
    )

    override val viewportWidth: Int
        get() = OmniResolution.scaledWidth

    override val viewportHeight: Int
        get() = OmniResolution.scaledHeight

    override val pixelRatio: Float
        get() = OmniResolution.scaleFactor.toFloat()

    override val animationFps: Int = 244

    private val PIXELS = MemoryUtil.memAlloc(3).put(112).put(120).put(0).flip() as ByteBuffer

    private var isDrawing = false
    private lateinit var context: NvgContext
    private lateinit var svgContext: NSvgContext

    private val fonts = mutableMapOf<Font, NvgFont>()
    private val images = mutableMapOf<Image, Int>()
    private val svgs = mutableMapOf<Image, Pair<NSVGImage, MutableMap<Int, Int>>>()

    private val nvgColor = NVGColor.malloc()
    private val nvgColor2 = NVGColor.malloc()
    private val nvgPaint = NVGPaint.malloc()

    // Render state
    private var blendState: BlendState? = null
    private var depthState: Boolean? = null
    private var activeTexture: Int? = null
    private var textureState: Int? = null

    override fun initialize() {
        if (!::context.isInitialized) {
            context = NvgContext()
        }

        if (!::svgContext.isInitialized) {
            svgContext = NSvgContext()
        }

        require(context.isCreated) { "Context was not created" }
        require(svgContext.isCreated) { "SVG context was not created" }
    }

    override fun destroy() {
        // no-op - This implementation is persistent
    }

    override fun startFrame() {
        if (isDrawing) {
            throw IllegalStateException("Already drawing")
        }

        // Capture old GL state
        blendState = BlendState.active()
        depthState = GL11.glGetBoolean(GL11.GL_DEPTH_TEST)
        activeTexture = OmniTextureManager.getActiveTexture()
        activeTexture?.let(OmniTextureManager::setActiveTexture)
        textureState = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D)

        initialize()
        if (!context.isOpenGl3) {
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS)
        }

        NanoVG.nvgBeginFrame(context.handle, viewportWidth.toFloat(), viewportHeight.toFloat(), pixelRatio)
        isDrawing = true
    }

    override fun endFrame() {
        if (!isDrawing) {
            throw IllegalStateException("Not drawing")
        }

        NanoVG.nvgEndFrame(context.handle)
        if (!context.isOpenGl3) {
            GL11.glPopAttrib()
        }

        // Reset Minecraft's GL state tracking
        OmniRenderState.setBlendFuncSeparate(OmniRenderState.SrcFactor.SRC_ALPHA, OmniRenderState.DstFactor.ONE_MINUS_SRC_ALPHA, OmniRenderState.SrcFactor.SRC_ALPHA, OmniRenderState.DstFactor.ONE_MINUS_SRC_ALPHA)
        OmniRenderState.enableBlend()
        OmniRenderState.disableDepth()

        // Restore old GL state
        blendState?.activate()
        depthState?.let(OmniRenderState::toggleDepth)
        textureState?.let { textureState -> GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureState) }
        activeTexture?.let(OmniTextureManager::setActiveTexture)

        isDrawing = false
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
        if (color.isTransparent || strokeWidth == 0f) {
            return
        }

        val width = x2 - x1
        val height = y2 - y1

        NanoVG.nvgBeginPath(context.handle)
        NanoVG.nvgRoundedRectVarying(context.handle, x1, y1, width, height, topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius)
        NanoVG.nvgStrokeWidth(context.handle, strokeWidth)
        NanoVG.nvgLineJoin(context.handle, NanoVG.NVG_ROUND)
        NanoVG.nvgLineCap(context.handle, NanoVG.NVG_ROUND)
        populateStrokeColor(color, x1, y1, width, height)
        NanoVG.nvgStroke(context.handle)
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
        if (color.isTransparent) {
            return
        }

        val width = x2 - x1
        val height = y2 - y1

        NanoVG.nvgBeginPath(context.handle)
        NanoVG.nvgRoundedRectVarying(context.handle, x1, y1, width, height, topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius)
        populateFillOrColor(color, x1, y1, width, height)
        NanoVG.nvgFill(context.handle)
    }

    override fun text(font: Font, text: TextHolder, x: Float, y: Float, color: Color, fontSize: Float) {
        val (width, height) = textSize(font, text, fontSize)

        NanoVG.nvgBeginPath(context.handle)
        NanoVG.nvgFontSize(context.handle, fontSize)
        NanoVG.nvgFontFaceId(context.handle, getOrPopulateFont(font).handle)
        NanoVG.nvgTextAlign(context.handle, NanoVG.NVG_ALIGN_LEFT or NanoVG.NVG_ALIGN_TOP)
        populateFillOrColor(color, x, y, width, height)
        NanoVG.nvgFillColor(context.handle, nvgColor)
        NanoVG.nvgText(context.handle, x, y, text.asUnformattedString())
    }

    override fun textSize(font: Font, text: TextHolder, fontSize: Float): Pair<Float, Float> {
        val string = text.asUnformattedString().let { if (it.endsWith(" ")) "$it " else it }

        val output = FloatArray(4)
        NanoVG.nvgFontFaceId(context.handle, getOrPopulateFont(font).handle)
        NanoVG.nvgTextAlign(context.handle, NanoVG.NVG_ALIGN_LEFT or NanoVG.NVG_ALIGN_TOP)
        NanoVG.nvgFontSize(context.handle, fontSize)
        NanoVG.nvgTextBounds(context.handle, 0f, 0f, string, output)

        val width = output[2] - output[0]
        val height = output[3] - output[1]
        return width to height
    }

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

        NanoVG.nvgImagePattern(context.handle, x1, y1, width, height, 0f, getOrPopulateImage(image, width, height), 1f, nvgPaint)
        if (!color.isTransparent) {
            populateNvgColor(color, nvgPaint.innerColor())
        }

        NanoVG.nvgBeginPath(context.handle)
        NanoVG.nvgRoundedRectVarying(context.handle, x1, y1, width, height, topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius)
        NanoVG.nvgFillPaint(context.handle, nvgPaint)
        NanoVG.nvgFill(context.handle)
    }

    private fun getOrPopulateFont(font: Font): NvgFont {
        return fonts.getOrPut(font) {
            val bytes = font.data
            val buffer = ByteBuffer.allocateDirect(bytes.size).order(ByteOrder.nativeOrder()).put(bytes).flip() as ByteBuffer
            val handle = NanoVG.nvgCreateFontMem(
                context.handle,
                font.name,
                buffer,
                //#if LWJGL >= 3.3.2
                true
                //#else
                //$$ 0
                //#endif
            )

            NvgFont(handle, buffer)
        }
    }

    private fun getOrPopulateImage(image: Image, width: Float, height: Float): Int {
        return images.getOrPut(image) {
            val bytes = image.data

            when (image.type) {
                ImageType.RASTER -> {
                    val buffer = run {
                        val buffer = ByteBuffer.allocateDirect(bytes.size)
                            .order(ByteOrder.nativeOrder())
                            .put(bytes)
                            .flip() as ByteBuffer
                        val width = IntArray(1)
                        val height = IntArray(1)
                        val result = STBImage.stbi_load_from_memory(buffer, width, height, IntArray(1), 4) ?: throw IllegalStateException("Failed to load image")
                        image.width = width[0].toFloat()
                        image.height = height[0].toFloat()

                        result
                    }

                    NanoVG.nvgCreateImageRGBA(context.handle, image.width.toInt(), image.height.toInt(), 0, buffer)
                }

                ImageType.VECTOR -> {
                    val (svgImage, map) = svgs.getOrPut(image) {
                        val buffer = ByteBuffer.allocateDirect(bytes.size + 1) // +1 for null terminator
                            .order(ByteOrder.nativeOrder())
                            .put(bytes)
                            .put(0) // null terminator
                            .flip() as ByteBuffer
                        val handle = loadSvg(image, buffer)
                        return handle
                    }

                    if (image.width == 0f || image.height == 0f) {
                        image.width = svgImage.width()
                        image.height = svgImage.height()
                    }

                    map.getOrPut(width.hashCode() * 31 + height.hashCode()) {
                        resizeSvg(svgImage, width, height)
                    }
                }

                else -> throw UnsupportedOperationException("Unsupported image type")
            }
        }
    }

    private fun loadSvg(image: Image, data: ByteBuffer): Int {
        val svgImage = NanoSVG.nsvgParse(data, PIXELS, 96f) ?: throw IllegalStateException("Failed to parse SVG")
        image.width = svgImage.width()
        image.height = svgImage.height()

        val map = mutableMapOf<Int, Int>()
        val handle = resizeSvg(svgImage, svgImage.width(), svgImage.height())
        map[image.width.hashCode() * 31 + image.height.hashCode()] = handle
        svgs[image] = svgImage to map

        return handle
    }

    private fun resizeSvg(svgImage: NSVGImage, width: Float, height: Float): Int {
        val w = (width * 2f).toInt()
        val h = (height * 2f).toInt()

        val dest = MemoryUtil.memAlloc(w * h * 4)
        val scale = (if (abs((width / svgImage.width()) - 1f) <= abs((height / svgImage.height()) - 1f)) {
            width / svgImage.width()
        } else {
            height / svgImage.height()
        }) * 2f

        NanoSVG.nsvgRasterize(svgContext.handle, svgImage, 0f, 0f, scale, dest, w, h, w * 4)
        return NanoVG.nvgCreateImageRGBA(context.handle, w, h, 0, dest)
    }

    private fun populateNvgColor(color: Color, nvgColor: NVGColor) {
        val rgba = color.rgba
        NanoVG.nvgRGBA(
            (rgba shr 16 and 0xFF).toByte(),
            (rgba shr 8 and 0xFF).toByte(),
            (rgba and 0xFF).toByte(),
            (rgba shr 24 and 0xFF).toByte(),
            nvgColor
        )
    }

    private fun populateStaticColor(color: Color) {
        populateNvgColor(color, nvgColor)

        if (color is GradientColor) {
            populateNvgColor(color.secondColor, nvgColor2)
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
                            context.handle,
                            x,
                            y,
                            x + width,
                            y,
                            nvgColor,
                            nvgColor2,
                            nvgPaint
                        )
                    }

                    GradientType.LinearDirection.VERTICAL -> {
                        NanoVG.nvgLinearGradient(
                            context.handle,
                            x,
                            y,
                            x,
                            y + height,
                            nvgColor,
                            nvgColor2,
                            nvgPaint
                        )
                    }

                    GradientType.LinearDirection.DIAGONAL -> {
                        NanoVG.nvgLinearGradient(
                            context.handle,
                            x,
                            y,
                            x + width,
                            y + height,
                            nvgColor,
                            nvgColor2,
                            nvgPaint
                        )
                    }
                }
            }

            is GradientType.Radial -> {
                val (centerX, centerY) = type.center
                NanoVG.nvgRadialGradient(
                    context.handle,
                    if (centerX == -1f) x + width / 2 else centerX,
                    if (centerY == -1f) y + height / 2 else centerY,
                    type.innerRadius,
                    type.outerRadius,
                    nvgColor,
                    nvgColor2,
                    nvgPaint
                )
            }
        }

        return true
    }

    private fun populateFillOrColor(color: Color, x: Float, y: Float, width: Float, height: Float) {
        if (populateColor(color, x, y, width, height)) {
            NanoVG.nvgFillPaint(context.handle, nvgPaint)
        } else {
            NanoVG.nvgFillColor(context.handle, nvgColor)
        }
    }

    private fun populateStrokeColor(color: Color, x: Float, y: Float, width: Float, height: Float) {
        if (populateColor(color, x, y, width, height)) {
            NanoVG.nvgStrokePaint(context.handle, nvgPaint)
        } else {
            NanoVG.nvgStrokeColor(context.handle, nvgColor)
        }
    }

}
