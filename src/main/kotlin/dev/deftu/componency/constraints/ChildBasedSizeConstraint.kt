package dev.deftu.componency.constraints

import dev.deftu.componency.components.BaseComponent
import dev.deftu.state.SimpleState
import dev.deftu.state.State

class ChildBasedSizeConstraint(
    padding: State<Float> = SimpleState(0f)
) : SizeConstraint {
    constructor(padding: Float) : this(SimpleState(padding))

    override var cached = 0f
    override var recalculate = true
    override var attachedTo: BaseComponent? = null

    private val paddingState = padding.map { it }
    private var padding: Float
        get() = paddingState.get()
        set(value) {
            paddingState.set(value)
        }

    fun bindPadding(padding: State<Float>) = apply {
        paddingState.rebind(padding)
    }

    override fun getImplValueForWidth(component: BaseComponent): Float {
        val target = attachedTo ?: component.parent
        return target.children.sumOf {
            it.getWidth() + ((it.constraints.x as? PaddingConstraint)?.getHorizontalPadding(it) ?: 0f).toDouble()
        }.toFloat() + (target.children.size - 1) * padding
    }

    override fun getImplValueForHeight(component: BaseComponent): Float {
        val target = attachedTo ?: component.parent
        return target.children.sumOf {
            it.getHeight() + ((it.constraints.y as? PaddingConstraint)?.getVerticalPadding(it) ?: 0f).toDouble()
        }.toFloat() + (target.children.size - 1) * padding
    }

    override fun getImplValueForRadius(component: BaseComponent): Float {
        val target = attachedTo ?: component.parent
        return target.children.sumOf {
            it.getHeight().toDouble()
        }.toFloat() * 2
    }
}
