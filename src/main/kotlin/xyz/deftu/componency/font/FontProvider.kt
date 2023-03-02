package xyz.deftu.componency.font

import xyz.deftu.componency.constraints.Constraint
import xyz.deftu.multi.MultiMatrixStack
import xyz.deftu.text.Text
import java.awt.Color

interface FontProvider : Constraint<FontProvider> {
    fun setAnimating(animating: Boolean) {
    }

    fun getWidth(
        text: Text,
        shadow: Boolean
    ): Float

    fun getHeight(
        text: Text,
        shadow: Boolean
    ): Float

    fun getLineHeight(
        shadow: Boolean
    ): Float

    fun getLineHeight() = getLineHeight(true)

    fun render(
        stack: MultiMatrixStack,
        text: Text,
        x: Float,
        y: Float,
        scale: Float,
        shadow: Boolean,
        color: Color
    )
}
