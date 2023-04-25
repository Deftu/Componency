package xyz.deftu.componency.components.impl

import org.lwjgl.opengl.GL11
import xyz.deftu.componency.components.BaseComponent
import xyz.deftu.multi.MultiGlStateManager
import xyz.deftu.multi.MultiMatrixStack
import xyz.deftu.multi.MultiTessellator
import java.awt.Color

class BoxComponent : BaseComponent() {
    companion object {
        @JvmStatic fun renderBox(
            stack: MultiMatrixStack,
            x1: Float,
            y1: Float,
            x2: Float,
            y2: Float,
            color: Color
        ) {
            MultiGlStateManager.enableBlend()
            MultiGlStateManager.blendFuncSeparate(MultiGlStateManager.SrcFactor.SRC_ALPHA, MultiGlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, MultiGlStateManager.SrcFactor.ONE, MultiGlStateManager.DstFactor.ZERO)

            val buffer = MultiTessellator.getFromBuffer()
            buffer.beginWithDefaultShader(MultiTessellator.DrawModes.QUADS, MultiTessellator.VertexFormats.POSITION_COLOR)
            renderBox(buffer, stack, x1, y1, x2, y2, color)

            MultiGlStateManager.disableBlend()
        }

        @JvmStatic fun renderBoxWithActiveShader(
            stack: MultiMatrixStack,
            x1: Float,
            y1: Float,
            x2: Float,
            y2: Float,
            color: Color
        ) {
            val buffer = MultiTessellator.getFromBuffer()
            buffer.beginWithActiveShader(MultiTessellator.DrawModes.QUADS, MultiTessellator.VertexFormats.POSITION_COLOR)
            renderBox(buffer, stack, x1, y1, x2, y2, color)
        }

        @JvmStatic fun renderBox(
            buffer: MultiTessellator,
            stack: MultiMatrixStack,
            x1: Float,
            y1: Float,
            x2: Float,
            y2: Float,
            color: Color
        ) {
            val red = color.red / 255f
            val green = color.green / 255f
            val blue = color.blue / 255f
            val alpha = color.alpha / 255f

            buffer.vertex(stack, x1, y2, 0f).color(red, green, blue, alpha).next()
            buffer.vertex(stack, x2, y2, 0f).color(red, green, blue, alpha).next()
            buffer.vertex(stack, x2, y1, 0f).color(red, green, blue, alpha).next()
            buffer.vertex(stack, x1, y1, 0f).color(red, green, blue, alpha).next()

            MultiGlStateManager.enableDepth()
            MultiGlStateManager.depthFunc(GL11.GL_ALWAYS)
            buffer.draw()
            MultiGlStateManager.disableDepth()
            MultiGlStateManager.depthFunc(GL11.GL_LEQUAL)
        }
    }

    override fun render(
        stack: MultiMatrixStack,
        tickDelta: Float
    ) {
        val color = getColor()
        if (color.alpha == 0) return

        renderBox(stack, getX(), getY(), getRight(), getBottom(), color)
    }
}
