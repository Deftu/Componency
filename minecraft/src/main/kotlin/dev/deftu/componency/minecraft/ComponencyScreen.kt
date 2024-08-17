package dev.deftu.componency.minecraft

import dev.deftu.componency.components.Component
import dev.deftu.componency.components.events.KeyboardModifiers
import dev.deftu.componency.components.impl.FrameComponent
import dev.deftu.componency.dsl.configure
import dev.deftu.componency.dsl.percent
import dev.deftu.omnicore.client.OmniKeyboard
import dev.deftu.omnicore.client.OmniScreen
import dev.deftu.omnicore.client.render.OmniMatrixStack

public abstract class ComponencyScreen : OmniScreen() {

    public val frame: Component = FrameComponent().configure {
        name = "frame"

        properties {
            width = 100.percent
            height = 100.percent
        }
    }.makeRoot(MinecraftEngine)

    override fun handleRender(stack: OmniMatrixStack, mouseX: Int, mouseY: Int, tickDelta: Float) {
        super.handleRender(stack, mouseX, mouseY, tickDelta)
        frame.handleRender()
    }

    override fun handleMouseClick(x: Double, y: Double, button: Int) {
        frame.handleMouseClick(x, y, button)
        super.handleMouseClick(x, y, button)
    }

    override fun handleMouseReleased(x: Double, y: Double, state: Int) {
        frame.handleMouseRelease()
        super.handleMouseReleased(x, y, state)
    }

    override fun handleMouseScrolled(delta: Double) {
        frame.handleMouseScroll(delta.coerceIn(-1.0, 1.0))
        super.handleMouseScrolled(delta)
    }

    override fun handleKeyPress(code: Int, char: Char, modifiers: OmniKeyboard.KeyboardModifiers) {
        frame.handleKeyPress(code, char, KeyboardModifiers(modifiers.shift, modifiers.alt, modifiers.ctrl, false))
        super.handleKeyPress(code, char, modifiers)
    }

}
