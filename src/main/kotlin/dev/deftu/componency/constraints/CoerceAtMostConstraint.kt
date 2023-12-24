package dev.deftu.componency.constraints

import dev.deftu.componency.components.BaseComponent

class CoerceAtMostConstraint(
    val value: Constraint<Float>,
    val max: Constraint<Float>
) : BoxConstraint {
    override var cached = 0f
    override var recalculate = true
    override var attachedTo: BaseComponent? = null

    override fun handleAnimate() {
        super.handleAnimate()
        value.handleAnimate()
        max.handleAnimate()
    }

    override fun getImplValueForX(component: BaseComponent): Float {
        val valueX = value as XConstraint
        val maxX = max as XConstraint
        return valueX.getValueForX(component).coerceAtMost(maxX.getValueForX(component))
    }

    override fun getImplValueForY(component: BaseComponent): Float {
        val valueY = value as YConstraint
        val maxY = max as YConstraint
        return valueY.getValueForY(component).coerceAtMost(maxY.getValueForY(component))
    }

    override fun getImplValueForWidth(component: BaseComponent): Float {
        val valueWidth = value as WidthConstraint
        val maxWidth = max as WidthConstraint
        return valueWidth.getValueForWidth(component).coerceAtMost(maxWidth.getValueForWidth(component))
    }

    override fun getImplValueForHeight(component: BaseComponent): Float {
        val valueHeight = value as HeightConstraint
        val maxHeight = max as HeightConstraint
        return valueHeight.getValueForHeight(component).coerceAtMost(maxHeight.getValueForHeight(component))
    }

    override fun getImplValueForRadius(component: BaseComponent): Float {
        val valueRadius = value as RadiusConstraint
        val maxRadius = max as RadiusConstraint
        return valueRadius.getValueForRadius(component).coerceAtMost(maxRadius.getValueForRadius(component))
    }
}
