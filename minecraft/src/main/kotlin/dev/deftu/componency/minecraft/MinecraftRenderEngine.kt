package dev.deftu.componency.minecraft

import dev.deftu.componency.color.Color
import dev.deftu.componency.color.GradientColor
import dev.deftu.componency.color.GradientType
import dev.deftu.componency.engine.RenderEngine
import dev.deftu.componency.font.Font
import dev.deftu.componency.image.Image
import dev.deftu.componency.image.ImageType
import dev.deftu.componency.minecraft.api.NanoVgApi
import dev.deftu.componency.minecraft.api.StbApi
import dev.deftu.componency.styling.StrokeSide
import dev.deftu.omnicore.client.render.OmniRenderState
import dev.deftu.omnicore.client.render.OmniResolution
import dev.deftu.omnicore.client.render.OmniTextureManager
import dev.deftu.omnicore.client.shaders.BlendState
import dev.deftu.textile.TextHolder
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.abs

public class MinecraftRenderEngine(
    private val nanoVg: NanoVgApi,
    private val stb: StbApi
) : RenderEngine {

    @Suppress("unused")
    internal class NvgFont(
        val id: Int,
        val buffer: ByteBuffer
    )

    override val viewportWidth: Int
        get() = OmniResolution.scaledWidth

    override val viewportHeight: Int
        get() = OmniResolution.scaledHeight

    override val pixelRatio: Float
        get() = OmniResolution.scaleFactor.toFloat()

    override val animationFps: Int = 244

    private var isDrawing = false

    private var paintAddress = -1L
        get() {
            if (field == -1L) {
                field = nanoVg.createPaint()
            }

            return field
        }

    private var color1Address = -1L
        get() {
            if (field == -1L) {
                field = nanoVg.createColor()
            }

            return field
        }

    private var color2Address = -1L
        get() {
            if (field == -1L) {
                field = nanoVg.createColor()
            }

            return field
        }

    private val fonts = mutableMapOf<Font, NvgFont>()
    private val images = mutableMapOf<Image, Int>()
    private val svgs = mutableMapOf<Image, Pair<Long, MutableMap<Int, Int>>>()

    // Render state
    private var blendState: BlendState? = null
    private var depthState: Boolean? = null
    private var activeTexture: Int? = null
    private var textureState: Int? = null

    override fun initialize() {
        nanoVg.maybeSetup()
    }

    override fun destroy() {
        // no-op - This implementation is persistent
    }

    override fun startFrame() {
        if (isDrawing) {
            throw IllegalStateException("Already drawing")
        }

        initialize()

        // Capture old GL state
        blendState = BlendState.active()
        depthState = GL11.glGetBoolean(GL11.GL_DEPTH_TEST)
        activeTexture = OmniTextureManager.getActiveTexture()
        activeTexture?.let(OmniTextureManager::setActiveTexture)
        textureState = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D)

        nanoVg.beginFrame(viewportWidth.toFloat(), viewportHeight.toFloat(), pixelRatio)
        isDrawing = true
    }

    override fun endFrame() {
        if (!isDrawing) {
            throw IllegalStateException("Not drawing")
        }

        nanoVg.endFrame()

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

        nanoVg.beginPath()
        nanoVg.roundedRectVarying(x1, y1, width, height, topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius)
        nanoVg.strokeWidth(strokeWidth)
        nanoVg.lineJoin(nanoVg.constants.NVG_ROUND)
        nanoVg.lineCap(nanoVg.constants.NVG_ROUND)
        populateStrokeColor(color, x1, y1, width, height)
        nanoVg.stroke()
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

        nanoVg.beginPath()
        nanoVg.roundedRectVarying(x1, y1, width, height, topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius)
        populateFillOrColor(color, x1, y1, width, height)
        nanoVg.fill()
    }

    override fun text(font: Font, text: TextHolder, x: Float, y: Float, color: Color, fontSize: Float) {
        val (width, height) = textSize(font, text, fontSize)

        nanoVg.beginPath()
        nanoVg.fontSize(fontSize)
        nanoVg.fontFaceId(getOrPopulateFont(font).id)
        nanoVg.textAlign(nanoVg.constants.NVG_ALIGN_LEFT or nanoVg.constants.NVG_ALIGN_TOP)
        populateFillOrColor(color, x, y, width, height)
        // todo - NanoVG.nvgFillColor(nvgColor)
        nanoVg.text(x, y, text.asUnformattedString())
    }

    override fun textSize(font: Font, text: TextHolder, fontSize: Float): Pair<Float, Float> {
        val string = text.asUnformattedString().let { if (it.endsWith(" ")) "$it " else it }

        val output = FloatArray(4)
        nanoVg.fontFaceId(getOrPopulateFont(font).id)
        nanoVg.textAlign(nanoVg.constants.NVG_ALIGN_LEFT or nanoVg.constants.NVG_ALIGN_TOP)
        nanoVg.fontSize(fontSize)
        nanoVg.textBounds(0f, 0f, string, output)

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

        nanoVg.imagePattern(x1, y1, width, height, 0f, getOrPopulateImage(image, width, height), 1f, paintAddress)
        if (!color.isTransparent) {
            populateNvgColor(color, nanoVg.getPaintColor(paintAddress))
        }

        nanoVg.beginPath()
        nanoVg.roundedRectVarying(x1, y1, width, height, topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius)
        nanoVg.fillPaint(paintAddress)
        nanoVg.fill()
    }

    override fun pushScissor(x1: Float, y1: Float, x2: Float, y2: Float) {
        val width = x2 - x1
        val height = y2 - y1
        nanoVg.scissor(x1, y1, width, height)
    }

    override fun popScissor() {
        nanoVg.resetScissor()
    }

    private fun getOrPopulateFont(font: Font): NvgFont {
        return fonts.getOrPut(font) {
            val bytes = font.data
            val buffer = ByteBuffer.allocateDirect(bytes.size).order(ByteOrder.nativeOrder()).put(bytes).flip() as ByteBuffer
            val id = nanoVg.createFont(
                font.name,
                buffer
            )

            NvgFont(id, buffer)
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
                        val widthOutput = IntArray(1)
                        val heightOutput = IntArray(1)
                        val result = stb.loadFromMemory(buffer, widthOutput, heightOutput, IntArray(1), 4)
                        image.width = widthOutput[0].toFloat()
                        image.height = heightOutput[0].toFloat()

                        result
                    }

                    nanoVg.createImage(image.width, image.height, buffer)
                }

                ImageType.VECTOR -> {
                    val (svgImage, map) = svgs.getOrPut(image) {
                        val buffer = ByteBuffer.allocateDirect(bytes.size + 1) // +1 for null terminator
                            .order(ByteOrder.nativeOrder())
                            .put(bytes)
                            .put(0) // null terminator
                            .flip() as ByteBuffer
                        val id = loadSvg(image, buffer)
                        return id
                    }

                    if (image.width == 0f || image.height == 0f) {
                        val (svgWidth, svgHeight) = nanoVg.svgBounds(svgImage)
                        image.width = svgWidth
                        image.height = svgHeight
                    }

                    map.getOrPut(width.hashCode() * 31 + height.hashCode()) {
                        resizeSvg(svgImage, width, height, width, height)
                    }
                }

                else -> throw UnsupportedOperationException("Unsupported image type")
            }
        }
    }

    private fun loadSvg(image: Image, data: ByteBuffer): Int {
        val (address, svgWidth, svgHeight) = nanoVg.parseSvg(data)
        image.width = svgWidth
        image.height = svgHeight

        val map = mutableMapOf<Int, Int>()
        val id = resizeSvg(address, svgWidth, svgHeight, svgWidth, svgHeight)
        map[image.width.hashCode() * 31 + image.height.hashCode()] = id
        svgs[image] = address to map

        return id
    }

    private fun resizeSvg(address: Long, svgWidth: Float, svgHeight: Float, width: Float, height: Float): Int {
        val w = (width * 2f).toInt()
        val h = (height * 2f).toInt()

        val dest = MemoryUtil.memAlloc(w * h * 4)
        val scale = (if (abs((width / svgWidth) - 1f) <= abs((height / svgHeight) - 1f)) {
            width / svgWidth
        } else {
            height / svgHeight
        }) * 2f

        nanoVg.rasterizeSvg(address, 0f, 0f, w, h, scale, w * 4, dest)
        return nanoVg.createImage(w.toFloat(), h.toFloat(), dest)
    }

    private fun populateNvgColor(color: Color, colorAddress: Long) {
        nanoVg.rgba(colorAddress, color.rgba)
    }

    private fun populateStaticColor(color: Color) {
        populateNvgColor(color, color1Address)

        if (color is GradientColor) {
            populateNvgColor(color.secondColor, color2Address)
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
                        nanoVg.linearGradient(
                            paintAddress,
                            x,
                            y,
                            x + width,
                            y,
                            color1Address,
                            color2Address
                        )
                    }

                    GradientType.LinearDirection.VERTICAL -> {
                        nanoVg.linearGradient(
                            paintAddress,
                            x,
                            y,
                            x,
                            y + height,
                            color1Address,
                            color2Address
                        )
                    }

                    GradientType.LinearDirection.DIAGONAL -> {
                        nanoVg.linearGradient(
                            paintAddress,
                            x,
                            y,
                            x + width,
                            y + height,
                            color1Address,
                            color2Address
                        )
                    }
                }
            }

            is GradientType.Radial -> {
                val (centerX, centerY) = type.center
                nanoVg.radialGradient(
                    paintAddress,
                    if (centerX == -1f) x + width / 2 else centerX,
                    if (centerY == -1f) y + height / 2 else centerY,
                    type.innerRadius,
                    type.outerRadius,
                    color1Address,
                    color2Address
                )
            }
        }

        return true
    }

    private fun populateFillOrColor(color: Color, x: Float, y: Float, width: Float, height: Float) {
        if (populateColor(color, x, y, width, height)) {
            nanoVg.fillPaint(paintAddress)
        } else {
            nanoVg.fillColor(color1Address)
        }
    }

    private fun populateStrokeColor(color: Color, x: Float, y: Float, width: Float, height: Float) {
        if (populateColor(color, x, y, width, height)) {
            nanoVg.strokePaint(paintAddress)
        } else {
            nanoVg.strokeColor(color1Address)
        }
    }

}
