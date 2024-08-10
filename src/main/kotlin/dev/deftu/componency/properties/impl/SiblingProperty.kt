package dev.deftu.componency.properties.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.PositionalProperty
import dev.deftu.stateful.State
import dev.deftu.stateful.utils.mappedMutableStateOf
import dev.deftu.stateful.utils.mutableStateBound
import dev.deftu.stateful.utils.stateOf

public open class SiblingProperty(
    paddingState: State<Float>,
    inverseState: State<Boolean>
) : PositionalProperty {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    private val paddingState = mappedMutableStateOf(paddingState) { it }
    private val inverseState = mappedMutableStateOf(inverseState) { it }

    public var padding: Float by mutableStateBound(this.paddingState)
    public var isInverse: Boolean by mutableStateBound(this.inverseState)

    public constructor(paddingState: State<Float>) : this(paddingState, stateOf(false))

    public constructor(padding: Float, inverse: Boolean) : this(stateOf(padding), stateOf(inverse))

    public constructor(padding: Float) : this(padding, false)

    override fun calculateX(component: Component): Float {
        return if (component.hasParent) {
            val parent = component.parent!!
            val componentIndex = parent.indexOfChild(component)
            if (isInverse) {
                if (componentIndex == 0) return parent.right - component.width
                val sibling = parent.getChildAt(componentIndex - 1)
                getLeftMostPoint(sibling, parent, componentIndex) - component.width - padding
            } else {
                if (componentIndex == 0) return parent.left
                val sibling = parent.getChildAt(componentIndex - 1)
                getRightMostPoint(sibling, parent, componentIndex) + padding
            }
        } else {
            throw UnsupportedOperationException("Cannot use SiblingProperty on root component")
        }
    }

    override fun calculateY(component: Component): Float {
        return if (component.hasParent) {
            val parent = component.parent!!
            val componentIndex = parent.indexOfChild(component)
            if (isInverse) {
                if (componentIndex == 0) return parent.bottom - component.height
                val sibling = parent.getChildAt(componentIndex - 1)
                getHighestPoint(sibling, parent, componentIndex) - component.height - padding
            } else {
                if (componentIndex == 0) return parent.top
                val sibling = parent.getChildAt(componentIndex - 1)
                getLowestPoint(sibling, parent, componentIndex) + padding
            }
        } else {
            throw UnsupportedOperationException("Cannot use SiblingProperty on root component")
        }
    }

    protected fun getLowestPoint(sibling: Component, parent: Component, index: Int): Float {
        var lowestPoint = sibling.bottom

        for (n in index - 1 downTo 0) {
            val child = parent.getChildAt(n)

            if (child.top != sibling.top) {
                break
            }

            if (child.bottom > lowestPoint) {
                lowestPoint = child.bottom
            }
        }

        return lowestPoint
    }

    protected fun getHighestPoint(sibling: Component, parent: Component, index: Int): Float {
        var highestPoint = sibling.top

        for (n in index - 1 downTo 0) {
            val child = parent.getChildAt(n)

            if (child.bottom != sibling.bottom) {
                break
            }

            if (child.top < highestPoint) {
                highestPoint = child.top
            }
        }

        return highestPoint
    }

    protected fun getRightMostPoint(sibling: Component, parent: Component, index: Int): Float {
        var rightmostPoint = sibling.right

        for (n in index - 1 downTo 0) {
            val child = parent.getChildAt(n)

            if (child.left != sibling.left) {
                break
            }

            if (child.right > rightmostPoint) {
                rightmostPoint = child.right
            }
        }

        return rightmostPoint
    }

    protected fun getLeftMostPoint(sibling: Component, parent: Component, index: Int): Float {
        var leftmostPoint = sibling.left

        for (n in index - 1 downTo 0) {
            val child = parent.getChildAt(n)

            if (child.right != sibling.right) {
                break
            }

            if (child.left < leftmostPoint) {
                leftmostPoint = child.left
            }
        }

        return leftmostPoint
    }

}
