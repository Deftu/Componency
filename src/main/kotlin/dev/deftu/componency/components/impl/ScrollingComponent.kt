package dev.deftu.componency.components.impl

import dev.deftu.componency.animations.Easings
import dev.deftu.componency.components.Component
import dev.deftu.componency.components.events.MouseScrollEvent
import dev.deftu.componency.dsl.*
import dev.deftu.componency.effects.impl.ScissorEffect
import dev.deftu.componency.engine.Engine
import dev.deftu.componency.input.Key
import dev.deftu.componency.input.MouseButton
import dev.deftu.componency.properties.SizingProperty
import dev.deftu.componency.properties.impl.ContentHuggingProperty
import dev.deftu.componency.properties.impl.LargestChildProperty
import dev.deftu.componency.properties.impl.ParentRelativeProperty
import kotlin.math.abs

/**
 * Loosely based off of the [ScrollComponent](https://github.com/EssentialGG/Elementa/blob/f66a554070639eb5acd96507d003529f8cb3f459/src/main/kotlin/gg/essential/elementa/components/ScrollComponent.kt) from [Elementa](hhttps://github.com/EssentialGG/Elementa) under the [LGPL 3.0 License](https://www.gnu.org/licenses/lgpl-3.0.html).
 *
 * [LICENSE](https://github.com/EssentialGG/Elementa/blob/f66a554070639eb5acd96507d003529f8cb3f459/LICENSE)
 */
