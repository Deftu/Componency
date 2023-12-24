@file:Suppress(
    "MemberVisibilityCanBePrivate",
    "SpellCheckingInspection",
    "LeakingThis",
    "unused"
)

package dev.deftu.componency.components

import dev.deftu.componency.animations.ComponentAnimationConstraints
import dev.deftu.componency.constraints.ColorConstraint
import dev.deftu.componency.constraints.ComponentConstraints
import dev.deftu.componency.constraints.PixelConstraint
import dev.deftu.componency.dsl.animate
import dev.deftu.componency.effects.Effect
import dev.deftu.componency.events.*
import dev.deftu.componency.filters.Filter
import dev.deftu.componency.font.FontProvider
import dev.deftu.componency.utils.fixMousePos
import dev.deftu.multi.MultiKeyboard
import dev.deftu.multi.MultiMatrixStack
import dev.deftu.multi.MultiMouse
import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Predicate

abstract class BaseComponent {
    lateinit var parent: BaseComponent
    val hasParent: Boolean
        get() = ::parent.isInitialized
    private var indexInParent = -1
    internal val children = CopyOnWriteArrayList<BaseComponent>()

    var constraints = ComponentConstraints(this)
    internal val effects = mutableListOf<Effect>()
    internal val filters = mutableListOf<Filter>()
    private var hasInitialized = false

    private var currentlyHovered = false
    private var lastDraggedMouseX = 0.0
    private var lastDraggedMouseY = 0.0

    private val beforeHideAnimations = mutableListOf<ComponentAnimationConstraints.() -> Unit>()
    private val afterUnhideAnimations = mutableListOf<ComponentAnimationConstraints.() -> Unit>()
    private val focusGainListeners = mutableListOf<() -> Unit>()
    private val focusLostListeners = mutableListOf<() -> Unit>()

    private val mouseClickListeners = mutableListOf<(MouseClickEvent) -> Unit>()
    private val mouseReleaseListeners = mutableListOf<(MouseReleaseEvent) -> Unit>()
    private val mouseDragListeners = mutableListOf<(MouseDragEvent) -> Unit>()
    private val mouseScrollListeners = mutableListOf<(ComponentMouseScrollEvent) -> Unit>()
    private val mouseHoverListeners = mutableListOf<(MouseHoverEvent) -> Unit>()
    private val mouseUnhoverListeners = mutableListOf<(MouseHoverEvent) -> Unit>()
    private val keyPressListeners = mutableListOf<(KeyPressEvent) -> Unit>()

    abstract fun render(stack: MultiMatrixStack, tickDelta: Float)

    fun doRender(stack: MultiMatrixStack, tickDelta: Float) {
        if (!hasInitialized) {
            hasInitialized = true
            handleInitialize()
        }

        effects.forEach { it.applyPreRender(this, stack, tickDelta) }
        filters.forEach { it.applyPreRender(this, stack, tickDelta) }
        render(stack, tickDelta)

        val window = WindowComponent.find(this)
        children.forEach { child ->
            if (!window.isVisible(
                    child.getX(),
                    child.getY(),
                    child.getRight(),
                    child.getBottom()
                )) return@forEach
            child.doRender(stack, tickDelta)
        }

        if (this is WindowComponent) {
            floatingChildren.forEach { child ->
                if (!window.isVisible(
                        child.getX(),
                        child.getY(),
                        child.getRight(),
                        child.getBottom()
                    )) return@forEach
                child.doRender(stack, tickDelta)
            }
        }

        filters.forEach { it.applyPostRender(this, stack, tickDelta) }
        effects.forEach { it.applyPostRender(this, stack, tickDelta) }
    }

    fun destroy() {
        constraints.handleDestroy()
        filters.forEach { it.onDestroyed(this) }
        children.forEach { it.destroy() }
    }

    fun onWindowResize() {
        constraints.onWindowResize()
        children.forEach { it.onWindowResize() }
    }

    fun isHovered(): Boolean {
        val (fixedX, fixedY) = fixMousePos(MultiMouse.scaledX, MultiMouse.scaledY)
        return isInsideBounds(fixedX, fixedY)
    }

    fun isInsideBounds(x: Double, y: Double): Boolean {
        return x >= getX() && x <= getRight() && y >= getY() && y <= getBottom()
    }

    open fun isAlreadyCentered() = false

    open fun handleInitialize() {
    }

    // Hierarchical API

