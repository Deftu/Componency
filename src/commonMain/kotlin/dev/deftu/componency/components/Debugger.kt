package dev.deftu.componency.components

import dev.deftu.componency.color.Color
import dev.deftu.componency.components.debugging.DebugOverlayBuilder
import dev.deftu.componency.components.debugging.renderDebuggerOverlay
import dev.deftu.componency.components.traits.Focusable
import dev.deftu.componency.font.Font
import dev.deftu.componency.font.FontWeight
import dev.deftu.componency.platform.Platform
import kotlin.math.max
import kotlin.math.roundToInt

public class Debugger(private val component: Component<*, *>) {

    private var isInitialized = false
    private var lastDeltaTime = 0.0
    private var deltaTime = 0.0
    private var frameCount = 0
    private var font: Font? = null

    private var fps = 0
    private var frameTime = 0.0

    private val padding = 5f
    private val fontSize = 16f
    private val x = 15f
    private val y = 15f
    private val radius = 5f
    private val color = Color.GREEN.withAlphaPercentage(0.25f)

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
            this.lastDeltaTime = component.clock.nowSeconds()
        }

        this.font = loadFont()
        this.isInitialized = true
    }

    public fun render() {
        if (!isEnabled || font == null) {
            return
        }

        update()

        val platform = Component.findPlatform(component)
        val overlay = DebugOverlayBuilder().apply {
            section("Performance") {
                property("FPS", fps)
                property("Frame Time", "${frameTime.asFormattedMillis()} ms")
                property("Delta Time", "${deltaTime.asFormattedMillis()} ms")
            }

            section("Component Tree") {
                tree(component) { item ->
                    val name = item::class.simpleName ?: "Unknown"
                    buildString {
                        append(name)

                        item.properties.name?.let { name ->
                            append(" (").append(name).append(")")
                        }

                        if (item.hasTrait(Focusable::class) && item.isFocused) {
                            append(" [Focused]")
                        }
                    }
                }
            }
        }.build()

        renderDebuggerOverlay(platform, overlay, font!!, x, y, fontSize, radius, padding, color)
    }

    private fun update() {
        val currentTime = component.clock.nowSeconds()
        this.deltaTime = currentTime - this.lastDeltaTime
        this.frameCount++

        if (this.deltaTime >= 1.0) {
            this.fps = this.frameCount
            this.frameTime = (this.deltaTime / this.frameCount) * 1_000.0
            this.frameCount = 0
            this.lastDeltaTime = currentTime
        }
    }

    private fun loadFont(): Font {
        val path = "/fonts/Inter-Regular.ttf"
        return component.platform?.fontLoader?.loadFontEmbedded(
            name = "Inter",
            path = path,
            letterSpacing = 0f,
            lineSpacing = 0f,
            isItalic = false,
            weight = FontWeight.REGULAR
        ) ?: throw IllegalStateException("Font loader not available")
    }

    private fun Double.asFormattedMillis(): String {
        val i = (this * 100).roundToInt()
        val whole = i / 100
        val decimal = i % 100
        return "$whole.${decimal.toString().padStart(2, '0')}"
    }

}
