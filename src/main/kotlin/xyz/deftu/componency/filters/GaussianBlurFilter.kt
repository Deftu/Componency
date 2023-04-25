package xyz.deftu.componency.filters

import net.minecraft.client.gl.PostEffectProcessor
import net.minecraft.client.gl.ShaderProgram
import net.minecraft.client.render.GameRenderer
import net.minecraft.util.Identifier
import xyz.deftu.componency.components.BaseComponent
import xyz.deftu.multi.MultiClient
import xyz.deftu.multi.MultiFramebuffer
import xyz.deftu.multi.MultiMatrixStack
import xyz.deftu.multi.MultiResolution
import xyz.deftu.multi.shader.BlendState
import xyz.deftu.multi.shader.MultiShader
import xyz.deftu.multi.shader.Vec2Uniform
import xyz.deftu.multi.shader.VecUniform
import xyz.deftu.state.SimpleState
import xyz.deftu.state.State

data class GaussianBlurDirection(
    val horizontal: Float,
    val vertical: Float
) {
    companion object {
        val UP = GaussianBlurDirection(0f, 1f)
        val DOWN = GaussianBlurDirection(0f, -1f)
        val LEFT = GaussianBlurDirection(-1f, 0f)
        val RIGHT = GaussianBlurDirection(1f, 0f)
        val UP_LEFT = GaussianBlurDirection(-1f, 1f)
        val UP_RIGHT = GaussianBlurDirection(1f, 1f)
        val DOWN_LEFT = GaussianBlurDirection(-1f, -1f)
        val DOWN_RIGHT = GaussianBlurDirection(1f, -1f)
    }
}

class GaussianBlurFilter(
    radius: State<Float>,
    direction: State<GaussianBlurDirection> = SimpleState(GaussianBlurDirection.UP_RIGHT)
) : Filter {
    companion object {
        private lateinit var framebuffer: MultiFramebuffer
        // private lateinit var shader: MultiShader
        // private lateinit var radiusUniform: VecUniform
        // private lateinit var directionUniform: Vec2Uniform
        private val initialized: Boolean
            get() = ::framebuffer.isInitialized // && ::shader.isInitialized

        @JvmStatic fun initialize() {
            if (initialized) return

            println("Initializing gaussian blur effect")
            framebuffer = MultiFramebuffer()
            framebuffer.initialize()
            // shader = MultiShader.readFromResource("gaussian_blur", "gaussian_blur", BlendState.NORMAL)
            // if (!shader.usable) error("Failed to load gaussian blur shader")

            // shader.getVec2Uniform("InSize").setValue(1f, 1f)
            // shader.getVec2Uniform("OutSize").setValue(1f, 1f)

            // radiusUniform = shader.getVecUniform("u_Radius")
            // directionUniform = shader.getVec2Uniform("u_BlurDir")
        }

        @JvmStatic fun resize() {
            if (!initialized) return
            framebuffer.resize(MultiResolution.viewportWidth, MultiResolution.viewportHeight)
        }
    }

    constructor(radius: Float, direction: GaussianBlurDirection) : this(SimpleState(radius), SimpleState(direction))
    constructor(radius: Float) : this(SimpleState(radius))

    private val radiusState = radius.map { it }
    private val directionState = direction.map { it }

    private val radius: Float
        get() = radiusState.get()
    private val direction: GaussianBlurDirection
        get() = directionState.get()

    override fun applyPreRender(component: BaseComponent, stack: MultiMatrixStack, tickDelta: Float) {
        if (!initialized) return

        framebuffer.beginWrite(true)
        //shader.bind()

        //radiusUniform.setValue(radius)
        //directionUniform.setValue(direction.horizontal, direction.vertical)
    }

    override fun applyPostRender(component: BaseComponent, stack: MultiMatrixStack, tickDelta: Float) {
        if (!initialized) return

        framebuffer.endWrite()
        framebuffer.unbind()
        //shader.unbind()

        framebuffer.draw()
    }

    fun bindRadius(state: State<Float>) = apply {
        radiusState.rebind(state)
    }

    fun bindDirection(state: State<GaussianBlurDirection>) = apply {
        directionState.rebind(state)
    }
}
