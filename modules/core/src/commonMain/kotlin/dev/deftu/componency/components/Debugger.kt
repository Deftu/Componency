package dev.deftu.componency.components

import dev.deftu.componency.components.debugging.DebugOverlayBuilder
import dev.deftu.componency.components.debugging.renderDebuggerOverlay
import dev.deftu.componency.components.traits.Focusable
import dev.deftu.componency.font.Font
import dev.deftu.componency.font.FontWeight
import dev.deftu.componency.input.MouseButton
import dev.deftu.componency.platform.Platform
import dev.deftu.kolor.NamedColors
import kotlin.math.roundToInt

public class Debugger(private val component: Component<*, *>) {

    private var isInitialized = false
    private var lastDeltaTime = 0.0
    private var deltaTime = 0.0
    private var frameCount = 0
    internal var font: Font? = null

    private var fps = 0
    private var frameTime = 0.0

    private val padding = 5f
    internal var fontSize = 16f
    private val x = 15f
    private val y = 15f
    private val radius = 5f
    private val color = NamedColors.GREEN.withAlphaPercentage(0.25f)

    private var isHovered = false
    private var currentlyHoveredComponent: Component<*, *>? = null
    private var currentlySelectedComponent: Component<*, *>? = null

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

        if (this.font == null) {
            this.font = loadFont()
        }

        println("Debugger initialized for component: ${component::class.simpleName}. Font loaded: ${font != null} ($font)")
        this.isInitialized = true
    }

    public fun render() {
        if (!isEnabled) {
            return
        }

        update()

        val platform = Component.findPlatform(component)
        renderOverlay(platform)
        renderComponentOutlines(platform)
    }

    public fun handlePointerClick(button: MouseButton) {
        if (!isEnabled || !isHovered || this.currentlyHoveredComponent == null || button != MouseButton.LEFT) {
            return
        }

        this.currentlySelectedComponent = if (this.currentlySelectedComponent == this.currentlyHoveredComponent) null else this.currentlyHoveredComponent
        this.currentlyHoveredComponent = null
    }

    private fun renderOverlay(platform: Platform) {
        if (this.font == null) {
            return
        }

        val overlay = DebugOverlayBuilder {
            section("Performance") {
                property("FPS", fps)
                property("Frame Time", "${frameTime.asFormattedMillis()} ms")
                property("Delta Time", "${deltaTime.asFormattedMillis()} ms")
            }

            section("Component Tree") {
                componentTree(component) { item ->
                    val name = item::class.simpleName ?: "Unknown"
                    buildString {
                        append(name)

                        item.properties.name?.let { append(" ($it)") }

                        if (item.hasTrait(Focusable::class) && item.isFocused) {
                            append(" [Focused]")
                        }

                        val lastRenderTime = item.lastRenderTime
                        if (lastRenderTime > 100_000L) { // 0.1ms threshold
                            append(" (${lastRenderTime}ns)")
                        }
                    }
                }
            }

            val currentlySelectedComponent = this@Debugger.currentlySelectedComponent
            if (currentlySelectedComponent != null) {
                section("Selected Component") {
                    for ((name, getter) in currentlySelectedComponent.properties.debuggableProperties) {
                        property(name, getter.invoke())
                    }
                }
            }
        }.build()

        renderDebuggerOverlay(
            platform = platform,
            blocks = overlay,
            font = font!!,
            x = x,
            y = y,
            fontSize = fontSize,
            radius = radius,
            padding = padding,
            backgroundColor = color,
            onHoverState = { isHovered ->
                this.isHovered = isHovered
            },
            onTreeNodeHover = { hoveredComponent, _, _, _, _ ->
                this.currentlyHoveredComponent = hoveredComponent
            }
        )
    }

    /**
     * Draws a box around every component in the tree to visualize their bounds.
     */
    private fun renderComponentOutlines(platform: Platform) {
        if (!isEnabled) {
            return
        }

        val lineWidth = 1f

        fun renderOutline(component: Component<*, *>) {
            val x = component.x
            val y = component.y
            val w = component.width
            val h = component.height
            val color = NamedColors.DIMGRAY.withAlphaPercentage(0.95f)

            // Top edge
            platform.renderer.fill(
                x1 = x - lineWidth,
                y1 = y - lineWidth,
                x2 = x + w + lineWidth,
                y2 = y,
                color = color
            )

            // Bottom edge
            platform.renderer.fill(
                x1 = x - lineWidth,
                y1 = y + h,
                x2 = x + w + lineWidth,
                y2 = y + h + lineWidth,
                color = color
            )

            // Left edge
            platform.renderer.fill(
                x1 = x - lineWidth,
                y1 = y,
                x2 = x,
                y2 = y + h,
                color = color
            )

            // Right edge
            platform.renderer.fill(
                x1 = x + w,
                y1 = y,
                x2 = x + w + lineWidth,
                y2 = y + h,
                color = color
            )

            for (child in component.getChildren()) {
                renderOutline(child)
            }
        }

        renderOutline(component)
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

    private fun loadFont(): Font? {
        val path = "/font/inter_regular.ttf"
        return component.platform?.fontLoader?.loadFontEmbedded(
            name = "Inter",
            path = path,
            letterSpacing = 0.2f,
            lineSpacing = 1.4f,
            isItalic = false,
            weight = FontWeight.REGULAR
        )
    }

    private fun Double.asFormattedMillis(): String {
        val i = (this * 100).roundToInt()
        val whole = i / 100
        val decimal = i % 100
        return "$whole.${decimal.toString().padStart(2, '0')}"
    }

}
