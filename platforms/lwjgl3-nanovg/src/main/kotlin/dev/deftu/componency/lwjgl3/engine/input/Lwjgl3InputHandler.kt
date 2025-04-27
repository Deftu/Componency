package dev.deftu.componency.lwjgl3.engine.input

import dev.deftu.componency.platform.input.InputHandler

class Lwjgl3InputHandler(handle: Long) : InputHandler {

    override val pointerInput = Lwjgl3PointerInput(handle)

    override val keyboardInput = Lwjgl3KeyboardInput(handle)

    override var clipboard = Lwjgl3ClipboardAccess(handle)

}