    fun attachChild(index: Int, child: BaseComponent) {
        child.parent = this
        children.add(index, child)
        child.indexInParent = index
    }

    fun attachChild(child: BaseComponent) {
        child.parent = this
        children.add(child)
        child.indexInParent = children.size - 1
    }

    fun attachChildren(vararg children: BaseComponent) {
        children.forEach { attachChild(it) }
    }

    fun detachChild(child: BaseComponent) {
        child.filters.forEach { it.onDestroyed(child) }
        child.parent = child
        children.remove(child)
    }

    fun detachChild(vararg children: BaseComponent) {
        children.forEach { detachChild(it) }
    }

    open fun findTarget(x: Double, y: Double): BaseComponent {
        for (i in children.lastIndex downTo 0) {
            val child = children[i]
            if (child.isInsideBounds(x, y)) return child.findTarget(x, y)
        }

        return this
    }

    fun isChildOf(component: BaseComponent): Boolean {
        if (!hasParent) return false
        if (parent == component) return true
        return parent.isChildOf(component)
    }

    fun isInHierarchy(component: BaseComponent): Boolean {
        var current = this
        while (current.hasParent && current !is WindowComponent) {
            current = current.parent
            if (current == component) return true
        }

        return false
    }

    // Hide API

    fun hideInstantly() {
        indexInParent = parent.children.indexOf(this)
        parent.detachChild(this)
    }

    fun hide(instant: Boolean = false) {
        if (instant) {
            hideInstantly()
            return
        }

        animate {
            for (animation in beforeHideAnimations) animation()

            whenComplete {
                hideInstantly()
            }
        }
    }

    fun unhide(useLastPos: Boolean = true) {
        if (parent.children.contains(this)) return

        if (useLastPos && indexInParent >= 0 && indexInParent < parent.children.size) {
            parent.attachChild(indexInParent, this)
        } else {
            parent.attachChild(this)
        }

        animate {
            for (animation in afterUnhideAnimations) animation()
        }
    }

    fun animateBeforeHide(animation: ComponentAnimationConstraints.() -> Unit) = apply {
        beforeHideAnimations.add(animation)
    }

    fun animateAfterUnhide(animation: ComponentAnimationConstraints.() -> Unit) = apply {
        afterUnhideAnimations.add(animation)
    }

    // Listener API

    open fun mouseClick(x: Double, y: Double, button: Int): Boolean {
        lastDraggedMouseX = x
        lastDraggedMouseY = y

        if (isHovered()) {
            val event = MouseClickEvent(x, y, button)
            var result = false
            for (listener: (MouseClickEvent) -> Unit in mouseClickListeners) {
                listener(event)
                result = event.isCancelled || result
                if (!event.isBubbling) break
            }

            if (event.isBubbling) {
                for (child in children) {
                    result = child.mouseClick(x, y, button) || result
                }
            }

            return result
        }

        return false
    }

    open fun mouseRelease(x: Double, y: Double, button: Int): Boolean {
        if (!isHovered()) return false

        val event = MouseReleaseEvent(x, y, button)
        var result = false
        for (listener in mouseReleaseListeners) {
            listener(event)
            result = event.isCancelled || result
            if (!event.isBubbling) break
        }

        lastDraggedMouseX = 0.0
        lastDraggedMouseY = 0.0

        if (event.isBubbling) {
            for (child in children) {
                result = child.mouseRelease(x, y, button) || result
            }
        }

        return result
    }

    open fun mouseUpdate(x: Double, y: Double) {
        // TODO - Check for floating components
        val hovered = isHovered()

        if (hovered && !currentlyHovered) {
            mouseHoverListeners.forEach { listener ->
                listener(MouseHoverEvent(x, y))
            }
        } else if (!hovered && currentlyHovered) {
            mouseUnhoverListeners.forEach { listener ->
                listener(MouseHoverEvent(x, y))
            }
        }

        children.forEach { it.mouseUpdate(x, y) }
    }

    open fun mouseDrag(x: Double, y: Double, button: Int) {
    }

    open fun mouseScroll(x: Double, y: Double, scrollDelta: Double): Boolean {
        if (scrollDelta == 0.0) return false

        val event = ComponentMouseScrollEvent(x, y, scrollDelta)
        var result = false
        for (listener in mouseScrollListeners) {
            listener(event)
            result = event.isCancelled || result
            if (!event.isBubbling) break
        }

        if (event.isBubbling) {
            for (child in children) {
                if (!child.isHovered()) continue

                result = child.mouseScroll(x, y, scrollDelta) || result
            }
        }

        return result
    }

