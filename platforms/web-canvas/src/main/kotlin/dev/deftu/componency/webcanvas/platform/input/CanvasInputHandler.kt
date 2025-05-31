package dev.deftu.componency.webcanvas.platform.input

import dev.deftu.componency.platform.input.InputHandler
import org.w3c.dom.HTMLCanvasElement

public class CanvasInputHandler(element: HTMLCanvasElement) : InputHandler {

    override val pointerInput: CanvasPointerInput = CanvasPointerInput(element)

    override val keyboardInput: CanvasKeyboardInput = CanvasKeyboardInput()

    override val clipboard: CanvasClipboardAccess = CanvasClipboardAccess()

}
