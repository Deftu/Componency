package xyz.deftu.componency.font.impl

import net.minecraft.util.Identifier
import org.lwjgl.opengl.GL11
import xyz.deftu.componency.components.BaseComponent
import xyz.deftu.componency.font.FontProvider
import xyz.deftu.multi.*
import xyz.deftu.text.Text
import xyz.deftu.text.TextFormatting
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.font.FontRenderContext
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import kotlin.math.ceil
import kotlin.math.sqrt

private data class Glyph(
    val u: Float,
    val v: Float,
    val width: Int,
    val height: Int,
    val texture: Identifier?,
    val map: GlyphMap
)

private class GlyphMap(
    val font: Font,
    val start: Char,
    val end: Char
) {
    companion object {
        private const val PADDING = 5
        private val textureChars = "0123456789abcdefghijklmnopqrstuvwxyz_-".toCharArray()
    }

    private val texture by lazy {
        Identifier("fonts/${buildString {
            repeat(16) {
                append(textureChars.random())
            }
        }}")
    }
    private val glyphs = mutableMapOf<Char, Glyph>()
    private var generated = false
    private val transform = AffineTransform()
    private val context = FontRenderContext(transform, true, false)

    var width: Int = 0
        private set
    var height: Int = 0
        private set

    fun getGlyph(
        char: Char
    ) = glyphs[char] ?: run {
        generate()
        glyphs[char]
    }

    fun has(char: Char) = glyphs.containsKey(char)

    fun destroy() {
        MultiClient.getInstance().textureManager.destroyTexture(texture)
        glyphs.clear()
        width = -1
        height = -1
        generated = false
    }

    private fun generate() {
        if (generated) return

        glyphs.clear()

        val generatedGlyphs = mutableMapOf<Char, Glyph>()
        val range = end - start - 1
        val charsVert = ceil(sqrt(range.toDouble()) * 1.5).toInt()
        var i = 0
        var x = 0
        var y = 0
        var largestX = 0
        var largestY = 0
        var currentRowLargestY = 0
        var charNX = 0
        while (i <= range) {
            val current = start + i
            val bounds = font.getStringBounds(current.toString(), context)
            val width = ceil(bounds.width).toInt()
            val height = ceil(bounds.height).toInt()

            largestX = maxOf(largestX, x + width)
            largestY = maxOf(largestY, y + height)

            if (charNX >= charsVert) {
                x = 0
                y += currentRowLargestY + PADDING
                charNX = 0
                currentRowLargestY = 0
            }

            currentRowLargestY = maxOf(currentRowLargestY, height)
            generatedGlyphs[current] = Glyph(x.toFloat(), y.toFloat(), width, height, texture, this)
            x += width + PADDING

            i++
            charNX++
        }

        val image = BufferedImage(maxOf(largestX + PADDING, 1), maxOf(largestY + PADDING, 1), BufferedImage.TYPE_INT_ARGB)
        width = image.width
        height = image.height
        val graphics = image.createGraphics()
        graphics.font = font
        graphics.color = Color(255, 255, 255, 0)
        graphics.fillRect(0, 0, width, height)
        graphics.color = Color.WHITE

        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF)
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF)
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        generatedGlyphs.forEach { (char, glyph) ->
            val metrics = graphics.fontMetrics
            graphics.drawString(char.toString(), glyph.u, glyph.v + metrics.ascent)
            glyphs[char] = glyph
        }

        MultiClient.getTextureManager().registerImageTexture(texture, image)
        generated = true
    }
}

