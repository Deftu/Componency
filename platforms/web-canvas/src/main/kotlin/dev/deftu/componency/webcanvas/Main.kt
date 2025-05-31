package dev.deftu.componency.webcanvas

import dev.deftu.componency.components.impl.Frame
import dev.deftu.componency.components.impl.FrameComponent
import dev.deftu.componency.components.impl.Rectangle
import dev.deftu.componency.components.impl.Text
import dev.deftu.componency.dsl.*
import dev.deftu.componency.easings.Easings
import dev.deftu.componency.font.Font
import dev.deftu.componency.font.FontWeight
import dev.deftu.componency.platform.Platform
import dev.deftu.componency.webcanvas.platform.CanvasPlatform
import dev.deftu.kolor.NamedColors
import dev.deftu.stateful.utils.mappedStateOf
import dev.deftu.stateful.utils.mutableStateOf
import dev.deftu.textile.SimpleTextHolder
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.pointerevents.PointerEvent

public fun main() {
    injectCss("""
        html, body {
            margin: 0;
            padding: 0;
            overflow: hidden;
            
            /* Dark background for better contrast */
            background-color: #121212;
        }
    """.trimIndent())

    val canvas = setupCanvas()
    canvas.width = window.innerWidth
    canvas.height = window.innerHeight

    val platform = CanvasPlatform(canvas, canvas.getContext("2d") as CanvasRenderingContext2D)
    val font = platform.fontLoader.loadFont(
        name = "sans-serif",
        path = "sans-serif",
        letterSpacing = 0f,
        lineSpacing = 0f,
        isItalic = false,
        weight = FontWeight.REGULAR,
        family = null
    ) as Font
    println("Font loaded: ${font.name}, weight: ${font.weight}")

    val frame = ui(platform, font)

    // Render loop
    fun render() {
        frame.handleRender()
        window.requestAnimationFrame { render() }
    }

    render()

    // Recalculate on resize
    window.addEventListener("resize", { event ->
        canvas.width = window.innerWidth
        canvas.height = window.innerHeight
        frame.recalculate()
    })

    /// Inputs
    // Pointer down
    canvas.addEventListener("pointerdown", { event ->
        event as PointerEvent
        frame.handlePointerClick(event.clientX.toDouble(), event.clientY.toDouble(), when (event.button.toInt()) {
            0 -> dev.deftu.componency.input.MouseButton.LEFT
            1 -> dev.deftu.componency.input.MouseButton.MIDDLE
            2 -> dev.deftu.componency.input.MouseButton.RIGHT
            else -> dev.deftu.componency.input.MouseButton.UNKNOWN // Handle unknown buttons gracefully
        })
    })

    // Pointer up
    canvas.addEventListener("pointerup", { event ->
        event as PointerEvent
        frame.handlePointerRelease()
    })
}

private fun ui(platform: Platform, font: Font): FrameComponent {
    return Frame("window") {
        root(platform)
        debugger(font)
        size(100.percent, 100.percent)

        Rectangle("button") {
            size(100.px, 20.px)
            position(centered, 10.px)
            fill = NamedColors.GREEN.asProperty

            onPointerClick {
                println("Button clicked!")
            }

            Text("button_text") {
                position(centered, centered)
                fill = NamedColors.BLUE.asProperty
                this.font = font
                this.fontSize = 16.px
                text = SimpleTextHolder("Click Me")
            }
        }

        Rectangle("animating_box") {
            size(100.px, 100.px)
            position(centered, centered)
            fill = NamedColors.RED.asProperty

            val state = mutableStateOf(false)

            onPointerClick {
                state.set { !it }
                val color = mappedStateOf(state) { if (it) NamedColors.RED else NamedColors.GREEN }
                fill.animateTo(Easings.IN_OUT_QUAD, 500.millis, color.asProperty)
            }

            onHover {
                width.animateTo(Easings.IN_OUT_QUAD, 500.millis, 200.px)
            }

            onUnhover {
                width.animateTo(Easings.IN_OUT_QUAD, 500.millis, 100.px)
            }
        }
    }
}

private fun setupCanvas(): HTMLCanvasElement {
    val canvas = document.getElementById("componency-canvas") as? HTMLCanvasElement
        ?: error("No canvas with id 'componency-canvas' found")

    canvas.width = 800
    canvas.height = 600

    return canvas
}

private fun injectCss(rule: String) {
    val style = document.createElement("style")
    style.textContent = rule
    document.head?.appendChild(style)
}
