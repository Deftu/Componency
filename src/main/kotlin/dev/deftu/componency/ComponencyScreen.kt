package dev.deftu.componency

import dev.deftu.componency.components.WindowComponent
import dev.deftu.multi.MultiKeyboard
import dev.deftu.multi.MultiMatrixStack
import dev.deftu.multi.MultiMouse
import dev.deftu.multi.MultiScreen
import kotlin.math.floor

abstract class ComponencyScreen @JvmOverloads constructor(
    restorePreviousScreen: Boolean = true,
    titleKey: String? = null
) : MultiScreen(
    restorePreviousScreen,
    titleKey
) {
    val window by lazy(::createWindow)
    private var isInitialized = false

    init {
        window.addKeyPressListener { event ->
            processKeyEvent(event.keyCode, event.typedChar)
        }
    }

    open fun handleInitialize() {
    }

    override fun handleInitialize(width: Int, height: Int) {
        window.onWindowResize()
        super.handleInitialize(width, height)
    }

    override fun handleRender(stack: MultiMatrixStack, mouseX: Int, mouseY: Int, tickDelta: Float) {
        if (!isInitialized) {
            isInitialized = true
            handleInitialize()
        }

        super.handleRender(stack, mouseX, mouseY, tickDelta)
        window.doRender(stack, tickDelta)
    }

    override fun handleKeyPress(code: Int, char: Char, modifiers: MultiKeyboard.KeyboardModifiers) {
        window.keyPress(code, char, modifiers)
    }

    override fun handleMouseClick(x: Double, y: Double, button: Int) {
        super.handleMouseClick(x, y, button)

        val (preciseMouseX, preciseMouseY) =
            if (x == floor(x) && y == floor(y)) {
                val scaledX = MultiMouse.scaledX
                val scaledY = MultiMouse.scaledY

                x + (scaledX - floor(scaledX)) to y + (scaledY - floor(scaledY))
            } else {
                x to y
            }
        window.mouseClick(preciseMouseX, preciseMouseY, button)
    }

    override fun handleMouseReleased(x: Double, y: Double, state: Int) {
        super.handleMouseReleased(x, y, state)
        window.mouseRelease(x, y, state)
    }

    override fun handleMouseScrolled(delta: Double) {
        super.handleMouseScrolled(delta)
        window.mouseScroll(MultiMouse.scaledX, MultiMouse.scaledY, delta.coerceIn(-1.0, 1.0))
    }

    override fun handleClose() {
        super.handleClose()
        window.destroy()
    }

    open fun createWindow() = WindowComponent()

    private fun processKeyEvent(keyCode: Int, typedChar: Char) {
        super.handleKeyPress(keyCode, typedChar, MultiKeyboard.getModifiers())
    }
}
