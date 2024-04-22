@file:Suppress("unused", "LeakingThis", "MemberVisibilityCanBePrivate")

package dev.deftu.componency.components

import dev.deftu.componency.components.events.ComponentParentChangeEvent
import dev.deftu.componency.utils.ListAddEvent
import dev.deftu.componency.utils.ListClearEvent
import dev.deftu.componency.utils.ListRemoveEvent
import dev.deftu.componency.utils.SubscribeableLinkedList
import dev.deftu.omnicore.client.render.OmniMatrixStack

public abstract class Component {
    private val parentChangeListeners = mutableListOf<(ComponentParentChangeEvent) -> Unit>()

    private var currentlyHovered = false
    private var lastHoveredMouseX = 0.0
    private var lastHoveredMouseY = 0.0

    private var internalIndex = -1
    private var initialized = false
    public var parent: Component? = null
        private set(value) {
            if (field == value) return
            val oldParent = field
            field = value
            parentChangeListeners.forEach { it(ComponentParentChangeEvent(oldParent, value)) }
        }
    public val hasParent: Boolean
        get() = parent != null
    private val children = SubscribeableLinkedList<Component>()

    private var hidden = false
    internal var shouldRedraw = true
        set(value) {
            if (value) parent?.shouldRedraw = true
            field = value
        }
    private var requestedRedraw = false
        set(value) {
            if (value) parent?.requestedRedraw = true
            field = value
        }

    init {
        children.subscribe { event ->
            when (event) {
                is ListAddEvent -> {
                    event.element.value.parent = this
                    event.element.value.internalIndex = event.element.index
                }

                is ListRemoveEvent -> {
                    event.element.value.parent = null
                    event.element.value.internalIndex = -1
                }

                is ListClearEvent -> {
                    event.elements.forEach { it.parent = null }
                }
            }
        }
    }

    // Abstractions

    public open fun preRender(
        stack: OmniMatrixStack,
        tickDelta: Float
    ) {
    }

    public open fun renderChildren(
        stack: OmniMatrixStack,
        tickDelta: Float
    ) {
        children.linkedEach { child -> renderChild(child, stack, tickDelta) }
    }

    public open fun renderChild(
        child: Component,
        stack: OmniMatrixStack,
        tickDelta: Float
    ) {
        child.handleRender(stack, tickDelta)
    }

    public open fun postRender(
        stack: OmniMatrixStack,
        tickDelta: Float
    ) {
    }

    public open fun initialize() {
    }

    // Implementation

    /**
     * Forces this component to be redrawn on the next frame.
     */
    public fun requestRedraw() {
        requestedRedraw = true
    }

    /**
     * Adds a child to this component.
     *
     * If:
     * - The child already has a parent, it will be removed from that parent.
     * - The window containing this component supports frame pausing, the component will be redrawn immediately.
     *
     * @param child The child to add
     */
    public fun addChild(child: Component) {
        children.add(child)
        shouldRedraw = true
    }

    /**
     * Adds a child to this component at the specified index.
     *
     * If:
     * - The child already has a parent, it will be removed from that parent.
     * - The window containing this component supports frame pausing, the component will be redrawn immediately.
     * - The index is out of bounds, the child will be added to the end of the list.
     * - There is already a child at the specified index, it will be pushed back.
     *
     * @param index The index to add the child at
     * @param child The child to add
     */
    public fun addChild(index: Int, child: Component) {
        val actualIndex = if (index < 0 || index >= children.size) children.size else index
        children.add(actualIndex, child)
        shouldRedraw = true
    }

    /**
     * Replaces a child at the specified index with the specified child.
     *
     * If:
     * - The child already has a parent, it will be removed from that parent.
     * - The window containing this component supports frame pausing, the component will be redrawn immediately.
     *
     * @param index The index to replace the child at
     * @param child The child to replace the old child with
     */
    public fun replaceChild(index: Int, child: Component) {
        children[index] = child
        shouldRedraw = true
    }

    /**
     * Removes a child from this component.
     *
     * If:
     * - The window containing this component supports frame pausing, the component will be redrawn immediately.
     *
     * @param child The child to remove
     */
    public fun removeChild(child: Component) {
        children.remove(child)
        shouldRedraw = true
    }

    /**
     * Removes a child from this component at the specified index.
     *
     * If:
     * - The window containing this component supports frame pausing, the component will be redrawn immediately.
     * - The index is out of bounds, an exception will be thrown.
     *
     * @param index The index to remove the child at
     */
    public fun removeChild(index: Int) {
        children[index].parent = null
        children.removeAt(index)
        shouldRedraw = true
    }

    public fun clearChildren() {
        children.forEach { it.parent = null }
        children.clear()
        shouldRedraw = true
    }

    public fun addChildren(vararg children: Component) {
        children.forEach(::addChild)
    }

    public fun removeChildren(vararg children: Component) {
        children.forEach(::removeChild)
    }

    public fun removeChildren(vararg indices: Int) {
        indices.forEach(::removeChild)
    }

    public fun removeChildrenIf(predicate: (Component) -> Boolean) {
        children.filter(predicate).forEach(::removeChild)
    }

    public fun handleRender(
        stack: OmniMatrixStack,
        tickDelta: Float
    ) {
        if (hidden) return
        if (!initialized) {
            initialize()
            initialized = true
        }

        if (requestedRedraw) {
            shouldRedraw = true
            requestedRedraw = false
        }

        preRender(stack, tickDelta)
        renderChildren(stack, tickDelta)
        postRender(stack, tickDelta)
    }

    public fun onParentChange(callback: (ComponentParentChangeEvent) -> Unit): () -> Unit {
        parentChangeListeners.add(callback)
        return {
            parentChangeListeners.remove(callback)
        }
    }
}
