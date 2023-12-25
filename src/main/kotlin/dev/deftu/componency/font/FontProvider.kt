package dev.deftu.componency.font

import dev.deftu.componency.constraints.Constraint
import dev.deftu.multi.MultiMatrixStack
import dev.deftu.textful.Text
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
