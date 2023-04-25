package xyz.deftu.componency.effects

import xyz.deftu.componency.components.BaseComponent
import xyz.deftu.multi.MultiFramebuffer
import xyz.deftu.multi.MultiMatrixStack
import xyz.deftu.multi.MultiResolution
import xyz.deftu.multi.MultiTessellator
import xyz.deftu.multi.shader.BlendState
import xyz.deftu.multi.shader.MultiShader
import xyz.deftu.multi.shader.Vec4Uniform
import xyz.deftu.multi.shader.VecUniform
import java.awt.Color

class OutlineEffect(
    val color: Color,
    val thickness: Float = 1f,
) : Effect {
    companion object {
        private val tessellator = MultiTessellator.getFromSize(2097152)
        private lateinit var framebuffer: MultiFramebuffer
        private lateinit var shader: MultiShader
        private lateinit var thicknessUniform: VecUniform
        private lateinit var colorUniform: Vec4Uniform
        private val initialized: Boolean
            get() = ::shader.isInitialized

        @JvmStatic fun initialize() {
            if (initialized) return

            println("Initializing outline effect")
            framebuffer = MultiFramebuffer()
            shader = MultiShader.readFromResource("componency_outline", "outline", "outline", BlendState.NORMAL)
            if (!shader.usable) error("Failed to load outline shader")

            //thicknessUniform = shader.getVecUniform("u_Thickness")
            //colorUniform = shader.getVec4Uniform("u_Color")
        }

        @JvmStatic fun resize() {
            if (!initialized) return
            framebuffer.resize(MultiResolution.viewportWidth, MultiResolution.viewportHeight)
        }
    }

    // TODO

    // We need to render an outline around the component
    // Not just a simple rectangle around it's mathematically calculated bounds
    // But a shader used to align to any of its curves or inner edges
    //
    // We need to render the component to a framebuffer
    // Then render the outline to framebuffer
    // Then render the texture of the framebuffer to the screen

    override fun applyPreRender(component: BaseComponent, stack: MultiMatrixStack, tickDelta: Float) {
        if (!initialized) return

        framebuffer.bind(true)
        shader.bind()

        // thicknessUniform.setValue(thickness)
        // colorUniform.setValue(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)

        tessellator.beginWithActiveShader(MultiTessellator.DrawModes.QUADS, MultiTessellator.VertexFormats.POSITION_COLOR)
        val x1 = component.getX()
        val y1 = component.getY()
        val x2 = component.getRight()
        val y2 = component.getBottom()
        tessellator.vertex(stack, x1, y1, 0f).color(0f, 0f, 0f, 1f).next()
        tessellator.vertex(stack, x2, y1, 0f).color(0f, 0f, 0f, 1f).next()
        tessellator.vertex(stack, x2, y2, 0f).color(0f, 0f, 0f, 1f).next()
        tessellator.vertex(stack, x1, y2, 0f).color(0f, 0f, 0f, 1f).next()
        tessellator.draw()

        shader.unbind()
    }

    override fun applyPostRender(component: BaseComponent, stack: MultiMatrixStack, tickDelta: Float) {
        if (!initialized) return

        framebuffer.unbind()

        framebuffer.bindTexture()
        tessellator.beginWithDefaultShader(MultiTessellator.DrawModes.QUADS, MultiTessellator.VertexFormats.POSITION_TEXTURE)
        val x1 = component.getX()
        val y1 = component.getY()
        val x2 = component.getRight()
        val y2 = component.getBottom()
        tessellator.vertex(stack, x1, y1, 0f).texture(0f, 0f).next()
        tessellator.vertex(stack, x2, y1, 0f).texture(1f, 0f).next()
        tessellator.vertex(stack, x2, y2, 0f).texture(1f, 1f).next()
        tessellator.vertex(stack, x1, y2, 0f).texture(0f, 1f).next()
        tessellator.draw()
        framebuffer.unbindTexture()
    }
}