public class ScrollingComponent @JvmOverloads constructor(
    private val innerPadding: Float = 0f,
    private val directions: Set<ScrollDirection> = setOf(ScrollDirection.VERTICAL),
    directionPreference: ScrollDirection = directions.first(),
    private val scrollDistance: SizingProperty = 15.px,
    private val scrollAcceleration: Float = 1f
) : Component() {

    public enum class ScrollDirection {
        VERTICAL,
        HORIZONTAL
    }

    private inner class ScrollingConstraint : SizingProperty {

        override var cachedValue: Float = 0f
        override var needsRecalculate: Boolean = true

        private val sumOfProperty = ContentHuggingProperty()
        private val maxOfProperty = LargestChildProperty()

        override fun calculateWidth(component: Component): Float {
            val property = if (directions.contains(ScrollDirection.HORIZONTAL)) {
                sumOfProperty
            } else {
                maxOfProperty
            }

            return property.calculateWidth(component)
        }

        override fun calculateHeight(component: Component): Float {
            val property = if (directions.contains(ScrollDirection.VERTICAL)) {
                sumOfProperty
            } else {
                maxOfProperty
            }

            return property.calculateHeight(component)
        }

    }

    private class GripMinSizeProperty(private val desiredSize: SizingProperty) : SizingProperty {

        override var cachedValue: Float = 0f
        override var needsRecalculate: Boolean = true

        override fun frame() {
            super.frame()
            desiredSize.frame()
        }

        override fun calculateWidth(component: Component): Float {
            val parent = component.parent ?: return 0f
            val minimumWidthPercentage = if (parent.width < 200) 0.15f else 0.1f
            val minimumWidth = parent.width * minimumWidthPercentage

            return desiredSize.getWidth(component).coerceAtLeast(minimumWidth)
        }

        override fun calculateHeight(component: Component): Float {
            val parent = component.parent ?: return 0f
            val minimumHeightPercentage = if (parent.height < 200) 0.15f else 0.1f
            val minimumHeight = parent.height * minimumHeightPercentage

            return desiredSize.getHeight(component).coerceAtLeast(minimumHeight)
        }

    }

    /**
     * Returns the preferred direction, otherwise the first direction in the set.
     */
    private val primaryDirection: ScrollDirection =
        if (directions.contains(directionPreference)) {
            directionPreference
        } else {
            directions.first()
        }

    /**
     * Returns the secondary direction, which is used when shift is held down.
     *
     * If the directions set only contains the preferred direction, the first direction in the set is returned. Otherwise, the first non-preferred direction is returned.
     */
    private val secondaryDirection: ScrollDirection =
        if (directions.size == 1) {
            directions.first()
        } else {
            directions.first { it != primaryDirection }
        }

    private val contentHolder = FrameComponent().configure {
        name = "content_holder"

        properties {
            x = innerPadding.px
            y = innerPadding.px
            width = 100.percent - innerPadding.px
            height = 100.percent
        }
    }

    private var cachedAnimationFps: Int = 1

    private var oldWidth: Float = 0f
    private var oldHeight: Float = 0f
    private var oldContentWidth: Float = 0f
    private var oldContentHeight: Float = 0f
    private var needsUpdate = false

    private var currentScrollAcceleration = 1f
    private var verticalOffset = 0f
    private var horizontalOffset = 0f

    private var verticalGripComponent: Component? = null
    private var hideVerticalGripIfUseless = true
    private var verticalGripStart = -1f

    private var horizontalGripComponent: Component? = null
    private var hideHorizontalGripIfUseless = true
    private var horizontalGripStart = -1f

    private var autoScrollIndicatorComponent: Component? = null
    private var isAutoScrolling = false
    private var autoScrollStart = -1f to -1f

    private val scrollListener: MouseScrollEvent.() -> Unit = {
        val engine = Engine.get(component)
        val direction = if (engine.inputEngine.isKeyDown(Key.KEY_LSHIFT)) secondaryDirection else primaryDirection
        if (handleScroll(delta.toFloat(), direction)) {
            stopBubbling()
        }
    }

    public val verticalOverhang: Float
        get() = maxOf(0f, calculateContentHeight() - height)

    public val horizontalOverhang: Float
        get() = maxOf(0f, calculateContentWidth() - width)

    init {
        super.addChild(contentHolder)

        configure {
            properties {
                width = ScrollingConstraint().coerceAtMost(100.percentOfRoot)
                height = ScrollingConstraint().coerceAtMost(100.percentOfRoot)
            }

            effects {
                +ScissorEffect()
            }
        }.whenMouseClick {
            if (isAutoScrolling) {
                isAutoScrolling = false
                autoScrollStart = -1f to -1f

                autoScrollIndicatorComponent?.hide()
                return@whenMouseClick
            }

            if (button != MouseButton.MIDDLE) {
                return@whenMouseClick
            }

            isAutoScrolling = true
            autoScrollStart = relativeX.toFloat() to relativeY.toFloat()

            autoScrollIndicatorComponent?.show(useLastPosition = false)
            autoScrollIndicatorComponent?.configure {
                properties {
                    x = relativeX.px - simpleXProperty { component -> component.width / 2 }
                    y = relativeY.px - simpleYProperty { component -> component.height / 2 }
                }
            }
        }.whenMouseScroll(scrollListener)
    }

    override fun initialize() {
        cachedAnimationFps = Engine.get(this).renderEngine.animationFps
    }

    override fun render() {
        if (width != oldWidth || height != oldHeight) {
            oldWidth = width
            oldHeight = height

            needsUpdate = true
        }

        val contentWidth = calculateContentWidth()
        val contentHeight = calculateContentHeight()
        if (contentWidth != oldContentWidth || contentHeight != oldContentHeight) {
            oldContentWidth = contentWidth
            oldContentHeight = contentHeight

            needsUpdate = true
        }

        if (!needsUpdate) {
            return
        }

        needsUpdate = false

        val verticalRange = calculateOffset(ScrollDirection.VERTICAL)
        val horizontalRange = calculateOffset(ScrollDirection.HORIZONTAL)
        verticalOffset = if (verticalRange.isEmpty()) innerPadding else verticalOffset.coerceIn(verticalRange)
        horizontalOffset = if (horizontalRange.isEmpty()) innerPadding else horizontalOffset.coerceIn(horizontalRange)

        contentHolder.animate {
            animateX(
                easing = Easings.IN_SINE,
                duration = 100.millis,
                newValue = horizontalOffset.px
            )

            animateY(
                easing = Easings.IN_SINE,
                duration = 100.millis,
                newValue = verticalOffset.px
            )
        }

        if (verticalGripComponent != null) {
            val percent = (innerPadding - verticalOffset) / verticalRange.width()
            val percentOfParent = height / contentHeight
            updateGripPosition(percent, percentOfParent, ScrollDirection.VERTICAL)
        }

        if (horizontalGripComponent != null) {
            val percent = (innerPadding - horizontalOffset) / horizontalRange.width()
            val percentOfParent = width / contentWidth
            updateGripPosition(percent, percentOfParent, ScrollDirection.HORIZONTAL)
        }
    }

    override fun frame() {
        super.frame()

        currentScrollAcceleration = (currentScrollAcceleration - ((currentScrollAcceleration - 1f) / cachedAnimationFps.toFloat())).coerceAtLeast(1f)

        if (isAutoScrolling && autoScrollStart != -1f to -1f) {
            if (directions.contains(ScrollDirection.VERTICAL)) {
                val yBegin = autoScrollStart.second + top
                val currentY = engine!!.inputEngine.mouseY

                if (currentY in top..bottom) {
                    val deltaY = currentY - yBegin
                    val percentY = deltaY / (-height / 2)
                    verticalOffset += percentY * 5f
                    needsUpdate = true
                }
            }

            if (directions.contains(ScrollDirection.HORIZONTAL)) {
                val xBegin = autoScrollStart.first + left
                val currentX = engine!!.inputEngine.mouseX

                if (currentX in left..right) {
                    val deltaX = currentX - xBegin
                    val percentX = deltaX / (-width / 2)
                    horizontalOffset += percentX * 5f
                    needsUpdate = true
                }
            }
        }
    }

    @JvmOverloads
    public fun setGripComponent(
        component: Component,
        direction: ScrollDirection,
        hideIfUseless: Boolean = true
    ): ScrollingComponent = apply {
        when (direction) {
            ScrollDirection.VERTICAL -> {
                verticalGripComponent = component
                hideVerticalGripIfUseless = hideIfUseless
            }

            ScrollDirection.HORIZONTAL -> {
                horizontalGripComponent = component
                hideHorizontalGripIfUseless = hideIfUseless
            }
        }

        component.whenMouseClick {
            when (direction) {
                ScrollDirection.VERTICAL -> {
                    verticalGripStart = relativeY.toFloat()
                }

                ScrollDirection.HORIZONTAL -> {
                    horizontalGripStart = relativeX.toFloat()
                }
            }
        }.whenMouseRelease {
            when (direction) {
                ScrollDirection.VERTICAL -> {
                    verticalGripStart = -1f
                }

                ScrollDirection.HORIZONTAL -> {
                    horizontalGripStart = -1f
                }
            }
        }.whenMouseDrag {
            val startCoord = when (direction) {
                ScrollDirection.VERTICAL -> verticalGripStart
                ScrollDirection.HORIZONTAL -> horizontalGripStart
            }

            if (startCoord == -1f) {
                return@whenMouseDrag
            }

            val mouseCoord = when (direction) {
                ScrollDirection.VERTICAL -> relativeY.toFloat()
                ScrollDirection.HORIZONTAL -> relativeX.toFloat()
            }

            dragGrip(component, mouseCoord, startCoord, direction)
        }.whenMouseScroll(scrollListener)

        needsUpdate = true
    }

    @JvmOverloads
    public fun setVerticalGripComponent(
        component: Component,
        hideIfUseless: Boolean = true
    ): ScrollingComponent = setGripComponent(component, ScrollDirection.VERTICAL, hideIfUseless)

    @JvmOverloads
    public fun setHorizontalGripComponent(
        component: Component,
        hideIfUseless: Boolean = true
    ): ScrollingComponent = setGripComponent(component, ScrollDirection.HORIZONTAL, hideIfUseless)

    public fun setAutoScrollIndicatorComponent(component: Component): ScrollingComponent = apply {
        autoScrollIndicatorComponent = component
        super.addChild(autoScrollIndicatorComponent!!)
        component.hide()

        needsUpdate = true
    }

    private fun handleScroll(delta: Float, direction: ScrollDirection): Boolean {
        var result = false

        val offsetProperty = if (direction == ScrollDirection.VERTICAL) ::verticalOffset else ::horizontalOffset
        val range = calculateOffset(direction)
        val newOffset = (offsetProperty.get() + delta * scrollDistance.getWidth(this) * currentScrollAcceleration).coerceIn(range)
        if (newOffset != offsetProperty.get()) {
            offsetProperty.set(newOffset)
            result = true
        }

        currentScrollAcceleration = (currentScrollAcceleration + (scrollAcceleration - 1f) * 0.15f).coerceIn(0f, scrollAcceleration)
        needsUpdate = true
        return result
    }

    private fun dragGrip(component: Component, mouseCoord: Float, gripCoord: Float, direction: ScrollDirection) {
        val parent = component.parent!!

        val minCoord = if (direction == ScrollDirection.VERTICAL) parent.top else parent.left
        val maxCoord = if (direction == ScrollDirection.VERTICAL) parent.bottom else parent.right
        val dragDelta = mouseCoord - gripCoord

        val newPos = if (direction == ScrollDirection.VERTICAL) {
            component.top
        } else {
            component.left
        } + dragDelta - minCoord
        val percentage = newPos / (maxCoord - minCoord)

        when (direction) {
            ScrollDirection.VERTICAL -> {
                verticalOffset = -(calculateContentHeight() * percentage)
            }

            ScrollDirection.HORIZONTAL -> {
                horizontalOffset = -(calculateContentWidth() * percentage)
            }
        }

        needsUpdate = true
    }

    private fun updateGripPosition(percentage: Float, percentageOfParent: Float, direction: ScrollDirection) {
        val component = when (direction) {
            ScrollDirection.VERTICAL -> verticalGripComponent
            ScrollDirection.HORIZONTAL -> horizontalGripComponent
        } ?: return

        val validatedPercentageOfParent = percentageOfParent.coerceAtMost(1f)

        val relativeProp = ParentRelativeProperty(validatedPercentageOfParent)
        val desiredSizeProp = GripMinSizeProperty(relativeProp)

        when (direction) {
            ScrollDirection.VERTICAL -> {
                component.configure {
                    properties {
                        height = desiredSizeProp
                    }
                }
            }

            ScrollDirection.HORIZONTAL -> {
                component.configure {
                    properties {
                        width = desiredSizeProp
                    }
                }
            }
        }

        component.animate {
            when (direction) {
                ScrollDirection.VERTICAL -> {
                    animateY(
                        easing = Easings.IN_SINE,
                        duration = 100.millis,
                        newValue = simpleYProperty { component ->
                            val parent = component.parent!!
                            val offset = (parent.height - component.height) * percentage
                            parent.top + offset
                        }
                    )
                }

                ScrollDirection.HORIZONTAL -> {
                    animateX(
                        easing = Easings.IN_SINE,
                        duration = 100.millis,
                        newValue = simpleXProperty { component ->
                            val parent = component.parent!!
                            val offset = (parent.width - component.width) * percentage
                            parent.left + offset
                        }
                    )
                }
            }
        }
    }

    private fun calculateContentWidth(): Float {
        if (contentHolder.getChildren().isEmpty()) {
            return 0f
        }

        return contentHolder.getChildren().maxOf { child ->
            child.right
        } - contentHolder.getChildren().minOf { child ->
            child.left
        }
    }

    private fun calculateContentHeight(): Float {
        if (contentHolder.getChildren().isEmpty()) {
            return 0f
        }

        return contentHolder.getChildren().maxOf { child ->
            child.bottom
        } - contentHolder.getChildren().minOf { child ->
            child.top
        }
    }

    private fun calculateOffset(direction: ScrollDirection): ClosedFloatingPointRange<Float> {
        return when (direction) {
            ScrollDirection.VERTICAL -> {
                val contentHeight = calculateContentHeight()
                val maxNegative = height - contentHeight
                maxNegative..innerPadding
            }

            ScrollDirection.HORIZONTAL -> {
                val contentWidth = calculateContentWidth()
                val maxNegative = width - contentWidth
                maxNegative..innerPadding
            }
        }
    }

    private fun ClosedFloatingPointRange<Float>.width(): Float {
        return abs(start - endInclusive)
    }

    // Overriding default methods to redirect to the content holder

    override fun hitTest(x: Double, y: Double): Component {
        return contentHolder.hitTest(x, y)
    }

    override fun makeRoot(engine: Engine): Component {
        return contentHolder.makeRoot(engine)
    }

    override fun <T : Component> attachedTo(parent: Component): T {
        return contentHolder.attachedTo(parent)
    }

    override fun addChild(child: Component) {
        if (child == autoScrollIndicatorComponent) {
            super.addChild(child)
            return
        }

        contentHolder.addChild(child)
    }

    override fun addChild(index: Int, child: Component) {
        contentHolder.addChild(index, child)
    }

    override fun replaceChild(index: Int, child: Component) {
        contentHolder.replaceChild(index, child)
    }

    override fun removeChild(child: Component) {
        if (child == autoScrollIndicatorComponent) {
            super.removeChild(child)
            return
        }

        contentHolder.removeChild(child)
    }

    override fun removeChild(index: Int) {
        contentHolder.removeChild(index)
    }

    override fun clearChildren() {
        contentHolder.clearChildren()
    }

    override fun getChildren(): List<Component> {
        return contentHolder.getChildren()
    }

    override fun getChildAt(index: Int): Component {
        return contentHolder.getChildAt(index)
    }

    override fun indexOfChild(child: Component): Int {
        return contentHolder.indexOfChild(child)
    }

    override fun handleMouseClick(x: Double, y: Double, button: MouseButton) {
        contentHolder.handleMouseClick(x, y, button)
    }

}
