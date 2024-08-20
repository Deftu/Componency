@file:Suppress("unused", "LeakingThis", "MemberVisibilityCanBePrivate", "UNCHECKED_CAST")

package dev.deftu.componency.components

import dev.deftu.componency.animations.ComponentAnimationProperties
import dev.deftu.componency.components.events.*
import dev.deftu.componency.dsl.px
import dev.deftu.componency.effects.Effect
import dev.deftu.componency.engine.Engine
import dev.deftu.componency.exceptions.EnginePresentException
import dev.deftu.componency.exceptions.InvalidHierarchyException
import dev.deftu.componency.input.Key
import dev.deftu.componency.input.MouseButton
import dev.deftu.componency.utils.Animateable
import java.util.LinkedList
import java.util.function.Consumer

/**
 * The base class for all components, housing the basic functionality for rendering, managing children, handling events, etc.
 *
 * @since 0.1.0
 * @author Deftu
 */
public abstract class Component : Animateable {

    public companion object {

        @JvmStatic
        public fun findRootOrNull(component: Component): Component? {
            var current = component
            while (current.hasParent) {
                current = current.parent ?: return null
            }

            return current
        }

        @JvmStatic
        public fun findRoot(component: Component): Component {
            return findRootOrNull(component) ?: throw IllegalStateException("Component has no root")
        }

        @JvmStatic
        public fun <T : Component> configure(component: T, block: Consumer<ComponentConfiguration>): T {
            block.accept(component.config)
            return component
        }

        @JvmStatic
        public fun <T : ComponentProperties> properties(properties: T, block: Consumer<T>): T {
            block.accept(properties)
            return properties
        }

        @JvmStatic
        public fun <T : ComponentEffects> effects(effects: T, block: Consumer<T>): T {
            block.accept(effects)
            return effects
        }

    }

    public var engine: Engine? = null
        private set
    public var parent: Component? = null
        private set

    public val hasParent: Boolean
        get() = parent != null

    public val config: ComponentConfiguration = ComponentConfiguration(this)

    public val events: ComponentEvents = ComponentEvents()

    public var width: Float
        get() = config.properties.width.getWidth(this)
        set(value) { config.properties.width = value.px }

    public var height: Float
        get() = config.properties.height.getHeight(this)
        set(value) { config.properties.height = value.px }

    public val top: Float
        get() = config.properties.y.getY(this)

    public val left: Float
        get() = config.properties.x.getX(this)

    public val right: Float
        get() = left + width

    public val bottom: Float
        get() = top + height

    private var isRoot = false
    private var systemTime = -1L

    private val children = LinkedList<Component>()

    protected val mousePos: Pair<Double, Double>
        get() {
            val engine = engine!!
            val halfPixel = 0.5 / engine.renderEngine.pixelRatio
            return Pair(engine.inputEngine.mouseX + halfPixel, engine.inputEngine.mouseY + halfPixel)
        }

    // Input state
    /// Mouse Hover
    private var isCurrentlyHovered = false
    private var lastHoveredMouseX = 0.0
    private var lastHoveredMouseY = 0.0

    /// Mouse Drag & Click
    private var lastDraggedMouseX = 0.0
    private var lastDraggedMouseY = 0.0
    private var currentClickCount = 0
    private var lastClickButton = MouseButton.UNKNOWN
    private var lastClickTime = 0L

    private var internalIndex = -1
    private var initialized = false

    // Abstractions

    public open val isAlreadyCentered: Boolean = false

    /**
     * Called on the first render frame.
     *
     * @since 0.1.0
     * @author Deftu
     */
    public open fun initialize() {
    }

    /**
     * Renders before anything else, including effects, called before [render].
     *
     * @see render
     * @see renderChildren
     * @see postRender
     * @since 0.1.0
     * @author Deftu
     */
    public open fun preRender() {
    }

    /**
     * Renders the component itself, called after [preRender] and before [renderChildren].
     *
     * @see preRender
     * @see renderChildren
     * @see postRender
     * @since 0.1.0
     * @author Deftu
     */
    public open fun render() {
    }

