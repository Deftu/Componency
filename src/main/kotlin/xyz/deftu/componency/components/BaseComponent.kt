package xyz.deftu.componency.components

import xyz.deftu.componency.animations.ComponentAnimationConstraints
import xyz.deftu.componency.constraints.ColorConstraint
import xyz.deftu.componency.constraints.ComponentConstraints
import xyz.deftu.componency.constraints.PixelConstraint
import xyz.deftu.componency.dsl.animate
import xyz.deftu.componency.effects.Effect
import xyz.deftu.componency.effects.OutlineEffect
import xyz.deftu.componency.events.*
import xyz.deftu.componency.filters.Filter
import xyz.deftu.componency.filters.GaussianBlurFilter
import xyz.deftu.componency.font.FontProvider
import xyz.deftu.componency.utils.fixMousePos
import xyz.deftu.multi.MultiKeyboard
import xyz.deftu.multi.MultiMatrixStack
import xyz.deftu.multi.MultiMouse
import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Consumer
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

    private val mouseClickListeners = mutableListOf<ComponentMouseClickEvent.() -> Boolean>()
    private val mouseReleaseListeners = mutableListOf<ComponentMouseReleaseEvent.() -> Boolean>()
    private val mouseDragListeners = mutableListOf<ComponentMouseDragEvent.() -> Boolean>()
    private val mouseScrollListeners = mutableListOf<ComponentMouseScrollEvent.() -> Boolean>()
    private val mouseHoverListeners = mutableListOf<(x: Double, y: Double) -> Boolean>()
    private val mouseUnhoverListeners = mutableListOf<(x: Double, y: Double) -> Boolean>()
    private val keyPressListeners = mutableListOf<ComponentKeyPressEvent.() -> Boolean>()

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
        OutlineEffect.resize()
        GaussianBlurFilter.resize()

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
            val event = ComponentMouseClickEvent(x, y, button)
            var result = false
            for (listener in mouseClickListeners) {
                result = listener(event) || result
                if (event.isCancelled) break
            }

            for (child in children) {
                result = child.mouseClick(x, y, button) || result
            }

            return result
        }

        return false
    }

    open fun mouseRelease(x: Double, y: Double, button: Int): Boolean {
        if (!isHovered()) return false

        val event = ComponentMouseReleaseEvent(x, y, button)
        var result = false
        for (listener in mouseReleaseListeners) {
            result = listener(event) || result
            if (event.isCancelled) break
        }

        lastDraggedMouseX = 0.0
        lastDraggedMouseY = 0.0

        for (child in children) {
            result = child.mouseRelease(x, y, button) || result
        }

        return result
    }

    open fun mouseUpdate(x: Double, y: Double) {
        // TODO - Check for floating components
        val hovered = isHovered()

        if (hovered && !currentlyHovered) {
            mouseHoverListeners.forEach { if (it(x, y)) return }
        } else if (!hovered && currentlyHovered) {
            mouseUnhoverListeners.forEach { if (it(x, y)) return }
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
            result = listener(event) || result
            if (event.isCancelled) break
        }

        for (i in children.lastIndex downTo 0) {
            val child = children[i]
            if (child.isHovered()) {
                return child.mouseScroll(x, y, scrollDelta)
            }
        }

        return result
    }

    open fun keyPress(keyCode: Int, typedChar: Char, modifiers: MultiKeyboard.KeyboardModifiers): Boolean {
        val event = ComponentKeyPressEvent(keyCode, typedChar, modifiers)
        var result = false
        for (listener in keyPressListeners) {
            result = listener(event) || result
            if (event.isCancelled) break
        }

        for (child in children) {
            result = child.keyPress(keyCode, typedChar, modifiers) || result
        }

        return result
    }

    // Event API

    fun onMouseClick(listener: ComponentMouseClickEvent.() -> Boolean) = apply {
        mouseClickListeners.add(listener)
    }

    fun onMouseClick(listener: Predicate<ComponentMouseClickEvent>) = apply {
        onMouseClick(listener::test)
    }

    fun onMouseLeftClick(listener: ComponentMouseClickEvent.() -> Boolean) = apply {
        onMouseClick {
            if (button == MultiMouse.LEFT) listener(this) else false
        }
    }

    fun onMouseLeftClick(listener: Predicate<ComponentMouseClickEvent>) = apply {
        onMouseLeftClick(listener::test)
    }

    fun onMouseRightClick(listener: ComponentMouseClickEvent.() -> Boolean) = apply {
        onMouseClick {
            if (button == MultiMouse.RIGHT) listener(this) else false
        }
    }

    fun onMouseRightClick(listener: Predicate<ComponentMouseClickEvent>) = apply {
        onMouseRightClick(listener::test)
    }

    fun onMouseMiddleClick(listener: ComponentMouseClickEvent.() -> Boolean) = apply {
        onMouseClick {
            if (button == MultiMouse.MIDDLE) listener(this) else false
        }
    }

    fun onMouseMiddleClick(listener: Predicate<ComponentMouseClickEvent>) = apply {
        onMouseMiddleClick(listener::test)
    }

    fun onMouseRelease(listener: ComponentMouseReleaseEvent.() -> Boolean) = apply {
        mouseReleaseListeners.add(listener)
    }

    fun onMouseRelease(listener: Predicate<ComponentMouseReleaseEvent>) = apply {
        onMouseRelease(listener::test)
    }

    fun onMouseLeftRelease(listener: ComponentMouseReleaseEvent.() -> Boolean) = apply {
        onMouseRelease {
            if (button == MultiMouse.LEFT) listener(this) else false
        }
    }

    fun onMouseLeftRelease(listener: Predicate<ComponentMouseReleaseEvent>) = apply {
        onMouseLeftRelease(listener::test)
    }

    fun onMouseRightRelease(listener: ComponentMouseReleaseEvent.() -> Boolean) = apply {
        onMouseRelease {
            if (button == MultiMouse.RIGHT) listener(this) else false
        }
    }

    fun onMouseRightRelease(listener: Predicate<ComponentMouseReleaseEvent>) = apply {
        onMouseRightRelease(listener::test)
    }

    fun onMouseMiddleRelease(listener: ComponentMouseReleaseEvent.() -> Boolean) = apply {
        onMouseRelease {
            if (button == MultiMouse.MIDDLE) listener(this) else false
        }
    }

    fun onMouseMiddleRelease(listener: Predicate<ComponentMouseReleaseEvent>) = apply {
        onMouseMiddleRelease(listener::test)
    }

    fun onMouseDrag(listener: ComponentMouseDragEvent.() -> Boolean) = apply {
        mouseDragListeners.add(listener)
    }

    fun onMouseDrag(listener: Predicate<ComponentMouseDragEvent>) = apply {
        mouseDragListeners.add(listener::test)
    }

    fun onMouseLeftDrag(listener: ComponentMouseDragEvent.() -> Boolean) = apply {
        onMouseDrag {
            if (button == MultiMouse.LEFT) listener(this) else false
        }
    }

    fun onMouseLeftDrag(listener: Predicate<ComponentMouseDragEvent>) = apply {
        onMouseLeftDrag(listener::test)
    }

    fun onMouseRightDrag(listener: ComponentMouseDragEvent.() -> Boolean) = apply {
        onMouseDrag {
            if (button == MultiMouse.RIGHT) listener(this) else false
        }
    }

    fun onMouseRightDrag(listener: Predicate<ComponentMouseDragEvent>) = apply {
        onMouseRightDrag(listener::test)
    }

    fun onMouseMiddleDrag(listener: ComponentMouseDragEvent.() -> Boolean) = apply {
        onMouseDrag {
            if (button == MultiMouse.MIDDLE) listener(this) else false
        }
    }

    fun onMouseMiddleDrag(listener: Predicate<ComponentMouseDragEvent>) = apply {
        onMouseMiddleDrag(listener::test)
    }

    fun onMouseScroll(listener: ComponentMouseScrollEvent.() -> Boolean) = apply {
        mouseScrollListeners.add(listener)
    }

    fun onMouseScroll(listener: Predicate<ComponentMouseScrollEvent>) = apply {
        onMouseScroll(listener::test)
    }

    fun onMouseHover(listener: (x: Double, y: Double) -> Boolean) = apply {
        mouseHoverListeners.add(listener)
    }

    fun onMouseUnhover(listener: (x: Double, y: Double) -> Boolean) = apply {
        mouseUnhoverListeners.add(listener)
    }

    fun onKeyPress(listener: ComponentKeyPressEvent.() -> Boolean) = apply {
        keyPressListeners.add(listener)
    }

    fun onKeyPress(listener: Predicate<ComponentKeyPressEvent>) = apply {
        onKeyPress(listener::test)
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
