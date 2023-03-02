package xyz.deftu.componency.effects

import org.lwjgl.opengl.GL11
import xyz.deftu.componency.components.BaseComponent
import xyz.deftu.multi.MultiMatrixStack
import xyz.deftu.multi.MultiResolution
import kotlin.math.max
import kotlin.math.min

class ScissorEffect(
    private val boundingBox: BaseComponent? = null,
    private val scissorIntersection: Boolean = true
) : Effect {
    companion object {
        @JvmStatic var currentState: ScissorState? = null
            private set
    }

    private var previousState: ScissorState? = null
    private var bounds: ScissorBox? = null

    constructor(
        x1: Int,
        y1: Int,
        x2: Int,
        y2: Int,
        scissorIntersection: Boolean = true
    ) : this(scissorIntersection = scissorIntersection) {
        bounds = ScissorBox(x1, y1, x2, y2)
    }

    override fun applyPreRender(component: BaseComponent, stack: MultiMatrixStack, tickDelta: Float) {
        val bounds = boundingBox?.getScissorBox() ?: bounds ?: component.getScissorBox()
        val scaleFactor = MultiResolution.scaleFactor.toInt()

        if (currentState == null) GL11.glEnable(GL11.GL_SCISSOR_TEST)
        previousState = currentState
        val state = previousState

        var x = bounds.x1 * scaleFactor
        var y = MultiResolution.viewportHeight - (bounds.y2 * scaleFactor)
        var width = bounds.width * scaleFactor
        var height = bounds.height * scaleFactor

        if (currentState != null && scissorIntersection) {
            val x2 = x + width
            val y2 = y + height

            val oldX = state!!.x
            val oldY = state.y
            val oldX2 = oldX + state.width
            val oldY2 = oldY + state.height

            x = max(x, oldX)
            y = max(y, oldY)
            width = min(x2, oldX2) - x
            height = min(y2, oldY2) - y
        }

        GL11.glScissor(x, y, width, height)

        currentState = ScissorState(x, y, width.coerceAtLeast(0), height.coerceAtLeast(0))
    }

    override fun applyPostRender(component: BaseComponent, stack: MultiMatrixStack, tickDelta: Float) {
        val state = previousState

        if (state != null) {
            GL11.glScissor(state.x, state.y, state.width, state.height)
            previousState = null
        } else GL11.glDisable(GL11.GL_SCISSOR_TEST)

        currentState = state
    }

    private fun BaseComponent.getScissorBox() = ScissorBox(
        getX().toInt(),
        getY().toInt(),
        getRight().toInt(),
        getBottom().toInt()
    )

    data class ScissorState(
        val x: Int,
        val y: Int,
        val width: Int,
        val height: Int
    )

    data class ScissorBox(
        val x1: Int,
        val y1: Int,
        val x2: Int,
        val y2: Int
    ) {
        val width: Int
            get() = x2 - x1
        val height: Int
            get() = y2 - y1

        fun contains(x: Int, y: Int): Boolean {
            return x in x1..x2 && y in y1..y2
        }
    }
}