    /**
     * Renders the children of this component, called after [render] and before [postRender].
     *
     * @see preRender
     * @see render
     * @see renderChildren
     * @see postRender
     * @since 0.1.0
     * @author Deftu
     */
    public open fun renderChildren() {
        children.forEach { child -> renderChild(child) }
    }

    /**
     * Renders a child component.
     *
     * @see preRender
     * @see render
     * @see renderChildren
     * @see postRender
     * @since 0.1.0
     * @author Deftu
     */
    public open fun renderChild(
        child: Component
    ) {
        child.handleRender()
    }

    /**
     * Renders after everything else, called after [renderChildren].
     *
     * @see preRender
     * @see render
     * @see renderChildren
     * @since 0.1.0
     * @author Deftu
     */
    public open fun postRender() {
    }

    /**
     * Handles an animation frame.
     *
     * @since 0.1.0
     * @author Deftu
     */
    public override fun frame() {
        if (isRoot && lastClickButton != MouseButton.UNKNOWN) {
            val (x, y) = mousePos
            handleMouseDrag(x, y)
        }

        this.config.frame()
        this.children.forEach(Animateable::frame)
    }

    public open fun isPointInside(x: Double, y: Double): Boolean {
        return x in left..right && y in top..bottom
    }

    public open fun hitTest(x: Double, y: Double): Component {
        for (child in children.reversed()) {
            if (child.isPointInside(x, y)) {
                return child.hitTest(x, y)
            }
        }

        return this
    }

    // Implementation

    /// Hierarchy

    public open fun makeRoot(engine: Engine): Component = apply {
        if (this.engine != null) {
            throw EnginePresentException("Root component already has a window assigned to it")
        }

        if (hasParent) {
            throw InvalidHierarchyException("Root component cannot have a parent")
        }

        this.isRoot = true
        this.engine = engine
    }

    public open fun <T : Component> attachedTo(parent: Component): T = apply {
        parent.addChild(this)
    } as T

    /**
     * Adds a child to this component.
     *
     * @param child The child to add
     *
     * @since 0.1.0
     * @author Deftu
     */
    public open fun addChild(child: Component) {
        child.parent?.removeChild(child)
        this.children.add(child)
        child.parent = this
        child.engine = this.engine
    }

    /**
     * Adds a child to this component at the specified index.
     *
     * @param index The index to add the child at
     * @param child The child to add
     * @throws IndexOutOfBoundsException If the index is less than 0 or greater than the size of the children list
     *
     * @since 0.1.0
     * @author Deftu
     */
    public open fun addChild(index: Int, child: Component) {
        if (index < 0 || index > this.children.size) {
            throw IndexOutOfBoundsException("Index $index is out of bounds for children list of size ${children.size}")
        }

        child.parent?.removeChild(child)
        this.children.add(index, child)
        child.parent = this
        child.engine = this.engine
    }

    /**
     * Replaces a child at the specified index with the specified child.
     *
     * @param index The index to replace the child at
     * @param child The child to replace the old child with
     * @throws IndexOutOfBoundsException If the index is less than 0 or greater than the size of the children list
     *
     * @since 0.1.0
     * @author Deftu
     */
    public open fun replaceChild(index: Int, child: Component) {
        if (index < 0 || index >= this.children.size) {
            throw IndexOutOfBoundsException("Index $index is out of bounds for children list of size ${this.children.size}")
        }

        this.children[index].parent = null
        this.children[index] = child
        child.parent = this
        child.engine = this.engine
    }

    /**
     * Removes a child from this component.
     *
     * @param child The child to remove
     *
     * @since 0.1.0
     * @author Deftu
     */
    public open fun removeChild(child: Component) {
        this.children.remove(child)
    }

    /**
     * Removes a child from this component at the specified index.
     *
     * @param index The index to remove the child at
     * @throws IndexOutOfBoundsException If the index is less than 0 or greater than the size of the children list
     *
     * @since 0.1.0
     * @author Deftu
     */
    public open fun removeChild(index: Int) {
        if (index < 0 || index >= this.children.size) {
            throw IndexOutOfBoundsException("Index $index is out of bounds for children list of size ${this.children.size}")
        }

        this.children[index].parent = null
        this.children.removeAt(index)
    }

