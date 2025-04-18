package dev.deftu.componency.lwjgl3.engine

import dev.deftu.componency.platform.input.InputHandler

class Lwjgl3InputHandler(private val handle: Long) : InputHandler {

    override val pointerInput = Lwjgl3PointerInput(handle)

    override val keyboardInput = Lwjgl3KeyboardInput(handle)

    override var clipboard = Lwjgl3ClipboardAccess(handle)

}
