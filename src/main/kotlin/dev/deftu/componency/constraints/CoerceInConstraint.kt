package dev.deftu.componency.constraints

import dev.deftu.componency.components.BaseComponent

class CoerceInConstraint(
    val value: Constraint<Float>,
    val min: Constraint<Float>,
    val max: Constraint<Float>
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
        val minX = min as XConstraint
        val maxX = max as XConstraint
        return valueX.getValueForX(component).coerceIn(minX.getValueForX(component), maxX.getValueForX(component))
    }

    override fun getImplValueForY(component: BaseComponent): Float {
        val valueY = value as YConstraint
        val minY = min as YConstraint
        val maxY = max as YConstraint
        return valueY.getValueForY(component).coerceIn(minY.getValueForY(component), maxY.getValueForY(component))
    }

    override fun getImplValueForWidth(component: BaseComponent): Float {
        val valueWidth = value as WidthConstraint
        val minWidth = min as WidthConstraint
        val maxWidth = max as WidthConstraint
        return valueWidth.getValueForWidth(component).coerceIn(minWidth.getValueForWidth(component), maxWidth.getValueForWidth(component))
    }

    override fun getImplValueForHeight(component: BaseComponent): Float {
        val valueHeight = value as HeightConstraint
        val minHeight = min as HeightConstraint
        val maxHeight = max as HeightConstraint
        return valueHeight.getValueForHeight(component).coerceIn(minHeight.getValueForHeight(component), maxHeight.getValueForHeight(component))
    }

    override fun getImplValueForRadius(component: BaseComponent): Float {
        val valueRadius = value as RadiusConstraint
        val minRadius = min as RadiusConstraint
        val maxRadius = max as RadiusConstraint
        return valueRadius.getValueForRadius(component).coerceIn(minRadius.getValueForRadius(component), maxRadius.getValueForRadius(component))
    }
}
