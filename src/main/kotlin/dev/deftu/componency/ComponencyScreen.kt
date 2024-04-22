package dev.deftu.componency

import dev.deftu.componency.components.impl.WindowComponent
import dev.deftu.omnicore.client.OmniScreen
import dev.deftu.omnicore.client.render.OmniMatrixStack
import dev.deftu.omnicore.client.render.OmniResolution

public abstract class ComponencyScreen : OmniScreen() {
    public val window: WindowComponent = WindowComponent()

    public open fun supportsRedraw(): Boolean = true

    override fun handleInitialize(width: Int, height: Int) {
        window.resize(OmniResolution.viewportWidth, OmniResolution.viewportHeight)
        super.handleInitialize(width, height)
    }

    override fun handleResize(width: Int, height: Int) {
        window.resize(OmniResolution.viewportWidth, OmniResolution.viewportHeight)
        super.handleResize(width, height)
    }

    override fun handleClose() {
        window.destroy()
        super.handleClose()
    }

    override fun handleRender(stack: OmniMatrixStack, mouseX: Int, mouseY: Int, tickDelta: Float) {
        window.supportsRedraw = supportsRedraw()
        window.handleRender(stack, tickDelta)
        super.handleRender(stack, mouseX, mouseY, tickDelta)
    }
}
