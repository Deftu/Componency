package dev.deftu.componency.constraints

import dev.deftu.componency.components.BaseComponent
import dev.deftu.stateful.SimpleState
import dev.deftu.stateful.State

class RelativeConstraint(
    value: State<Float>
) : BoxConstraint {
    constructor(value: Float) : this(SimpleState(value))

    override var cached = 0f
    override var recalculate = true
    override var attachedTo: BaseComponent? = null

    private val valueState = value.map { it }

    var value: Float
        get() = valueState.get()
        set(value) {
            valueState.set(value)
        }

    fun bindValue(value: State<Float>) = apply {
        valueState.rebind(value)
    }

    override fun getImplValueForX(component: BaseComponent): Float {
        return (attachedTo ?: component.parent).getX() + getValueForWidth(component)
    }

    override fun getImplValueForY(component: BaseComponent): Float {
        return (attachedTo ?: component.parent).getY() + getValueForHeight(component)
    }

    override fun getImplValueForWidth(component: BaseComponent): Float {
        return (attachedTo ?: component.parent).getWidth() * value
    }

    override fun getImplValueForHeight(component: BaseComponent): Float {
        return (attachedTo ?: component.parent).getHeight() * value
    }

    override fun getImplValueForRadius(component: BaseComponent): Float {
        return ((attachedTo ?: component.parent).getWidth() * value) / 2
    }
}