    /**
     * Removes all children from this component.
     *
     * @since 0.1.0
     * @author Deftu
     */
    public open fun clearChildren() {
        this.children.forEach { it.parent = null }
        this.children.clear()
    }

    /**
     * Adds multiple children to this component.
     *
     * @param children The children to add
     *
     * @see addChild
     * @since 0.1.0
     * @author Deftu
     */
    public fun addChildren(vararg children: Component) {
        children.forEach(::addChild)
    }

    /**
     * Removes multiple children from this component.
     *
     * @param children The children to remove
     *
     * @see removeChild
     * @since 0.1.0
     * @author Deftu
     */
    public fun removeChildren(vararg children: Component) {
        children.forEach(::removeChild)
    }

    /**
     * Removes children from this component at the given indices.
     *
     * @param indices The indices to remove children at
     * @throws IndexOutOfBoundsException If any of the indices are less than 0 or greater than the size of the children list
     *
     * @see removeChild
     * @since 0.1.0
     * @author Deftu
     */
    public fun removeChildren(vararg indices: Int) {
        indices.forEach(::removeChild)
    }

    public open fun getChildren(): List<Component> {
        return children.toList()
    }

    public open fun getChildAt(index: Int): Component {
        return children[index]
    }

    public open fun indexOfChild(child: Component): Int {
        return children.indexOf(child)
    }

    public fun addEffect(effect: Effect) {
        config.effects.add(effect)
    }

    public fun removeEffect(effect: Effect) {
        config.effects.remove(effect)
    }

    /// Events

    protected fun fireMouseClickEvent(event: MouseClickEvent) {
        for (listener in events.mouseClickListeners) {
            listener.invoke(event)

            if (event.isCancelled) {
                return
            }
        }

        if (hasParent) {
            parent!!.fireMouseClickEvent(event.copy(component = parent!!))
        }
    }

    protected fun fireMouseScrollEvent(event: MouseScrollEvent) {
        for (listener in events.mouseScrollListeners) {
            listener.invoke(event)

            if (event.isCancelled) {
                return
            }
        }

        if (hasParent) {
            parent!!.fireMouseScrollEvent(event.copy(component = parent!!))
        }
    }

    public fun <T : Component> onMouseClick(listener: Consumer<MouseClickEvent>): T = apply {
        events.mouseClickListeners.add(listener::accept)
    } as T

    public fun <T : Component> onMouseRelease(listener: Consumer<MouseReleaseEvent>): T = apply {
        events.mouseReleaseListeners.add(listener::accept)
    } as T

    public fun <T : Component> onMouseDrag(listener: Consumer<MouseDragEvent>): T = apply {
        events.mouseDragListeners.add(listener::accept)
    } as T

    public fun <T : Component> onMouseScroll(listener: Consumer<MouseScrollEvent>): T = apply {
        events.mouseScrollListeners.add(listener::accept)
    } as T

    public fun <T : Component> onMouseHover(listener: Consumer<MouseHoverEvent>): T = apply {
        events.mouseHoverListeners.add(listener::accept)
    } as T

    public fun <T : Component> onMouseUnhover(listener: Consumer<MouseHoverEvent>): T = apply {
        events.mouseUnhoverListeners.add(listener::accept)
    } as T

    public fun <T : Component> onKeyPress(listener: Consumer<KeyPressEvent>): T = apply {
        events.keyPressListeners.add(listener::accept)
    } as T

    public fun beginAnimation(): ComponentAnimationProperties {
        return config.beginAnimation()
    }

    /**
     * Calls all render-relating operations in their correct order to render the component.
     *
     * @see initialize
     * @see preRender
     * @see render
     * @see renderChildren
     * @see postRender
     * @since 0.1.0
     * @author Deftu
     */
    public fun handleRender() {
        // Tell our engine that we're starting a new frame
        if (isRoot) {
            engine!!.renderEngine.startFrame()
        }

        if (!initialized) {
            // Initialize the component on the first possible frame to ensure that it has time to set up
            initialize()
            initialized = true
        }

        // Begin our render operations!
        preRender()
        config.effects.preRender()
        if (isRoot) {
            renderRoot()
        }

        render()
        renderChildren()
        postRender()
        config.effects.postRender()

        // Tell our engine that we're ending the frame
        if (isRoot) {
            engine!!.renderEngine.endFrame()
        }

        // Update mouse state
        if (isRoot) {
            val (x, y) = mousePos
            handleMouseMove(x, y)
        }
    }