    open fun keyPress(keyCode: Int, typedChar: Char, modifiers: MultiKeyboard.KeyboardModifiers): Boolean {
        val event = KeyPressEvent(keyCode, typedChar, modifiers)
        var result = false
        for (listener in keyPressListeners) {
            listener(event)
            result = event.isCancelled || result
            if (!event.isBubbling) break
        }

        if (event.isBubbling) {
            for (child in children) {
                result = child.keyPress(keyCode, typedChar, modifiers) || result
            }
        }

        return result
    }

    // Event API

    fun addMouseClickListener(listener: (MouseClickEvent) -> Unit) = apply {
        mouseClickListeners.add(listener)
    }

    fun addMouseClickListener(listener: Predicate<MouseClickEvent>) = apply {
        addMouseClickListener(listener::test)
    }

    fun addMouseLeftClickListener(listener: (MouseClickEvent) -> Unit) = apply {
        addMouseClickListener { event ->
            if (event.button == MultiMouse.LEFT) listener(event)
        }
    }

    fun addMouseLeftClickListener(listener: Predicate<MouseClickEvent>) = apply {
        addMouseLeftClickListener(listener::test)
    }

    fun addMouseRightClickListener(listener: (MouseClickEvent) -> Unit) = apply {
        addMouseClickListener { event ->
            if (event.button == MultiMouse.RIGHT) listener(event)
        }
    }

    fun addMouseRightClickListener(listener: Predicate<MouseClickEvent>) = apply {
        addMouseRightClickListener(listener::test)
    }

    fun addMouseMiddleClickListener(listener: (MouseClickEvent) -> Unit) = apply {
        addMouseClickListener { event ->
            if (event.button == MultiMouse.MIDDLE) listener(event)
        }
    }

    fun addMouseMiddleClickListener(listener: Predicate<MouseClickEvent>) = apply {
        addMouseMiddleClickListener(listener::test)
    }

    fun addMouseReleaseListener(listener: (MouseReleaseEvent) -> Unit) = apply {
        mouseReleaseListeners.add(listener)
    }

    fun addMouseReleaseListener(listener: Predicate<MouseReleaseEvent>) = apply {
        addMouseReleaseListener(listener::test)
    }

    fun addMouseLeftReleaseListener(listener: (MouseReleaseEvent) -> Unit) = apply {
        addMouseReleaseListener { event ->
            if (event.button == MultiMouse.LEFT) listener(event)
        }
    }

    fun addMouseLeftReleaseListener(listener: Predicate<MouseReleaseEvent>) = apply {
        addMouseLeftReleaseListener(listener::test)
    }

    fun addMouseRightReleaseListener(listener: (MouseReleaseEvent) -> Unit) = apply {
        addMouseReleaseListener { event ->
            if (event.button == MultiMouse.RIGHT) listener(event)
        }
    }

    fun addMouseRightReleaseListener(listener: Predicate<MouseReleaseEvent>) = apply {
        addMouseRightReleaseListener(listener::test)
    }

    fun addMouseMiddleReleaseListener(listener: (MouseReleaseEvent) -> Unit) = apply {
        addMouseReleaseListener { event ->
            if (event.button == MultiMouse.MIDDLE) listener(event)
        }
    }

    fun addMouseMiddleReleaseListener(listener: Predicate<MouseReleaseEvent>) = apply {
        addMouseMiddleReleaseListener(listener::test)
    }

    fun addMouseDragListener(listener: (MouseDragEvent) -> Unit) = apply {
        mouseDragListeners.add(listener)
    }

    fun addMouseDragListener(listener: Predicate<MouseDragEvent>) = apply {
        mouseDragListeners.add(listener::test)
    }

    fun addMouseLeftDragListener(listener: (MouseDragEvent) -> Unit) = apply {
        addMouseDragListener { event ->
            if (event.button == MultiMouse.LEFT) listener(event)
        }
    }

    fun addMouseLeftDragListener(listener: Predicate<MouseDragEvent>) = apply {
        addMouseLeftDragListener(listener::test)
    }

    fun addMouseRightDragListener(listener: (MouseDragEvent) -> Unit) = apply {
        addMouseDragListener { event ->
            if (event.button == MultiMouse.RIGHT) listener(event)
        }
    }

