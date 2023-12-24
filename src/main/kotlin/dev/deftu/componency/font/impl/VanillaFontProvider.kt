package dev.deftu.componency.font.impl

//#if MC >= 1.20
//$$ import net.minecraft.client.render.Tessellator
//$$ import net.minecraft.client.render.VertexConsumerProvider
//#endif

import net.minecraft.client.font.TextRenderer
import dev.deftu.componency.components.BaseComponent
import dev.deftu.componency.font.FontProvider
import dev.deftu.componency.utils.roundToScaledPixels
import dev.deftu.multi.MultiClient
import dev.deftu.multi.MultiMatrixStack
import dev.deftu.text.Text
import dev.deftu.text.TextFactory
import dev.deftu.text.utils.toVanilla
import java.awt.Color

object VanillaFontProvider : FontProvider {
    override var cached: FontProvider = this
    override var recalculate = false
    override var attachedTo: BaseComponent? = null

    private val textRenderer: TextRenderer
        get() = MultiClient.getFontRenderer()

    override fun getWidth(text: Text, shadow: Boolean) =
        //#if MC>=11600
        textRenderer.getWidth(text.toVanilla()).toFloat().apply {
        //#else
        //$$ textRenderer.getStringWidth(text.asFormattedString()).toFloat().apply {
        //#endif
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
        val string = text.asString()
        val lines = string.split("\n")

        stack.scale(scale, scale, 1f)
        if (lines.size == 1) {
            draw(stack, text, scaledX, scaledY, color, shadow)
            return
        }

        var currentY = scaledY
        for (line in lines) {
            draw(stack, TextFactory.INSTANCE.create(line), scaledX, currentY, color, shadow)
            currentY += getLineHeight(shadow)
        }

        stack.scale(1f / scale, 1f / scale, 1f)
    }

    private fun draw(
        stack: MultiMatrixStack,
        text: Text,
        x: Float,
        y: Float,
        color: Color,
        shadow: Boolean
    ) {
        val vanillaText = text.toVanilla()
        //#if MC >= 1.15
        if (vanillaText.string.isEmpty()) return
        //#else
        //$$ if (vanillaText.unformattedText.isEmpty()) return
        //#endif

        if (color.alpha < 10) return

        //#if MC >= 1.20
        //$$ val vertexConsumerProvider = VertexConsumerProvider.immediate(Tessellator.getInstance().buffer)
        //$$ textRenderer.draw(
        //$$     vanillaText,
        //$$     x,
        //$$     y,
        //$$     color.rgb,
        //$$     shadow,
        //$$     stack.peek().matrix,
        //$$     vertexConsumerProvider,
        //$$     TextRenderer.TextLayerType.NORMAL,
        //$$     0xFFFFFF,
        //$$     0xFFFFFF
        //$$ )
        //#elseif MC >= 1.16
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
        //#elseif MC >= 1.15
        //$$ if (shadow) {
        //$$     textRenderer.drawWithShadow(
        //$$         text.asFormattedString(),
        //$$         x,
        //$$         y,
        //$$         color.rgb
        //$$     )
        //$$ } else {
        //$$     textRenderer.draw(
        //$$         text.asFormattedString(),
        //$$         x,
        //$$         y,
        //$$         color.rgb
        //$$     )
        //$$ }
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
