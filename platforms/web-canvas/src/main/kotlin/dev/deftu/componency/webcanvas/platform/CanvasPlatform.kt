package dev.deftu.componency.webcanvas.platform

import dev.deftu.componency.platform.Platform
import dev.deftu.componency.platform.audio.AudioEngine
import dev.deftu.componency.time.Clock
import dev.deftu.componency.time.DefaultClock
import dev.deftu.componency.webcanvas.platform.font.CanvasFontLoader
import dev.deftu.componency.webcanvas.platform.image.CanvasImageLoader
import dev.deftu.componency.webcanvas.platform.input.CanvasInputHandler
import dev.deftu.componency.webcanvas.platform.rendering.CanvasRenderer
import kotlinx.browser.window
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement

public class CanvasPlatform(
    private val element: HTMLCanvasElement,
    private val context: CanvasRenderingContext2D,
    override val clock: Clock = DefaultClock,
) : Platform {

    override val viewportWidth: Float
        get() = context.canvas.width.toFloat()

    override val viewportHeight: Float
        get() = context.canvas.height.toFloat()

    override val pixelRatio: Float
        get() = window.devicePixelRatio.toFloat()

    override val inputHandler: CanvasInputHandler = CanvasInputHandler(element)

    override val renderer: CanvasRenderer = CanvasRenderer(element, context)

    override val fontLoader: CanvasFontLoader = CanvasFontLoader

    override val imageLoader: CanvasImageLoader = CanvasImageLoader

    override val audioEngine: AudioEngine
        get() = TODO("Not yet implemented")

}
