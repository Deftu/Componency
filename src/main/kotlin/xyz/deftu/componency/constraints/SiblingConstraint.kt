package xyz.deftu.componency.constraints

import xyz.deftu.componency.components.BaseComponent
import xyz.deftu.state.SimpleState
import xyz.deftu.state.State

class SiblingConstraint(
    padding: State<Float> = SimpleState(0f),
    alignOpposite: State<Boolean> = SimpleState(false)
) : PositionalConstraint {
    constructor(padding: Float, alignOpposite: Boolean) : this(SimpleState(padding), SimpleState(alignOpposite))

    override var cached = 0f
    override var recalculate = true
    override var attachedTo: BaseComponent? = null

    private val paddingState = padding.map { it }
    private var padding: Float
        get() = paddingState.get()
        set(value) {
            paddingState.set(value)
        }

    private val alignOppositeState = alignOpposite.map { it }
    private var alignOpposite: Boolean
        get() = alignOppositeState.get()
        set(value) {
            alignOppositeState.set(value)
        }

    override fun getImplValueForX(component: BaseComponent): Float {
        if (attachedTo != null) {
            return if (alignOpposite) {
                attachedTo!!.getX() - component.getWidth() - padding
            } else {
                attachedTo!!.getRight() + padding
            }
        }

        val componentIndex = component.parent.children.indexOf(component)
        if (alignOpposite) {
            if (componentIndex == 0) return component.parent.getRight() - component.getWidth()
            val sibling = component.parent.children[componentIndex - 1]
            return getLeftmostPoint(sibling, component.parent, componentIndex)
        } else {
            if (componentIndex == 0) return component.parent.getX()
            val sibling = component.parent.children[componentIndex - 1]
            return getRightmostPoint(sibling, component.parent, componentIndex)
        }
    }

    override fun getImplValueForY(component: BaseComponent): Float {
        if (attachedTo != null) {
            return if (alignOpposite) {
                attachedTo!!.getY() - component.getHeight() - padding
            } else {
                attachedTo!!.getBottom() + padding
            }
        }

        val componentIndex = component.parent.children.indexOf(component)
        if (alignOpposite) {
            if (componentIndex == 0) return component.parent.getBottom() - component.getHeight()
            val sibling = component.parent.children[componentIndex - 1]
            return getHighestPoint(sibling, component.parent, componentIndex)
        } else {
            if (componentIndex == 0) return component.parent.getY()
            val sibling = component.parent.children[componentIndex - 1]
            return getLowestPoint(sibling, component.parent, componentIndex)
        }
    }

    protected fun getLowestPoint(sibling: BaseComponent, parent: BaseComponent, index: Int): Float {
        var lowestPoint = sibling.getBottom()

        for (n in index - 1 downTo 0) {
            val child = parent.children[n]

            if (child.getY() != sibling.getY()) break

            if (child.getBottom() > lowestPoint) lowestPoint = child.getBottom()
        }

        return lowestPoint
    }

    protected fun getHighestPoint(sibling: BaseComponent, parent: BaseComponent, index: Int): Float {
        var highestPoint = sibling.getY()

        for (n in index - 1 downTo 0) {
            val child = parent.children[n]

            if (child.getBottom() != sibling.getBottom()) break

            if (child.getY() < highestPoint) highestPoint = child.getY()
        }

        return highestPoint
    }

    protected fun getRightmostPoint(sibling: BaseComponent, parent: BaseComponent, index: Int): Float {
        var rightmostPoint = sibling.getRight()

        for (n in index - 1 downTo 0) {
            val child = parent.children[n]

            if (child.getX() != sibling.getX()) break

            if (child.getRight() > rightmostPoint) rightmostPoint = child.getRight()
        }

        return rightmostPoint
    }

    protected fun getLeftmostPoint(sibling: BaseComponent, parent: BaseComponent, index: Int): Float {
        var leftmostPoint = sibling.getX()

        for (n in index - 1 downTo 0) {
            val child = parent.children[n]

            if (child.getRight() != sibling.getRight()) break

            if (child.getX() < leftmostPoint) leftmostPoint = child.getX()
        }

        return leftmostPoint
    }
}
