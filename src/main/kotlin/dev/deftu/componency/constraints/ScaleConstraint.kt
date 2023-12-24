package dev.deftu.componency.constraints

import dev.deftu.componency.components.BaseComponent
import dev.deftu.state.SimpleState
import dev.deftu.state.State

class ScaleConstraint(
    val constraint: Constraint<Float>,
    scale: State<Float>
) : BoxConstraint {
    constructor(
        constraint: Constraint<Float>,
        scale: Float
    ) : this(constraint, SimpleState(scale))

    override var cached = 0f
    override var recalculate = true
    override var attachedTo: BaseComponent? = null

    private val scaleState = scale.map { it }
    private var scale: Float
        get() = scaleState.get()
        set(value) {
            scaleState.set(value)
        }

    fun bindScale(scale: State<Float>) = apply {
        scaleState.rebind(scale)
    }

    override fun handleAnimate() {
        super.handleAnimate()
        constraint.handleAnimate()
    }

    override fun getImplValueForX(component: BaseComponent): Float {
        val constraintX = constraint as XConstraint
        return constraintX.getValueForX(component) * scale
    }

    override fun getImplValueForY(component: BaseComponent): Float {
        val constraintY = constraint as YConstraint
        return constraintY.getValueForY(component) * scale
    }

    override fun getImplValueForWidth(component: BaseComponent): Float {
        val constraintWidth = constraint as WidthConstraint
        return constraintWidth.getValueForWidth(component) * scale
    }

    override fun getImplValueForHeight(component: BaseComponent): Float {
        val constraintHeight = constraint as HeightConstraint
        return constraintHeight.getValueForHeight(component) * scale
    }

    override fun getImplValueForRadius(component: BaseComponent): Float {
        val constraintRadius = constraint as RadiusConstraint
        return constraintRadius.getValueForRadius(component) * scale
    }
}
