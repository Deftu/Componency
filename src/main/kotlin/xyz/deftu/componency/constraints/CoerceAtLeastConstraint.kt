package xyz.deftu.componency.constraints

import xyz.deftu.componency.components.BaseComponent

class CoerceAtLeastConstraint(
    val value: Constraint<Float>,
    val min: Constraint<Float>
) : BoxConstraint {
    override var cached = 0f
    override var recalculate = true
    override var attachedTo: BaseComponent? = null

    override fun handleAnimate() {
        super.handleAnimate()
        value.handleAnimate()
        min.handleAnimate()
    }

    override fun getImplValueForX(component: BaseComponent): Float {
        val valueX = value as XConstraint
        val maxX = min as XConstraint
        return valueX.getValueForX(component).coerceAtLeast(maxX.getValueForX(component))
    }

    override fun getImplValueForY(component: BaseComponent): Float {
        val valueY = value as YConstraint
        val maxY = min as YConstraint
        return valueY.getValueForY(component).coerceAtLeast(maxY.getValueForY(component))
    }

    override fun getImplValueForWidth(component: BaseComponent): Float {
        val valueWidth = value as WidthConstraint
        val maxWidth = min as WidthConstraint
        return valueWidth.getValueForWidth(component).coerceAtLeast(maxWidth.getValueForWidth(component))
    }

    override fun getImplValueForHeight(component: BaseComponent): Float {
        val valueHeight = value as HeightConstraint
        val maxHeight = min as HeightConstraint
        return valueHeight.getValueForHeight(component).coerceAtLeast(maxHeight.getValueForHeight(component))
    }

    override fun getImplValueForRadius(component: BaseComponent): Float {
        val valueRadius = value as RadiusConstraint
        val maxRadius = min as RadiusConstraint
        return valueRadius.getValueForRadius(component).coerceAtLeast(maxRadius.getValueForRadius(component))
    }
}
