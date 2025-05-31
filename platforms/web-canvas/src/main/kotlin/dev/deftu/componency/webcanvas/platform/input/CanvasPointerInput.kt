package dev.deftu.componency.webcanvas.platform.input

import dev.deftu.componency.input.MouseButton
import dev.deftu.componency.platform.input.PointerInput
import dev.deftu.componency.platform.input.PointerState
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.TouchEvent
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.get

public class CanvasPointerInput(private val element: HTMLCanvasElement) : PointerInput {

    // Track mouse pointer separately
    private var mouseButtons = mutableSetOf<MouseButton>()
    private var mouseX: Float = 0f
    private var mouseY: Float = 0f

    // Track touches by identifier
    private val touches = mutableMapOf<Int, PointerState>()

    init {
        // Mouse events
        element.addEventListener("mousedown", { event ->
            event as MouseEvent
            mouseButtons.add(when (event.button.toInt()) {
                0 -> MouseButton.LEFT
                1 -> MouseButton.MIDDLE
                2 -> MouseButton.RIGHT
                else -> MouseButton.UNKNOWN // Handle unknown buttons gracefully
            })

            println("Mouse down: ${event.button}, buttons: $mouseButtons")
            updateMousePosition(event)
        })

        element.addEventListener("mouseup", { event ->
            event as MouseEvent
            mouseButtons.remove(when (event.button.toInt()) {
                0 -> MouseButton.LEFT
                1 -> MouseButton.MIDDLE
                2 -> MouseButton.RIGHT
                else -> MouseButton.UNKNOWN // Handle unknown buttons gracefully
            })

            updateMousePosition(event)
        })

        element.addEventListener("mousemove", { event ->
            event as MouseEvent
            updateMousePosition(event)
        })

        // Touch events
        element.addEventListener("touchstart", { event ->
            event as TouchEvent
            updateTouches(event)
        })

        element.addEventListener("touchmove", { event ->
            event as TouchEvent
            updateTouches(event)
        })

        element.addEventListener("touchend", { event ->
            event as TouchEvent
            removeTouches(event)
        })

        element.addEventListener("touchcancel", { event ->
            event as TouchEvent
            removeTouches(event)
        })
    }

    private fun updateMousePosition(event: MouseEvent) {
        val rect = element.getBoundingClientRect()
        mouseX = ((event.clientX - rect.left) / rect.width * element.width).toFloat()
        mouseY = ((event.clientY - rect.top) / rect.height * element.height).toFloat()
    }

    private fun updateTouches(event: TouchEvent) {
        val rect = element.getBoundingClientRect()
        for (i in 0 until event.touches.length) {
            val touch = event.touches[i]!!
            val x = ((touch.clientX - rect.left) / rect.width * element.width).toFloat()
            val y = ((touch.clientY - rect.top) / rect.height * element.height).toFloat()
            touches[touch.identifier] = PointerState(i, x, y, setOf(MouseButton.LEFT))
        }

        event.preventDefault() // prevent scrolling or other default behavior on touch
    }

    private fun removeTouches(event: TouchEvent) {
        for (i in 0 until event.changedTouches.length) {
            val touch = event.changedTouches[i]!!
            touches.remove(touch.identifier)
        }

        event.preventDefault()
    }

    override val pointerCount: Int
        get() = if (touches.isNotEmpty()) {
            touches.size
        } else if (mouseButtons.isNotEmpty()) {
            1
        } else {
            0
        }

    override fun getPointer(index: Int): PointerState {
        if (touches.isNotEmpty()) {
            require(index in 0 until touches.size) { "Pointer index $index out of bounds for touch pointers" }
            return touches.values.elementAt(index)
        }

        require(index == 0) { "Only one pointer (mouse) is supported when no touches are active" }
        return PointerState(0, mouseX, mouseY, mouseButtons)
    }

}
