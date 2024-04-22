package dev.deftu.componency.components.impl

import dev.deftu.componency.components.Component
import dev.deftu.omnicore.client.OmniClient
import dev.deftu.omnicore.client.render.OmniMatrixStack
import dev.deftu.omnicore.client.render.OmniRenderState
import dev.deftu.omnicore.client.render.OmniTessellator
import dev.deftu.stateful.SimpleState
import dev.deftu.stateful.State
import java.awt.Color

public class TestComponent : Component() {
    public val allowedToRedraw: State<Boolean> = SimpleState(false)
    private var fps = 0

    override fun preRender(stack: OmniMatrixStack, tickDelta: Float) {
        fps = OmniClient.getInstance().currentFps
        if (allowedToRedraw.get()) {
            requestRedraw()
        }

        val textColor = Color.GREEN.withAlpha(255 / 2)
        OmniClient.getFontRenderer().draw(stack.toVanillaStack(), "Hello, world! [REDRAW ALLOWED: ${allowedToRedraw.get()}] ($fps)", 65f, 5f, textColor.rgb)

        val color = Color.WHITE.withAlpha(255 / 2)
        drawSquare(stack, 0f, 0f, 100f, 100f, color)

        val color2 = Color.RED.withAlpha(255 / 2)
        drawSquare(stack, 50f, 50f, 100f, 100f, color2)

        val color3 = color2
        drawSquare(stack, 100f, 100f, 100f, 100f, color3)

        OmniClient.getFontRenderer().draw(stack.toVanillaStack(), "Hello, world! [REDRAW ALLOWED: ${allowedToRedraw.get()}] ($fps)", 65f, 30f, textColor.rgb)
    }

    private fun drawSquare(stack: OmniMatrixStack, x: Float, y: Float, width: Float, height: Float, color: Color) {
        OmniRenderState.enableBlend()
        OmniRenderState.blendFuncSeparate(OmniRenderState.SrcFactor.SRC_ALPHA, OmniRenderState.DstFactor.ONE_MINUS_SRC_ALPHA, OmniRenderState.SrcFactor.ONE, OmniRenderState.DstFactor.ZERO)

        val tessellator = OmniTessellator.getFromBuffer()
        tessellator.beginWithDefaultShader(OmniTessellator.DrawModes.QUADS, OmniTessellator.VertexFormats.POSITION_COLOR)
        tessellator.vertex(stack, x, y, 0f)
            .color(color)
            .next()
        tessellator.vertex(stack, x, y + height, 0f)
            .color(color)
            .next()
        tessellator.vertex(stack, x + width, y + height, 0f)
            .color(color)
            .next()
        tessellator.vertex(stack, x + width, y, 0f)
            .color(color)
            .next()
        tessellator.draw()

        OmniRenderState.disableBlend()
    }

    private fun Color.withAlpha(alpha: Int): Color {
        return Color(red, green, blue, alpha)
    }
}
