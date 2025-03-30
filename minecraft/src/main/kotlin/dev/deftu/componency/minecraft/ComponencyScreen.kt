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

    override fun handleMouseClick(x: Double, y: Double, button: Int): Boolean {
        frame.handleMouseClick(x, y, MinecraftInputEngine.getMouseButtonMapping(button))
        return super.handleMouseClick(x, y, button)
    }

    override fun handleMouseReleased(x: Double, y: Double, state: Int): Boolean {
        frame.handleMouseRelease()
        return super.handleMouseReleased(x, y, state)
    }

    override fun handleMouseScrolled(delta: Double): Boolean {
        frame.handleMouseScroll(delta.coerceIn(-1.0, 1.0))
        return super.handleMouseScrolled(delta)
    }

    public override fun handleKeyPress(
        keyCode: Int,
        scancode: Int,
        typedChar: Char,
        modifiers: OmniKeyboard.KeyboardModifiers,
        trigger: KeyPressTrigger
    ): Boolean {
        frame.handleKeyPress(MinecraftInputEngine.getKeyMapping(keyCode), KeyboardModifiers(modifiers.shift, modifiers.alt, modifiers.ctrl, false))
        return super.handleKeyPress(keyCode, scancode, typedChar, modifiers, trigger)
    }

    public override fun handleKeyRelease(
        keyCode: Int,
        scancode: Int,
        modifiers: OmniKeyboard.KeyboardModifiers
    ): Boolean {
        frame.handleKeyRelease(MinecraftInputEngine.getKeyMapping(keyCode), KeyboardModifiers(modifiers.shift, modifiers.ctrl, modifiers.alt, false))
        return super.handleKeyRelease(keyCode, scancode, modifiers)
    }

}