class StandardFontProvider(
    private val path: String,
    private val type: Int = Font.TRUETYPE_FONT,
    private val size: Float = FONT_SIZE
) : FontProvider {
    companion object {
        private const val FONT_SIZE = 9f
        private val formattingCodes by lazy {
            TextFormatting.values().map(TextFormatting::code)
        }
    }

    override var cached: FontProvider = this
    override var recalculate = false
    override var attachedTo: BaseComponent? = null

    private var font: Font? = null
    private var isAnimating = false

    private var prevScaleFactor = 1f
    private var scaleFactor = 1f
    private var renderScaleFactor = 1f
    private val totalScaleFactor: Float
        get() = scaleFactor + renderScaleFactor

    private val glyphMaps = mutableListOf<GlyphMap>()

    init {
        initialize()
    }

    override fun setAnimating(animating: Boolean) {
        isAnimating = animating
    }

    override fun getWidth(text: Text, shadow: Boolean): Float {
        val chars = text.asString().toCharArray()
        var width = 0f
        for (char in chars) {
            val glyph = locateGlyph(char)
            width += glyph.width / scaleFactor
        }

        return width
    }

    override fun getHeight(text: Text, shadow: Boolean): Float {
        val chars = text.asString().toCharArray()
        var height = 0f
        for (char in chars) {
            val glyph = locateGlyph(char)
            height = maxOf(height, glyph.height / scaleFactor)
        }

        return height
    }

    override fun getLineHeight(shadow: Boolean): Float {
        return FONT_SIZE * size
    }

    override fun handleDestroy() {
        destroy()
    }

    override fun render(
        stack: MultiMatrixStack,
        text: Text,
        x: Float,
        y: Float,
        scale: Float,
        shadow: Boolean,
        color: Color
    ) {
        if (!isAnimating) renderScaleFactor = scale
        checkSize()

        if (shadow) {
            render(stack, text, x + 0.5f, y + 0.5f, scale, false, Color(0, 0, 0, color.alpha))
        }

        stack.push()
        stack.translate(x, y, 1f)
        stack.scale(1f / scaleFactor, 1f / scaleFactor, 1f)
        stack.scale(scale, scale, 1f)

        var currentColor = color.rgb
        val content = text.asFormattedString()
        val chars = content.toCharArray()

        var currentX = 0f
        var currentY = 0f
        var largestHeight = 0f
        var i = 0
        while (i < chars.size) {
            val current = chars[i]
            if (current == TextFormatting.COLOR_CHAR) {
                if (i + 1 >= chars.size) break

                val code = chars[i + 1]
                if (code in formattingCodes) {
                    val formatting = TextFormatting.from(code)
                    currentColor = if (formatting == TextFormatting.RESET) color.rgb else formatting.color?.rgb ?: currentColor
                    i += 2
                    continue
                }

                i++
                continue
            }

            if (current == '\n') {
                currentX = 0f
                currentY += largestHeight
                largestHeight = 0f
                i++

                continue
            }

            val glyph = locateGlyph(current)
            largestHeight = maxOf(largestHeight, glyph.height.toFloat())
            val newColor = Color(currentColor)
            val red = newColor.red / 255f
            val green = newColor.green / 255f
            val blue = newColor.blue / 255f
            val alpha = newColor.alpha / 255f

            val buffer = MultiTessellator.getFromBuffer()
            glyph.texture?.let { MultiRenderSystem.setShaderTexture(0, it) }
            buffer.beginWithDefaultShader(MultiTessellator.DrawModes.QUADS, MultiTessellator.VertexFormats.POSITION_TEXTURE_COLOR)
            val u1 = glyph.u / glyph.map.width
            val v1 = glyph.v / glyph.map.height
            val u2 = (glyph.u + glyph.width) / glyph.map.width
            val v2 = (glyph.v + glyph.height) / glyph.map.height

            val glyphHeight = glyph.height.toFloat()
            val glyphWidth = glyph.width.toFloat()

            buffer.vertex(stack, currentX, glyphHeight + currentY, 0f).texture(u1, v2).color(red, green, blue, alpha).next()
            buffer.vertex(stack, glyphWidth + currentX, glyphHeight + currentY, 0f).texture(u2, v2).color(red, green, blue, alpha).next()
            buffer.vertex(stack, glyphWidth + currentX, currentY, 0f).texture(u2, v1).color(red, green, blue, alpha).next()
            buffer.vertex(stack, currentX, currentY, 0f).texture(u1, v1).color(red, green, blue, alpha).next()

            MultiGlStateManager.enableBlend()
            MultiGlStateManager.defaultBlendFunc()
            MultiGlStateManager.enableTexture2D()
            MultiGlStateManager.disableCull()
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR)
            buffer.draw()
            MultiGlStateManager.enableCull()
            MultiGlStateManager.disableTexture2D()
            MultiGlStateManager.disableBlend()

            currentX += glyph.width.toFloat()
            i++
        }

        stack.pop()
    }

    fun initialize() {
        prevScaleFactor = totalScaleFactor
        scaleFactor = MultiResolution.scaleFactor.toFloat()
        font = loadFont(path, type).deriveFont(size * scaleFactor + renderScaleFactor)
    }

    fun destroy() {
        glyphMaps.forEach { map ->
            map.destroy()
        }
        glyphMaps.clear()
    }

    private fun checkSize() {
        if (totalScaleFactor != prevScaleFactor) {
            destroy()
            initialize()
        }
    }

    private fun loadFont(
        path: String,
        type: Int = Font.TRUETYPE_FONT
    ) = Font.createFont(type, StandardFontProvider::class.java.getResourceAsStream(buildString {
        if (!path.startsWith("/")) append("/")
        append(path)
    }))

    private fun locateGlyph(char: Char): Glyph {
        return glyphMaps.firstOrNull {
            it.has(char)
        }?.getGlyph(char) ?: run {
            val map = GlyphMap(font!!, char, char + 32)
            glyphMaps.add(map)
            map.getGlyph(char)!!
        }
    }
}
