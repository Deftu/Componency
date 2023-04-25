package xyz.deftu.componency.components

import xyz.deftu.componency.components.impl.RoundedBoxComponent
import xyz.deftu.componency.effects.Effect
import xyz.deftu.componency.effects.OutlineEffect
import xyz.deftu.componency.effects.ScissorEffect
import xyz.deftu.componency.filters.GaussianBlurFilter
import xyz.deftu.componency.utils.fixMousePos
import xyz.deftu.multi.*
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
            //#if MC>=11900
            val fps = MultiClient.getOptions().maxFps.value
            //#elseif MC>=11500
            //$$ val fps = MultiClient.getOptions().maxFps
            //#else
            //$$ val fps = MultiClient.getOptions().limitFramerate
            //#endif

            // Add a small bit extra to make sure it doesn't slow down - This keeps the animation smooth while still adapting to the requested framerate
            (fps * 1.25).roundToInt()
        } else animationFramerate

    private var lastClickedMouseButton = -1

    private var requestingFocus: BaseComponent? = null
    private var focusedComponent: BaseComponent? = null

    init {
        super.parent = this
    }

    fun focus(component: BaseComponent) = apply {
        requestingFocus = component
    }

    fun unfocus() = apply {
        focusedComponent?.handleFocusLost()
        focusedComponent = null
    }

    fun isFocused(component: BaseComponent) = focusedComponent == component

    private fun handleFocusState() {
        if (requestingFocus == null) {
            unfocus()
        } else {
            if (requestingFocus != focusedComponent) {
                if (focusedComponent != null) focusedComponent?.handleFocusLost()

                focusedComponent = requestingFocus
                focusedComponent?.handleFocusGained()
            }

            requestingFocus = null
        }
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

    override fun findTarget(x: Double, y: Double): BaseComponent {
        for (child in floatingChildren.reversed()) {
            if (child.isInsideBounds(x, y)) {
                return child.findTarget(x, y)
            }
        }

        return super.findTarget(x, y)
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
            val targetTime = System.currentTimeMillis() + 1000 / trueAnimationFramerate
            val animationFrames = (targetTime - sysTime).toInt() / trueAnimationFramerate
            repeat(animationFrames.coerceAtMost((trueAnimationFramerate / 30).coerceAtLeast(1))) {
                handleAnimate()
                sysTime += 1000 / trueAnimationFramerate
            }

            // Call the mouse move event to make sure that the mouse is updated
            mouseUpdate(MultiMouse.scaledX, MultiMouse.scaledY)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    override fun handleInitialize() {
        addRenderOperation {
            // Components
            RoundedBoxComponent.initialize()

            // Constraints
            // ...

            // Effects
            OutlineEffect.initialize()

            // Filters
            GaussianBlurFilter.initialize()
        }
    }

    override fun mouseClick(x: Double, y: Double, button: Int): Boolean {
        val (fixedX, fixedY) = fixMousePos(x, y)
        val value = super.mouseClick(fixedX, fixedY, button)
        handleFocusState()
        return value
    }

    override fun keyPress(keyCode: Int, typedChar: Char, modifiers: MultiKeyboard.KeyboardModifiers): Boolean {
        // If the typed char is in a PUA (https://en.wikipedia.org/wiki/Private_Use_Areas), we don't want to propagate it down
        val character = if (typedChar in CharCategory.PRIVATE_USE) Char.MIN_VALUE else typedChar
        return focusedComponent?.keyPress(keyCode, character, modifiers) ?: super.keyPress(keyCode, character, modifiers)
    }

    override fun handleAnimate() {
        if (lastClickedMouseButton != -1) {
            val (x, y) = fixMousePos(MultiMouse.scaledX, MultiMouse.scaledY)
            mouseDrag(x, y, lastClickedMouseButton)
        }

        handleFocusState()
        super.handleAnimate()
    }

    override fun applyEffect(effect: Effect) = error("Cannot add effects to a window")
    override fun removeEffect(effect: Effect) = error("Cannot remove effects from a window")

    override fun getX() = 0f
    override fun getY() = 0f
    override fun getWidth() = MultiResolution.scaledWidth.toFloat()
    override fun getHeight() = MultiResolution.scaledHeight.toFloat()
    override fun getRight() = getWidth()
    override fun getBottom() = getHeight()
}
