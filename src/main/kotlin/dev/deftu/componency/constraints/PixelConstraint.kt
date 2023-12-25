package dev.deftu.componency.constraints

import dev.deftu.componency.components.BaseComponent
import dev.deftu.stateful.SimpleState
import dev.deftu.stateful.State

class PixelConstraint(
    value: State<Float>,
    alignOpposite: State<Boolean> = SimpleState(false),
    alignOutside: State<Boolean> = SimpleState(false)
) : BoxConstraint {
    constructor(
        value: Float,
        alignOpposite: Boolean = false,
        alignOutside: Boolean = false
    ) : this(SimpleState(value), SimpleState(alignOpposite), SimpleState(alignOutside))

    private val valueState = value.map { it }
    private val alignOppositeState = alignOpposite.map { it }
    private val alignOutsideState = alignOutside.map { it }

    var value: Float
        get() = valueState.get()
        set(value) {
            valueState.set(value)
        }
    var alignOpposite: Boolean
        get() = alignOppositeState.get()
        set(value) {
            alignOppositeState.set(value)
        }
    var alignOutside: Boolean
        get() = alignOutsideState.get()
        set(value) {
            alignOutsideState.set(value)
        }

    override var cached = 0f
    override var recalculate = true
    override var attachedTo: BaseComponent? = null

    fun bindValue(state: State<Float>) = apply {
        valueState.rebind(state)
    }

    fun bindAlignOpposite(state: State<Boolean>) = apply {
        alignOppositeState.rebind(state)
    }

    fun bindAlignOutside(state: State<Boolean>) = apply {
        alignOutsideState.rebind(state)
    }

    override fun getImplValueForX(component: BaseComponent): Float {
        return if (alignOpposite) {
            if (alignOutside) {
                (attachedTo ?: component.parent).getRight() + value
            } else {
                (attachedTo ?: component.parent).getRight() - value - component.getWidth()
            }
        } else {
            if (alignOutside) {
                (attachedTo ?: component.parent).getX() - component.getWidth() - value
            } else {
                (attachedTo ?: component.parent).getX() + value
            }
        }
    }

    override fun getImplValueForY(component: BaseComponent): Float {
        return if (alignOpposite) {
            if (alignOutside) {
                (attachedTo ?: component.parent).getBottom() + value
            } else {
                (attachedTo ?: component.parent).getBottom() - value - component.getHeight()
            }
        } else {
            if (alignOutside) {
                (attachedTo ?: component.parent).getY() - component.getHeight() - value
            } else {
                (attachedTo ?: component.parent).getY() + value
            }
        }
    }

    override fun getImplValueForWidth(component: BaseComponent) = value
    override fun getImplValueForHeight(component: BaseComponent) = value
    override fun getImplValueForRadius(component: BaseComponent) = value
}
