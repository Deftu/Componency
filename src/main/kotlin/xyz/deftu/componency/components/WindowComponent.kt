package xyz.deftu.componency.components

import xyz.deftu.componency.components.impl.RoundedBoxComponent
import xyz.deftu.componency.effects.Effect
import xyz.deftu.componency.effects.ScissorEffect
import xyz.deftu.multi.MultiClient
import xyz.deftu.multi.MultiMatrixStack
import xyz.deftu.multi.MultiMouse
import xyz.deftu.multi.MultiResolution
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.math.roundToInt

class WindowComponent(
    private val animationFramerate: Int = 144,
    private val adaptiveFramerate: Boolean = true
) : BaseComponent() {
    companion object {
        private val renderOperations = ConcurrentLinkedQueue<() -> Unit>()

        @JvmStatic
        fun addRenderOperation(operation: () -> Unit) {
            renderOperations.add(operation)
        }

        @JvmStatic
        fun addRenderOperation(operation: Runnable) {
            renderOperations.add(operation::run)
        }

        @JvmStatic
        fun findOrNull(component: BaseComponent): WindowComponent? {
            var parent = component.parent
            while (parent !is WindowComponent) {
                if (parent.parent == parent) return null
                parent = parent.parent
            }

            return parent
        }

        @JvmStatic
        fun find(component: BaseComponent): WindowComponent {
            var parent = component.parent
            while (parent !is WindowComponent) {
                if (parent.parent == parent) error("Component is not attached to a window")
                parent = parent.parent
            }

            return parent
        }
    }

    private var sysTime = -1L
    val floatingChildren = mutableListOf<BaseComponent>()

    val trueAnimationFramerate: Int
        get() = if (adaptiveFramerate) {
            val fps = MultiClient.getOptions().maxFps.value
            // Add a small bit extra to make sure it doesn't slow down - This keeps the animation smooth while still adapting to the requested framerate
            (fps * 1.25).roundToInt()
        } else animationFramerate

    init {
        super.parent = this
    }

    fun isVisible(
        x: Float,
        y: Float,
        right: Float,
        bottom: Float
    ): Boolean {
        if (
            getX() > right ||
            getY() > bottom ||
            getRight() < x ||
            getBottom() < y
        ) return false

        val scissorState = ScissorEffect.currentState ?: return true
        val scaleFactor = MultiResolution.scaleFactor
        val trueX = (scissorState.x / scaleFactor).roundToInt()
        val trueWidth = (scissorState.width / scaleFactor).roundToInt()
        val bottomY = ((MultiResolution.scaledHeight * scaleFactor) - scissorState.y).roundToInt()
        val trueHeight = (scissorState.height / scaleFactor).roundToInt()

        return (
            right > trueX &&
            x < trueX + trueWidth &&
            bottom >= bottomY - trueHeight &&
            y <= bottomY
        )
    }

    override fun render(stack: MultiMatrixStack, tickDelta: Float) {
        val renderOperations = renderOperations.iterator()
        while (renderOperations.hasNext()) {
            renderOperations.next()()
            renderOperations.remove()
        }

        if (sysTime == -1L) sysTime = System.currentTimeMillis()

        try {
            // Catch up the window if it's behind - This may result in a few skipped frames but it's better than the window freezing
            if (System.currentTimeMillis() - sysTime > 2500) sysTime = System.currentTimeMillis()

            // Make sure that animations are handled at the correct framerate
            while (System.currentTimeMillis() + 1000 / trueAnimationFramerate > sysTime) {
                handleAnimate()
                sysTime += 1000 / trueAnimationFramerate
            }

            // Call the mouse move event to make sure that the mouse is updated
            mouseMove(MultiMouse.scaledX.toFloat(), MultiMouse.scaledY.toFloat())
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    override fun handleInitialize() {
        addRenderOperation {
            RoundedBoxComponent.initialize()
        }
    }

    override fun applyEffect(effect: Effect) = error("Cannot add effects to a window")
    override fun removeEffect(effect: Effect) = error("Cannot remove effects from a window")

    override fun getX() = 0f
    override fun getY() = 0f
    override fun getWidth() = MultiResolution.scaledWidth.toFloat()
    override fun getHeight() = MultiResolution.scaledHeight.toFloat()
}
