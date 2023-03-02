package xyz.deftu.componency.font.impl

import net.minecraft.client.font.TextRenderer
import xyz.deftu.componency.components.BaseComponent
import xyz.deftu.componency.font.FontProvider
import xyz.deftu.componency.utils.roundToScaledPixels
import xyz.deftu.multi.MultiClient
import xyz.deftu.multi.MultiMatrixStack
import xyz.deftu.text.Text
import xyz.deftu.text.utils.toVanilla
import java.awt.Color

object VanillaFontProvider : FontProvider {
    override var cached: FontProvider = this
    override var recalculate = false
    override var attachedTo: BaseComponent? = null

    private val textRenderer: TextRenderer
        get() = MultiClient.getFontRenderer()

    override fun getWidth(text: Text, shadow: Boolean) =
        textRenderer.getWidth(text.toVanilla()).toFloat().apply {
            if (!shadow) return this - 2f
        }

    override fun getHeight(text: Text, shadow: Boolean) =
        textRenderer.fontHeight.toFloat().apply {
            if (!shadow) return this - 2f
        }

    override fun getLineHeight(shadow: Boolean) =
        textRenderer.fontHeight.toFloat().apply {
            if (!shadow) return this - 2f
        }

    override fun render(
        stack: MultiMatrixStack,
        text: Text,
        x: Float,
        y: Float,
        scale: Float,
        shadow: Boolean,
        color: Color
    ) {
        val scaledX = x.roundToScaledPixels() / scale
        val scaledY = y.roundToScaledPixels() / scale

        stack.scale(scale, scale, 1f)
        renderVanillaText(stack, text, scaledX, scaledY, shadow, color)
        stack.scale(1f / scale, 1f / scale, 1f)
    }

    private fun renderVanillaText(
        stack: MultiMatrixStack,
        text: Text,
        x: Float,
        y: Float,
        shadow: Boolean,
        color: Color
    ) {
        val vanillaText = text.toVanilla()
        if (vanillaText.string.isEmpty()) return

        if (color.alpha < 10) return

        //#if MC>=11502
        if (shadow) {
            textRenderer.drawWithShadow(
                stack.toVanillaStack(),
                vanillaText,
                x,
                y,
                color.rgb
            )
        } else {
            textRenderer.draw(
                stack.toVanillaStack(),
                vanillaText,
                x,
                y,
                color.rgb
            )
        }
        //#else
        //$$ textRenderer.drawString(
        //$$     text.asFormattedString(),
        //$$     x,
        //$$     y,
        //$$     color.rgb,
        //$$     shadow
        //$$ )
        //#endif
    }
}
