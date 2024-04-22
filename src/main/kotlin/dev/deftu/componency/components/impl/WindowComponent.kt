@file:Suppress("SetterBackingFieldAssignment", "MemberVisibilityCanBePrivate")

package dev.deftu.componency.components.impl

import dev.deftu.componency.components.Component
import dev.deftu.omnicore.client.render.OmniMatrixStack
import dev.deftu.omnicore.client.render.OmniResolution
import java.io.File

public open class WindowComponent : Component() {
    public companion object {

        /**
         * @return The [WindowComponent] that the [component] is attached to, or null if it is not attached to a window.
         *
         * @see get
         * @see Component
         * @since 0.1.0
         * @author Deftu
         */
        @JvmStatic
        public fun getOrNull(component: Component): WindowComponent? {
            var parent = component.parent
            while (parent != null) {
                if (parent is WindowComponent)
                    return parent
                parent = parent.parent
            }

            return null
        }

        /**
         * @return The [WindowComponent] that the [component] is attached to.
         * @throws IllegalStateException If the [component] is not attached to a window.
         *
         * @see getOrNull
         * @see Component
         * @since 0.1.0
         * @author Deftu
         */
        @JvmStatic
        public fun get(component: Component): WindowComponent =
            getOrNull(component) ?: throw IllegalStateException("Component is not attached to a window")

    }

    private val windowResizeListeners = mutableListOf<() -> Unit>()

    public val framebuffer: OmniFramebuffer = OmniFramebuffer()
    private val frozenFramebuffer: OmniFramebuffer = OmniFramebuffer()
    internal var supportsRedraw: Boolean = true

    // Implementation

    override fun initialize() {
        shouldRedraw = true
        val width = OmniResolution.viewportWidth
        val height = OmniResolution.viewportHeight
        framebuffer.resize(width, height)
        frozenFramebuffer.resize(width, height)
    }

    override fun preRender(stack: OmniMatrixStack, tickDelta: Float) {
        if (supportsRedraw) {
            if (shouldRedraw) {
                framebuffer.bind(true)
            }
        }
    }

    override fun renderChildren(stack: OmniMatrixStack, tickDelta: Float) {
        if (supportsRedraw) {
            if (shouldRedraw) {
                super.renderChildren(stack, tickDelta)
            }
        } else super.renderChildren(stack, tickDelta)
    }

    override fun postRender(stack: OmniMatrixStack, tickDelta: Float) {
        if (supportsRedraw) {
            framebuffer.unbind()
            frozenFramebuffer.draw(stack)
            if (shouldRedraw) {
                frozenFramebuffer.clear()
                frozenFramebuffer.copyFrom(framebuffer)
                shouldRedraw = false
            }

            framebuffer.clear()
        }
    }

    public fun resize(width: Int, height: Int) {
        shouldRedraw = true
        framebuffer.resize(width, height)
        frozenFramebuffer.resize(width, height)
        windowResizeListeners.forEach { it() }
    }

    public fun destroy() {
        framebuffer.delete()
        frozenFramebuffer.delete()
    }

    public fun writeFramebufferDebug(dir: File) {
        framebuffer.writeToFile(dir.resolve("main.png"))
        frozenFramebuffer.writeToFile(dir.resolve("frozen.png"))
    }

    public fun onWindowResize(callback: () -> Unit): () -> Unit {
        windowResizeListeners.add(callback)
        return {
            windowResizeListeners.remove(callback)
        }
    }
}
