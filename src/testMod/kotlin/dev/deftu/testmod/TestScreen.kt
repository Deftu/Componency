package dev.deftu.testmod

import dev.deftu.componency.ComponencyScreen
import dev.deftu.componency.components.impl.TestComponent
import dev.deftu.componency.utils.attachTo
import dev.deftu.omnicore.client.MultiKeyboard
import java.io.File

class TestScreen : ComponencyScreen() {
    val test = TestComponent().attachTo(window)

    override fun handleKeyPress(code: Int, char: Char, modifiers: MultiKeyboard.KeyboardModifiers) {
        when (code) {
            MultiKeyboard.KEY_J -> {
                window.writeFramebufferDebug(File("fbo"))
            }

            MultiKeyboard.KEY_K -> {
                test.requestRedraw()
                test.allowedToRedraw.set(!test.allowedToRedraw.get())
            }
        }

        super.handleKeyPress(code, char, modifiers)
    }

    override fun supportsRedraw() = true
    override fun doesPauseGame() = false
}
