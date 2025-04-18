@file:Suppress("unused", "LeakingThis", "MemberVisibilityCanBePrivate", "UNCHECKED_CAST")

package dev.deftu.componency.components

import dev.deftu.componency.components.events.*
import dev.deftu.componency.components.traits.Trait
import dev.deftu.componency.dsl.px
import dev.deftu.componency.effects.Effect
import dev.deftu.componency.exceptions.EnginePresentException
import dev.deftu.componency.exceptions.InvalidHierarchyException
import dev.deftu.componency.input.Key
import dev.deftu.componency.input.MouseButton
import dev.deftu.componency.platform.Platform
import java.util.LinkedList
import java.util.function.Consumer
import kotlin.reflect.KClass

public abstract class Component<T : Component<T, C>, C : ComponentProperties<T, C>>(
    propertiesFactory: (T) -> C,
) : ComponentRoot<T>, ComponentEventConsumer<T> {

    public companion object {

        @JvmStatic
        public fun findRootOrNull(component: Component<*, *>): Component<*, *>? {
            var current = component
            while (current.hasParent) {
                current = current.parent ?: return null
            }

            return current
        }

        @JvmStatic
        public fun findRoot(component: Component<*, *>): Component<*, *> {
            return findRootOrNull(component) ?: throw IllegalStateException("Component has no root")
        }

        @JvmStatic
        public fun findPlatformOrNull(component: Component<*, *>): Platform? {
            return findRootOrNull(component)?.platform
        }

        @JvmStatic
        public fun findPlatform(component: Component<*, *>): Platform {
            return findPlatformOrNull(component) ?: throw IllegalStateException("Component has no platform")
        }

    }

    public var parent: Component<*, *>? = null
        private set

    public val hasParent: Boolean
        get() = parent != null

    public var properties: C = propertiesFactory(this as T)
        private set

    public val effects: ComponentEffects<T, C> by lazy {
        ComponentEffects(this)
    }

    public val x: Float
        get() = properties.x.getX(this)

    public val y: Float
        get() = properties.y.getY(this)

    public var width: Float
        get() = properties.width.getWidth(this)
        set(value) { properties.width = value.px }

    public var height: Float
        get() = properties.height.getHeight(this)
        set(value) { properties.height = value.px }

    public val top: Float
        get() = y

    public val left: Float
        get() = x

    public val bottom: Float
        get() = top + height

    public val right: Float
        get() = left + width

    override var isRoot: Boolean = false
    private var systemTime = -1L

    private val children = LinkedList<Component<*, *>>()
    private var indexInParent: Int = -1
    private val eventBus = ComponentEventBus<T, C>()
    private val traits = mutableListOf<Trait>()

    public val isFocused: Boolean
        get() {
            return if (isRoot) {
                focusManager.isFocused
            } else {
                findRoot(this).focusManager.currentlyFocused == this
            }
        }

    // Responsibilities of root components
    public final override var platform: Platform? = null
        private set
    public val focusManager: FocusManager = FocusManager()

    // Pointer state
    private val pointerTracker = PointerTracker<T, C>()
    protected val pointerPos: Pair<Double, Double>
        get() {
            val platform = platform!!
            val halfPixel = 0.5 / platform.pixelRatio
            return Pair(
                platform.inputHandler.pointerInput.pointerX + halfPixel,
                platform.inputHandler.pointerInput.pointerY + halfPixel
            )
        }

    private var internalIndex = -1
    private var initialized = false

    // Abstractions

    public open val isAlreadyCentered: Boolean = false

    public open fun initialize() {
    }

    public open fun preRender() {
    }

    public open fun render() {
    }

    public open fun renderChildren() {
        children.forEach { child -> renderChild(child) }
    }

    public open fun renderChild(child: Component<*, *>) {
        child.handleRender()
    }

    public open fun postRender() {
    }

    public open fun animationFrame(deltaTime: Float) {
        if (isRoot && pointerTracker.lastClickedButton != MouseButton.UNKNOWN) {
            val (x, y) = pointerPos
            handleMouseDrag(x, y)
        }

        this.properties.animationFrame(deltaTime)
        this.children.forEach { child ->
            child.animationFrame(deltaTime)
        }
    }

    public open fun recalculate() {
        this.properties.recalculate()
        this.children.forEach(Component<*, *>::recalculate)
    }

    // Configuration

    public fun setProperties(properties: C): T = apply {
        this.properties = properties
        properties.recalculate()
    } as T

    public fun configure(block: Consumer<C>): T = apply {
        block.accept(properties)
    } as T

    /// Hierarchy

    public override fun makeRoot(platform: Platform): T = apply {
        if (this.platform != null) {
            throw EnginePresentException("Root component already has a platform instance assigned to it")
        }

        if (hasParent) {
            throw InvalidHierarchyException("Root component cannot have a parent")
        }

        this.isRoot = true
        this.platform = platform
    } as T

    public open fun isPointInside(x: Double, y: Double): Boolean {
        return x in this.x..right && y in this.y..bottom
    }

    public open fun hitTest(x: Double, y: Double): Component<*, *> {
        for (child in children.reversed()) {
            if (child.isPointInside(x, y)) {
                return child.hitTest(x, y)
            }
        }

        return this
    }

    public open fun attachTo(parent: Component<*, *>): T = apply {
        parent.addChild(this)
    } as T

    public open fun addChild(child: Component<*, *>): T = apply {
        child.parent?.removeChild(child)
        this.children.add(child)
        child.parent = this
        child.platform = this.platform
    } as T

    public open fun addChild(index: Int, child: Component<*, *>) {
        if (index < 0 || index > this.children.size) {
            throw IndexOutOfBoundsException("Index $index is out of bounds for children list of size ${children.size}")
        }

        child.parent?.removeChild(child)
        this.children.add(index, child)
        child.parent = this
        child.platform = this.platform
    }

    public open fun replaceChild(index: Int, child: Component<*, *>) {
        if (index < 0 || index >= this.children.size) {
            throw IndexOutOfBoundsException("Index $index is out of bounds for children list of size ${this.children.size}")
        }

        this.children[index].parent = null
        this.children[index] = child
        child.parent = this
        child.platform = this.platform
    }

    public open fun removeChild(child: Component<*, *>) {
        this.children.remove(child)
    }

    public open fun removeChild(index: Int) {
        if (index < 0 || index >= this.children.size) {
            throw IndexOutOfBoundsException("Index $index is out of bounds for children list of size ${this.children.size}")
        }

        this.children[index].parent = null
        this.children.removeAt(index)
    }

    public open fun clearChildren() {
        this.children.forEach { it.parent = null }
        this.children.clear()
    }

    public fun addChildren(vararg children: Component<*, *>) {
        children.forEach(::addChild)
    }

    public fun removeChildren(vararg children: Component<*, *>) {
        children.forEach(::removeChild)
    }

    public fun removeChildren(vararg indices: Int) {
        indices.forEach(::removeChild)
    }

    public open fun getChildren(): List<Component<*, *>> {
        return children.toList()
    }

    public open fun getChildAt(index: Int): Component<*, *> {
        return children[index]
    }

    public open fun indexOfChild(child: Component<*, *>): Int {
        return children.indexOf(child)
    }

    // Effects

    public fun addEffect(effect: Effect<T, C>): Component<T, C> {
        effects.add(effect)
        return this
    }

    public fun removeEffect(effect: Effect<T, C>): Component<T, C> {
        effects.remove(effect)
        return this
    }

    // Traits

    public fun addTrait(trait: Trait): Component<T, C> {
        traits.add(trait)
        return this
    }

    public fun removeTrait(trait: Trait): Component<T, C> {
        traits.remove(trait)
        return this
    }

    public fun <T : Trait> getTrait(trait: Class<T>): T? {
        return traits.firstOrNull { it::class.java == trait } as? T
    }

    public fun <T : Trait> getTrait(trait: KClass<T>): T? {
        return traits.firstOrNull { it::class == trait } as? T
    }

    public fun <T : Trait> getTrait(trait: Trait): T? {
        return traits.firstOrNull { it == trait } as? T
    }

    public fun getTraits(): List<Trait> {
        return traits.toList()
    }

    public fun hasTrait(trait: Class<out Trait>): Boolean {
        return traits.any { it::class.java == trait }
    }

    public fun hasTrait(trait: KClass<out Trait>): Boolean {
        return traits.any { it::class == trait }
    }

    public fun hasTrait(trait: Trait): Boolean {
        return traits.contains(trait)
    }

    public fun <T : Trait> findChildrenByTrait(
        type: Class<T>,
        recursive: Boolean = false,
        predicate: (Component<*, *>, T) -> Boolean = { _, _ -> true }
    ): List<Component<*, *>> {
        val result = mutableListOf<Component<*, *>>()
        fun findChildren(component: Component<*, *>) {
            if (component.getTrait(type)?.let { predicate(component, it) } == true) {
                result.add(component)
            }

            if (recursive) {
                component.children.forEach(::findChildren)
            }
        }

        findChildren(this)
        return result
    }

    public fun <T : Trait> findChildrenByTrait(
        type: KClass<T>,
        recursive: Boolean = false,
        predicate: (Component<*, *>, T) -> Boolean = { _, _ -> true }
    ): List<Component<*, *>> {
        return findChildrenByTrait(type.java, recursive, predicate)
    }

    public fun <T : Trait> findChildrenByTrait(
        trait: Trait,
        recursive: Boolean = false,
        predicate: (Component<*, *>, T) -> Boolean = { _, _ -> true }
    ): List<Component<*, *>> {
        val result = mutableListOf<Component<*, *>>()
        fun findChildren(component: Component<*, *>) {
            if (component.getTrait<T>(trait)?.let { predicate(component, it) } == true) {
                result.add(component)
            }

            if (recursive) {
                component.children.forEach(::findChildren)
            }
        }

        findChildren(this)
        return result
    }

    public inline fun <reified T : Trait> findChildrenByTrait(
        recursive: Boolean = false,
        noinline predicate: (Component<*, *>, T) -> Boolean = { _, _ -> true }
    ): List<Component<*, *>> {
        return findChildrenByTrait(T::class, recursive, predicate)
    }

    /// Visibility

    @JvmOverloads
    public fun show(useLastPosition: Boolean = true) {
        if (!hasParent) {
            return
        }

        val parent = parent!!
        if (parent.children.contains(this)) {
            return
        }

        if (useLastPosition && indexInParent >= 0 && indexInParent < parent.children.size) {
            parent.addChild(indexInParent, this)
        } else {
            parent.addChild(this)
        }
    }

    public fun hide() {
        if (!hasParent) {
            return
        }

        val parent = parent!!
        indexInParent = parent.children.indexOf(this)
        parent.removeChild(this)
    }

    // Focus

    public fun requestFocus() {
        val root = findRoot(this)
        root.focusManager.requestFocus(this)
    }

    /**
     * Unfocuses the currently focused child.
     *
     * @since 0.1.0
     * @author Deftu
     */
    public fun unfocus() {
        if (isRoot) {
            val root = findRoot(this)
            root.unfocus()
        } else {
            focusManager.releaseFocus()
        }
    }

    /// Events

    public override fun onPointerClick(listener: Consumer<PointerClickEvent>): T = apply {
        eventBus.pointerClickListeners.add(listener::accept)
    } as T

    public override fun onPointerRelease(listener: Consumer<PointerReleaseEvent>): T = apply {
        eventBus.pointerReleaseListeners.add(listener::accept)
    } as T

    public override fun onPointerDrag(listener: Consumer<PointerDragEvent>): T = apply {
        eventBus.pointerDragListeners.add(listener::accept)
    } as T

    public override fun onScroll(listener: Consumer<ScrollEvent>): T = apply {
        eventBus.scrollListeners.add(listener::accept)
    } as T

    public override fun onHover(listener: Consumer<HoverEvent>): T = apply {
        eventBus.hoverListeners.add(listener::accept)
    } as T

    public override fun onUnhover(listener: Consumer<HoverEvent>): T = apply {
        eventBus.unhoverListeners.add(listener::accept)
    } as T

    public override fun onKeyPress(listener: Consumer<KeyPressEvent>): T = apply {
        eventBus.keyPressListeners.add(listener::accept)
    } as T

    public override fun onKeyRelease(listener: Consumer<KeyReleaseEvent>): T = apply {
        eventBus.keyReleaseListeners.add(listener::accept)
    } as T

    public override fun onCharType(listener: Consumer<CharTypeEvent>): T = apply {
        eventBus.charTypeListeners.add(listener::accept)
    } as T

    override fun onFocus(listener: Runnable): T = apply {
        eventBus.focusListeners.add(listener)
    } as T

    override fun onUnfocus(listener: Runnable): T = apply {
        eventBus.unfocusListeners.add(listener)
    } as T

    override fun removePointerClickListener(listener: Consumer<PointerClickEvent>): T = apply {
        eventBus.pointerClickListeners.remove(listener)
    } as T

    override fun removePointerReleaseListener(listener: Consumer<PointerReleaseEvent>): T = apply {
        eventBus.pointerReleaseListeners.remove(listener)
    } as T

    override fun removePointerDragListener(listener: Consumer<PointerDragEvent>): T = apply {
        eventBus.pointerDragListeners.remove(listener)
    } as T

    override fun removeScrollListener(listener: Consumer<ScrollEvent>): T = apply {
        eventBus.scrollListeners.remove(listener)
    } as T

    override fun removeHoverListener(listener: Consumer<HoverEvent>): T = apply {
        eventBus.hoverListeners.remove(listener)
    } as T

    override fun removeUnhoverListener(listener: Consumer<HoverEvent>): T = apply {
        eventBus.unhoverListeners.remove(listener)
    } as T

    override fun removeKeyPressListener(listener: Consumer<KeyPressEvent>): T = apply {
        eventBus.keyPressListeners.remove(listener)
    } as T

    override fun removeKeyReleaseListener(listener: Consumer<KeyReleaseEvent>): T = apply {
        eventBus.keyReleaseListeners.remove(listener)
    } as T

    override fun removeCharTypeListener(listener: Consumer<CharTypeEvent>): T = apply {
        eventBus.charTypeListeners.remove(listener)
    } as T

    override fun removeFocusListener(listener: Runnable): T = apply {
        eventBus.focusListeners.remove(listener)
    } as T

    override fun removeUnfocusListener(listener: Runnable): T = apply {
        eventBus.unfocusListeners.remove(listener)
    } as T

    override fun firePointerClickEvent(event: PointerClickEvent) {
        for (listener in eventBus.pointerClickListeners) {
            listener.accept(event)

            if (event.isCancelled) {
                return
            }
        }

        if (hasParent) {
            parent!!.firePointerClickEvent(event.copy(component = parent!!))
        }
    }

    override fun firePointerReleaseEvent(event: PointerReleaseEvent) {
        for (listener in eventBus.pointerReleaseListeners) {
            listener.accept(event)
            
            if (event.isCancelled) {
                return
            }
        }
        
        if (event.isBubbling && hasParent && parent != this) {
            parent!!.firePointerReleaseEvent(event.copy(component = parent!!))
        }
    }

    override fun firePointerDragEvent(event: PointerDragEvent) {
        for (listener in eventBus.pointerDragListeners) {
            listener.accept(event)
            
            if (event.isCancelled) {
                return
            }
        }

        if (event.isBubbling && hasParent && parent != this) {
            parent!!.firePointerDragEvent(event.copy(component = parent!!))
        }
    }

    override fun fireScrollEvent(event: ScrollEvent) {
        for (listener in eventBus.scrollListeners) {
            listener.accept(event)

            if (event.isCancelled) {
                return
            }
        }

        if (event.isBubbling && hasParent && parent != this) {
            parent!!.fireScrollEvent(event.copy(component = parent!!))
        }
    }

    override fun fireHoverEvent(event: HoverEvent) {
        for (listener in eventBus.hoverListeners) {
            listener.accept(event)
        }

        if (hasParent && parent != this) {
            parent!!.fireHoverEvent(event.copy(component = parent!!))
        }
    }
    
    override fun fireUnhoverEvent(event: HoverEvent) {
        for (listener in eventBus.unhoverListeners) {
            listener.accept(event)
        }

        if (hasParent && parent != this) {
            parent!!.fireUnhoverEvent(event.copy(component = parent!!))
        }
    }
    
    override fun fireKeyPressEvent(event: KeyPressEvent) {
        for (listener in eventBus.keyPressListeners) {
            listener.accept(event)
        }
    }
    
    override fun fireKeyReleaseEvent(event: KeyReleaseEvent) {
        for (listener in eventBus.keyReleaseListeners) {
            listener.accept(event)
        }
    }
    
    override fun fireCharTypeEvent(event: CharTypeEvent) {
        for (listener in eventBus.charTypeListeners) {
            listener.accept(event)
        }
    }
    
    override fun fireFocusEvent() {
        for (listener in eventBus.focusListeners) {
            listener.run()
        }

        if (hasParent && parent != this) {
            parent!!.fireFocusEvent()
        }
    }
    
    override fun fireUnfocusEvent() {
        for (listener in eventBus.unfocusListeners) {
            listener.run()
        }

        if (hasParent && parent != this) {
            parent!!.fireUnfocusEvent()
        }
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
        // Tell our platform that we're starting a new frame
        if (isRoot) {
            platform!!.renderer.startFrame()
        }

        if (!initialized) {
            // Initialize the component on the first possible frame to ensure that it has time to set up
            initialize()
            initialized = true
        }

        // Begin our render operations!
        preRender()
        effects.preRender()
        if (isRoot) {
            renderRoot()
        }

        render()
        renderChildren()
        postRender()
        effects.postRender()

        // Tell our engine that we're ending the frame
        if (isRoot) {
            platform!!.renderer.endFrame()
        }

        // Update mouse state
        if (isRoot) {
            val (x, y) = pointerPos
            handlePointerMove(x, y)
        }
    }

    public fun handlePointerMove(x: Double, y: Double) {
        val isHovered = isPointInside(x, y)
        val event = HoverEvent(this, x, y)
        val change = pointerTracker.handlePointerMove(x, y, isHovered)
        when (change) {
            PointerTracker.HoverStateChange.ENTERED -> fireHoverEvent(event)
            PointerTracker.HoverStateChange.EXITED -> fireUnhoverEvent(event)
            else -> {} // no-op
        }
    }

    public open fun handlePointerClick(x: Double, y: Double, button: MouseButton) {
        val clickedChild = hitTest(x, y)
        pointerTracker.handlePointerClick(x, y, button)
        clickedChild.firePointerClickEvent(PointerClickEvent(clickedChild, x, y, button, pointerTracker.currentClickCount))
        if (isRoot) {
            focusManager.handleRequests()
        }
    }

    public open fun handlePointerRelease() {
        firePointerReleaseEvent(PointerReleaseEvent(this, pointerTracker.lastDraggedX, pointerTracker.lastDraggedY, pointerTracker.lastClickedButton))
        pointerTracker.handlePointerRelease()
        children.forEach(Component<*, *>::handlePointerRelease)
    }

    public open fun handleMouseDrag(x: Double, y: Double) {
        if (
            pointerTracker.lastDraggedY == x ||
            pointerTracker.lastDraggedY == y
        ) {
            return
        }

        pointerTracker.updateDragPosition(x, y)
        firePointerDragEvent(PointerDragEvent(this, x, y, pointerTracker.lastClickedButton))
        children.forEach { child -> child.handleMouseDrag(x, y) }
    }

    public open fun handleMouseScroll(amount: Double) {
        if (amount == 0.0) {
            return
        }


        for (child in children.reversed()) {
            if (child.pointerTracker.isCurrentlyHovered) {
                child.handleMouseScroll(amount)
                return
            }
        }

        fireScrollEvent(ScrollEvent(this, amount))
    }

    public open fun handleKeyPress(key: Key, modifiers: KeyboardModifiers) {
        fireKeyPressEvent(KeyPressEvent(this, key, modifiers))

        if (isRoot && isFocused) {
            focusManager.currentlyFocused!!.handleKeyPress(key, modifiers)
        }
    }

    public open fun handleKeyRelease(key: Key, modifiers: KeyboardModifiers) {
        fireKeyReleaseEvent(KeyReleaseEvent(this, key, modifiers))

        if (isRoot && isFocused) {
            focusManager.currentlyFocused!!.handleKeyRelease(key, modifiers)
        }
    }

    public open fun handleCharType(char: Char, modifiers: KeyboardModifiers) {
        fireCharTypeEvent(CharTypeEvent(this, char, modifiers))

        if (isRoot && isFocused) {
            focusManager.currentlyFocused!!.handleCharType(char, modifiers)
        }
    }

    private fun renderRoot() {
        val platform = findPlatform(this)
        val targetFramerate = platform.targetFramerate
        val targetFrameTimeMs = 1_000L / targetFramerate

        if (systemTime == -1L) {
            systemTime = System.currentTimeMillis()
        }

        try {
            val currentTime = System.currentTimeMillis()

            // If we're too far behind, resync to avoid spiraling
            if (currentTime - systemTime > 2_500) {
                systemTime = currentTime
            }

            val timeToCatchUp = currentTime - systemTime
            val maxFrames = (targetFramerate / 30).coerceAtLeast(1)
            val frameCount = (timeToCatchUp / targetFrameTimeMs).toInt().coerceAtMost(maxFrames)

            repeat(frameCount) {
                val frameStartTime = systemTime
                systemTime += targetFrameTimeMs

                val nextTime = systemTime
                val delta = (nextTime - frameStartTime) / 1000f // in seconds
                animationFrame(delta)
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }


}