    public fun handleMouseMove(x: Double, y: Double) {
        val isHovered = isPointInside(x, y)

        this.lastHoveredMouseX = x
        this.lastHoveredMouseY = y

        if (isHovered && !isCurrentlyHovered) {
            for (listener in events.mouseHoverListeners) {
                listener.invoke(MouseHoverEvent(this, x, y))
            }

            this.isCurrentlyHovered = true
        } else if (!isHovered && isCurrentlyHovered) {
            for (listener in events.mouseUnhoverListeners) {
                listener.invoke(MouseHoverEvent(this, x, y))
            }

            this.isCurrentlyHovered = false
        }

        for (child in children) {
            child.handleMouseMove(x, y)
        }
    }

    public open fun handleMouseClick(x: Double, y: Double, button: MouseButton): Boolean {
        val clickedChild = hitTest(x, y)

        this.lastDraggedMouseX = x
        this.lastDraggedMouseY = y
        this.currentClickCount = if (System.currentTimeMillis() - lastClickTime < 500) currentClickCount + 1 else 1
        this.lastClickButton = button
        this.lastClickTime = System.currentTimeMillis()

        clickedChild.fireMouseClickEvent(MouseClickEvent(clickedChild, x, y, button, currentClickCount))

        return true
    }

    public open fun handleMouseRelease() {
        for (listener in events.mouseReleaseListeners) {
            listener.invoke(MouseReleaseEvent(this, lastDraggedMouseX, lastDraggedMouseY, lastClickButton))
        }

        lastDraggedMouseX = -1.0
        lastDraggedMouseY = -1.0
        lastClickButton = MouseButton.UNKNOWN

        children.forEach(Component::handleMouseRelease)
    }

    public open fun handleMouseDrag(x: Double, y: Double) {
        if (
            lastDraggedMouseY == x ||
            lastDraggedMouseY == y
        ) {
            return
        }

        this.lastDraggedMouseX = x
        this.lastDraggedMouseY = y

        for (listener in events.mouseDragListeners) {
            listener.invoke(MouseDragEvent(this, x, y, lastClickButton))
        }

        children.forEach { child -> child.handleMouseDrag(x, y) }
    }

    public open fun handleMouseScroll(amount: Double) {
        if (amount == 0.0) {
            return
        }


        for (child in children.reversed()) {
            if (child.isCurrentlyHovered) {
                child.handleMouseScroll(amount)
                return
            }
        }

        fireMouseScrollEvent(MouseScrollEvent(this, amount))
    }

    public open fun handleKeyPress(key: Key, modifiers: KeyboardModifiers) {
        for (listener in events.keyPressListeners) {
            listener.invoke(KeyPressEvent(this, key, modifiers))
        }

        if (isRoot && focusedChild != null) {
            focusedChild!!.handleKeyPress(key, modifiers)
        }
    }

    public open fun handleKeyRelease(key: Key, modifiers: KeyboardModifiers) {
        for (listener in events.keyReleaseListeners) {
            listener.invoke(KeyReleaseEvent(this, key, modifiers))
        }

        if (isRoot && focusedChild != null) {
            focusedChild!!.handleKeyRelease(key, modifiers)
        }
    }

    private fun renderRoot() {
        val engine = engine!!

        if (systemTime == -1L) {
            systemTime = System.currentTimeMillis()
        }

        try {
            val animationFps = engine.renderEngine.animationFps
            val currentTime = System.currentTimeMillis()

            // Our window is behind - We're going to have to skip a few frames to catch up
            if (currentTime - systemTime > 2_500) {
                systemTime = System.currentTimeMillis()
            }

            val targetTime = currentTime + 1_000 / animationFps
            val frameCount = (targetTime - systemTime).toInt() / animationFps
            repeat(frameCount.coerceAtMost((animationFps / 30).coerceAtLeast(1))) {
                frame()
                systemTime += 1_000 / animationFps
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

}
