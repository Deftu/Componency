package xyz.deftu.componency.components.impl

import xyz.deftu.componency.components.BaseComponent
import xyz.deftu.multi.MultiMatrixStack
import xyz.deftu.multi.shader.MultiShader
import xyz.deftu.multi.shader.BlendState
import xyz.deftu.multi.shader.Vec4Uniform
import xyz.deftu.multi.shader.VecUniform
import java.awt.Color

class RoundedBoxComponent : BaseComponent() {
    companion object {
        private lateinit var shader: MultiShader
        private lateinit var radiusUniform: VecUniform
        private lateinit var innerRectUniform: Vec4Uniform
        private val initialized: Boolean
            get() = ::shader.isInitialized

        @JvmStatic fun initialize() {
            if (initialized) return

            shader = MultiShader.readFromResource("rounded_box", "rounded_box", BlendState.NORMAL)
            if (!shader.usable) error("Failed to load rounded box shader")

            radiusUniform = shader.getVecUniform("u_Radius")
            innerRectUniform = shader.getVec4Uniform("u_InnerRect")
        }

        @JvmStatic fun renderRoundedBox(
            stack: MultiMatrixStack,
            x1: Float,
            y1: Float,
            x2: Float,
            y2: Float,
            radius: Float,
            color: Color
        ) {
            if (!initialized) return

            shader.bind()
            radiusUniform.setValue(radius)
            innerRectUniform.setValue(x1 + radius, y1 + radius, x2 - radius, y2 - radius)
            BoxComponent.renderBoxWithActiveShader(stack, x1, y1, x2, y2, color)
            shader.unbind()
        }
    }

    override fun render(stack: MultiMatrixStack, tickDelta: Float) {
        val color = getColor()
        if (color.alpha == 0) return

        val radius = getRadius()
        renderRoundedBox(stack, getX(), getY(), getRight(), getBottom(), radius, color)
    }
}
