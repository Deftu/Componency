package xyz.deftu.componency

import xyz.deftu.componency.components.WindowComponent
import xyz.deftu.multi.MultiKeyboard
import xyz.deftu.multi.MultiMatrixStack
import xyz.deftu.multi.MultiMouse
import xyz.deftu.multi.MultiScreen

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
        window.onKeyPress {
            processKeyEvent(keyCode, typedChar)
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

    override fun handleKeyPress(code: Int, char: Char, modifiers: MultiKeyboard.KeyboardModifiers): Boolean {
        val defaultValue = super.handleKeyPress(code, char, modifiers)
        return defaultValue || window.keyPress(code, char, modifiers)
    }

    override fun handleMouseClick(x: Double, y: Double, button: Int): Boolean {
        val defaultValue = super.handleMouseClick(x, y, button)
        return (defaultValue.also {
            println("defaultValue: $defaultValue")
        } || window.mouseClick(x, y, button).also {
            println("window.mouseClick: $it")
        }).also {
            println("result: $it")
        }
    }

    override fun handleMouseReleased(x: Double, y: Double, state: Int): Boolean {
        val defaultValue = super.handleMouseReleased(x, y, state)
        return defaultValue || window.mouseRelease(x, y, state)
    }

    override fun handleMouseScrolled(delta: Double): Boolean {
        val defaultValue = super.handleMouseScrolled(delta)
        return defaultValue || window.mouseScroll(MultiMouse.scaledX, MultiMouse.scaledY, delta.coerceIn(-1.0, 1.0))
    }

    override fun handleClose() {
        super.handleClose()
        window.destroy()
    }

    open fun createWindow() = WindowComponent()

    private fun processKeyEvent(keyCode: Int, typedChar: Char): Boolean {
        return super.handleKeyPress(keyCode, typedChar, MultiKeyboard.getModifiers())
    }
}
