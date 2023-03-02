package xyz.deftu.componency.components

import xyz.deftu.componency.animations.ComponentAnimationConstraints
import xyz.deftu.componency.constraints.ColorConstraint
import xyz.deftu.componency.constraints.ComponentConstraints
import xyz.deftu.componency.constraints.PixelConstraint
import xyz.deftu.componency.dsl.animate
import xyz.deftu.componency.effects.Effect
import xyz.deftu.componency.filters.Filter
import xyz.deftu.componency.font.FontProvider
import xyz.deftu.multi.MultiMatrixStack
import xyz.deftu.multi.MultiMouse
import java.util.concurrent.CopyOnWriteArrayList

abstract class BaseComponent {
    lateinit var parent: BaseComponent
    private var indexInParent = -1
    internal val children = CopyOnWriteArrayList<BaseComponent>()

    var constraints = ComponentConstraints(this)
    internal val effects = mutableListOf<Effect>()
    internal val filters = mutableListOf<Filter>()
    private var hasInitialized = false

    private var currentlyHovered = false
    private var lastDraggedMouseX = 0f
    private var lastDraggedMouseY = 0f

    private val beforeHideAnimations = mutableListOf<ComponentAnimationConstraints.() -> Unit>()
    private val afterUnhideAnimations = mutableListOf<ComponentAnimationConstraints.() -> Unit>()

    private val mouseClickListeners = mutableListOf<(x: Float, y: Float, button: Int) -> Boolean>()
    private val mouseReleaseListeners = mutableListOf<(x: Float, y: Float, button: Int) -> Boolean>()
    private val mouseDragListeners = mutableListOf<(x: Float, y: Float, button: Int) -> Boolean>()
    private val mouseScrollListeners = mutableListOf<(x: Float, y: Float, scrollDelta: Double) -> Boolean>()
    private val mouseHoverListeners = mutableListOf<(x: Float, y: Float) -> Boolean>()
    private val mouseUnhoverListeners = mutableListOf<(x: Float, y: Float) -> Boolean>()
    private val keyPressListeners = mutableListOf<(keyCode: Int, typedChar: Char) -> Boolean>()

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
                child.render(stack, tickDelta)
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
        val mouseX = MultiMouse.scaledX
        val mouseY = MultiMouse.scaledY
        return mouseX >= getX() && mouseX <= getRight() && mouseY >= getY() && mouseY <= getBottom()
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

    fun mouseClick(x: Float, y: Float, button: Int) {
        lastDraggedMouseX = x
        lastDraggedMouseY = y

        mouseClickListeners.forEach { if (it(x, y, button)) return }
        children.forEach { it.mouseClick(x, y, button) }
    }

    fun mouseRelease(x: Float, y: Float, button: Int) {
        mouseReleaseListeners.forEach { if (it(x, y, button)) return }

        lastDraggedMouseX = 0f
        lastDraggedMouseY = 0f

        children.forEach { it.mouseRelease(x, y, button) }
    }

    fun mouseMove(x: Float, y: Float) {
        // TODO - Check for floating components
        val hovered = isHovered()

        if (hovered && !currentlyHovered) {
            mouseHoverListeners.forEach { if (it(x, y)) return }
        } else if (!hovered && currentlyHovered) {
            mouseUnhoverListeners.forEach { if (it(x, y)) return }
        }

        children.forEach { it.mouseMove(x, y) }
    }

    fun mouseScroll(x: Float, y: Float, scrollDelta: Double) {
        if (scrollDelta == 0.0) return

        for (i in children.lastIndex downTo 0) {
            val child = children[i]
            if (child.isHovered()) {
                child.mouseScroll(x, y, scrollDelta)
                return
            }
        }

        mouseScrollListeners.forEach { if (it(x, y, scrollDelta)) return }
    }

    fun keyType(keyCode: Int, typedChar: Char) {
        keyPressListeners.forEach { if (it(keyCode, typedChar)) return }
        children.forEach { it.keyType(keyCode, typedChar) }
    }

    // Event API

    fun onMouseClick(listener: (x: Float, y: Float, button: Int) -> Boolean) = apply {
        mouseClickListeners.add(listener)
    }

    fun onMouseLeftClick(listener: (x: Float, y: Float) -> Boolean) = apply {
        mouseClickListeners.add { x, y, button -> if (button == 0) listener(x, y) else true }
    }

    fun onMouseRightClick(listener: (x: Float, y: Float) -> Boolean) = apply {
        mouseClickListeners.add { x, y, button -> if (button == 1) listener(x, y) else true }
    }

    fun onMouseMiddleClick(listener: (x: Float, y: Float) -> Boolean) = apply {
        mouseClickListeners.add { x, y, button -> if (button == 2) listener(x, y) else true }
    }

    fun onMouseRelease(listener: (x: Float, y: Float, button: Int) -> Boolean) = apply {
        mouseReleaseListeners.add(listener)
    }

    fun onMouseLeftRelease(listener: (x: Float, y: Float) -> Boolean) = apply {
        mouseReleaseListeners.add { x, y, button -> if (button == 0) listener(x, y) else true }
    }

    fun onMouseRightRelease(listener: (x: Float, y: Float) -> Boolean) = apply {
        mouseReleaseListeners.add { x, y, button -> if (button == 1) listener(x, y) else true }
    }

    fun onMouseMiddleRelease(listener: (x: Float, y: Float) -> Boolean) = apply {
        mouseReleaseListeners.add { x, y, button -> if (button == 2) listener(x, y) else true }
    }

    fun onMouseDrag(listener: (x: Float, y: Float, button: Int) -> Boolean) = apply {
        mouseDragListeners.add(listener)
    }

    fun onMouseLeftDrag(listener: (x: Float, y: Float) -> Boolean) = apply {
        mouseDragListeners.add { x, y, button -> if (button == 0) listener(x, y) else true }
    }

    fun onMouseRightDrag(listener: (x: Float, y: Float) -> Boolean) = apply {
        mouseDragListeners.add { x, y, button -> if (button == 1) listener(x, y) else true }
    }

    fun onMouseMiddleDrag(listener: (x: Float, y: Float) -> Boolean) = apply {
        mouseDragListeners.add { x, y, button -> if (button == 2) listener(x, y) else true }
    }

    fun onMouseScroll(listener: (x: Float, y: Float, scrollDelta: Double) -> Boolean) = apply {
        mouseScrollListeners.add(listener)
    }

    fun onMouseHover(listener: (x: Float, y: Float) -> Boolean) = apply {
        mouseHoverListeners.add(listener)
    }

    fun onMouseUnhover(listener: (x: Float, y: Float) -> Boolean) = apply {
        mouseUnhoverListeners.add(listener)
    }

    fun onKeyPress(listener: (keyCode: Int, typedChar: Char) -> Boolean) = apply {
        keyPressListeners.add(listener)
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