    fun addMouseRightDragListener(listener: Predicate<MouseDragEvent>) = apply {
        addMouseRightDragListener(listener::test)
    }

    fun addMouseMiddleDragListener(listener: (MouseDragEvent) -> Unit) = apply {
        addMouseDragListener { event ->
            if (event.button == MultiMouse.MIDDLE) listener(event)
        }
    }

    fun addMouseMiddleDragListener(listener: Predicate<MouseDragEvent>) = apply {
        addMouseMiddleDragListener(listener::test)
    }

    fun addMouseScrollListener(listener: (ComponentMouseScrollEvent) -> Unit) = apply {
        mouseScrollListeners.add(listener)
    }

    fun addMouseScrollListener(listener: Predicate<ComponentMouseScrollEvent>) = apply {
        addMouseScrollListener(listener::test)
    }

    fun addMouseHoverListener(listener: (MouseHoverEvent) -> Unit) = apply {
        mouseHoverListeners.add(listener)
    }

    fun addMouseUnhoverListener(listener: (MouseHoverEvent) -> Unit) = apply {
        mouseUnhoverListeners.add(listener)
    }

    fun addKeyPressListener(listener: (KeyPressEvent) -> Unit) = apply {
        keyPressListeners.add(listener)
    }

    fun addKeyPressListener(listener: Predicate<KeyPressEvent>) = apply {
        addKeyPressListener(listener::test)
    }

    // Animation API

    fun startAnimating(constraints: ComponentAnimationConstraints) {
        this.constraints = constraints
    }

    open fun handleAnimate() {
        constraints.handleAnimate()

        effects.forEach { it.handleAnimate(this) }
        filters.forEach { it.handleAnimate(this) }

        children.forEach { it.handleAnimate() }
    }

    // Effects API

    open fun applyEffect(effect: Effect) = apply {
        effects.add(effect)
    }

    open fun removeEffect(effect: Effect) = apply {
        effects.remove(effect)
    }

    // Filter API

    open fun applyFilter(filter: Filter) = apply {
        filters.add(filter)
    }

    open fun removeFilter(filter: Filter) = apply {
        filters.remove(filter)
    }

    // Focus API

    fun handleFocusGained() {
        focusGainListeners.forEach { it() }
    }

    fun handleFocusLost() {
        focusLostListeners.forEach { it() }
    }

    fun requestFocus() = WindowComponent.find(this).focus(this)
    fun releaseFocus() = WindowComponent.find(this).unfocus()

    fun isFocused() = WindowComponent.find(this).isFocused(this)
    
    fun onFocusGained(listener: () -> Unit) = apply {
        focusGainListeners.add(listener)
    }
    
    fun onFocusGained(listener: Runnable) = apply {
        focusGainListeners.add(listener::run)
    }
    
    fun onFocusLost(listener: () -> Unit) = apply {
        focusLostListeners.add(listener)
    }
    
    fun onFocusLost(listener: Runnable) = apply {
        focusLostListeners.add(listener::run)
    }

    // Floating API

    fun setFloating(floating: Boolean) {
        val window = WindowComponent.find(this)
        if (floating) {
            window.floatingChildren.add(this)
            window.children.remove(this)
        } else {
            window.children.add(this)
            window.floatingChildren.remove(this)
        }
    }

    fun isFloating() = WindowComponent.find(this).floatingChildren.contains(this)

    // Constraints

    open fun getX() = constraints.getX()
    open fun getY() = constraints.getY()
    open fun getWidth() = constraints.getWidth()
    open fun getHeight() = constraints.getHeight()
    open fun getRight() = getX() + getWidth()
    open fun getBottom() = getY() + getHeight()
    open fun getFontProvider() = constraints.fontProvider
    open fun getRadius() = constraints.getRadius()
    open fun getColor() = constraints.getColor()

    open fun setX(x: PixelConstraint) {
        constraints.x = x
    }

    open fun setY(y: PixelConstraint) {
        constraints.y = y
    }

    open fun setWidth(width: PixelConstraint) {
        constraints.width = width
    }

    open fun setHeight(height: PixelConstraint) {
        constraints.height = height
    }

    open fun setFontProvider(fontProvider: FontProvider) {
        constraints.fontProvider = fontProvider
    }

    open fun setRadius(radius: PixelConstraint) {
        constraints.radius = radius
    }

    open fun setColor(color: ColorConstraint) {
        constraints.color = color
    }

    fun getWindow() = WindowComponent.find(this)
}
