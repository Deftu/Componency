package dev.deftu.componency.components

import dev.deftu.componency.color.Color
import dev.deftu.componency.font.Font
import dev.deftu.componency.font.FontWeight
import dev.deftu.componency.platform.Platform
import kotlin.math.max

public class Debugger(private val component: Component<*, *>) {

    private val currentTimeSecs: Double
        get() = System.currentTimeMillis() / 1_000.0

    private var isInitialized = false
    private var lastDeltaTime = 0.0
    private var deltaTime = 0.0
    private var frameCount = 0
    private var font: Font? = null

    private var fps = 0
    private var frameTime = 0.0

    private val paddingBetweenItems = 5f
    private val fontSize = 16f
    private val x = 15f
    private val y = 15f
    private var width = 0f
    private val radius = 5f
    private val color = Color.GREEN.withAlphaPercentage(0.25f)

    private var debugReadingHeight = 0f

    private val debugReadingLines: Set<String>
        get() = setOf(
            "FPS: $fps",
            "Frame Time: ${String.format("%.2f", frameTime)} ms",
            "Delta Time: ${String.format("%.2f", deltaTime)} ms"
        )

    public var isEnabled: Boolean = false
        set(value) {
            if (value && !isInitialized) {
                initialize()
            }

            field = value
        }

    internal fun initialize() {
        if (this.isInitialized) {
            return
        }

        if (this.lastDeltaTime == 0.0) {
            this.lastDeltaTime = currentTimeSecs
        }

        this.font = loadFont()

        this.isInitialized = true
    }

    public fun render() {
        if (!isEnabled) {
            return
        }

        update()

        font ?: return // Stop ourselves from attempting to render if we don't have a font

        val platform = Component.findPlatform(component)
        renderDebugReadings(platform)
        renderComponentTree(platform, component)
    }

    private fun update() {
        val currentTime = currentTimeSecs
        this.deltaTime = currentTime - this.lastDeltaTime
        this.frameCount++

        if (this.deltaTime >= 1.0) {
            // Update
            this.fps = this.frameCount
            this.frameTime = (this.deltaTime / this.frameCount) * 1_000.0

            // Reset
            this.frameCount = 0
            this.lastDeltaTime = currentTime
        }
    }

    private fun renderDebugReadings(platform: Platform) {
        val maxWidth = debugReadingLines.maxOf { platform.renderer.textRenderer.textSize(font!!, it, fontSize).width }
        width = max(width, maxWidth)

        var height = 0f
        for (line in debugReadingLines) {
            height += platform.renderer.textRenderer.textSize(font!!, line, fontSize).height
        }

        platform.renderer.fill(
            x1 = x - radius,
            y1 = y - radius,
            x2 = x + width + radius,
            y2 = y + height + radius,
            color = color,
            radius = radius,
        )

        var yOff = 0f
        for (line in debugReadingLines) {
            platform.renderer.textRenderer.text(
                font = font!!,
                text = line,
                x = x,
                y = y + yOff,
                color = Color.WHITE,
                fontSize = fontSize,
            )

            yOff += platform.renderer.textRenderer.textSize(font!!, line, fontSize).height
        }

        debugReadingHeight = height + radius * 2f
    }

    private fun renderComponentTree(platform: Platform, root: Component<*, *>) {
        val font = this.font ?: return

        val lineHeight = platform.renderer.textRenderer.textSize(font, " ", fontSize).height + 4f
        val totalLines = countComponentTreeLines(root)

        val totalHeight = totalLines * lineHeight
        val backgroundWidth = computeMaxTextWidth(platform, root, font, fontSize, 0) + 16f // add padding
        width = max(width, backgroundWidth)

        // Draw the single background box for the entire tree
        platform.renderer.fill(
            x1 = x - radius,
            y1 = y - radius + paddingBetweenItems + debugReadingHeight,
            x2 = x + width + radius,
            y2 = y + totalHeight + radius + paddingBetweenItems + debugReadingHeight,
            color = color,
            radius = radius,
        )

        // Render each line
        renderComponentSubtree(platform, root, depth = 0, line = 0, lineHeight = lineHeight)
    }

    private fun renderComponentSubtree(
        platform: Platform,
        component: Component<*, *>,
        depth: Int,
        line: Int,
        lineHeight: Float
    ): Int {
        val font = this.font ?: return line

        val componentName = component::class.simpleName ?: "Unknown"
        val text = "$componentName (${component.properties.name})"

        val xOffset = x + depth * 20f
        val yOffset = y + paddingBetweenItems + debugReadingHeight + line * lineHeight

        platform.renderer.textRenderer.text(
            font = font,
            text = text,
            x = xOffset,
            y = yOffset,
            color = Color.WHITE,
            fontSize = fontSize,
        )

        var nextLine = line + 1
        for (child in component.getChildren()) {
            nextLine = renderComponentSubtree(platform, child, depth + 1, nextLine, lineHeight)
        }

        return nextLine
    }

    private fun countComponentTreeLines(component: Component<*, *>): Int {
        var total = 1
        for (child in component.getChildren()) {
            total += countComponentTreeLines(child)
        }
        return total
    }

    private fun computeMaxTextWidth(
        platform: Platform,
        component: Component<*, *>,
        font: Font,
        fontSize: Float,
        depth: Int
    ): Float {
        val componentName = component::class.simpleName ?: "Unknown"
        val text = "$componentName (${component.properties.name})"
        val textWidth = platform.renderer.textRenderer.textSize(font, text, fontSize).width + depth * 20f

        var maxWidth = textWidth
        for (child in component.getChildren()) {
            maxWidth = maxOf(maxWidth, computeMaxTextWidth(platform, child, font, fontSize, depth + 1))
        }

        return maxWidth
    }


    private fun loadFont(): Font {
        val path = "/fonts/Inter-Regular.ttf"
        val stream = Debugger::class.java.getResourceAsStream(path)
            ?: throw IllegalArgumentException("Font $path does not exist.")
        return Font(
            name = "Inter",
            inputStream = stream,
            letterSpacing = 0f,
            lineSpacing = 0f,
            isItalic = false,
            weight = FontWeight.REGULAR
        )
    }

}
