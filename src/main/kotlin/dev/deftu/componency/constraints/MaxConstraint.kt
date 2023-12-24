package dev.deftu.componency.constraints

import dev.deftu.componency.components.BaseComponent
import kotlin.math.max

class MaxConstraint(
    val first: Constraint<Float>,
    val second: Constraint<Float>
) : BoxConstraint {
    override var cached = 0f
    override var recalculate = true
    override var attachedTo: BaseComponent? = null

    override fun handleAnimate() {
        super.handleAnimate()
        first.handleAnimate()
        second.handleAnimate()
    }

    override fun getImplValueForX(component: BaseComponent): Float {
        val firstX = first as XConstraint
        val secondX = second as XConstraint
        return max(firstX.getValueForX(component), secondX.getValueForX(component))
    }

    override fun getImplValueForY(component: BaseComponent): Float {
        val firstY = first as YConstraint
        val secondY = second as YConstraint
        return max(firstY.getValueForY(component), secondY.getValueForY(component))
    }

    override fun getImplValueForWidth(component: BaseComponent): Float {
        val firstWidth = first as WidthConstraint
        val secondWidth = second as WidthConstraint
        return max(firstWidth.getValueForWidth(component), secondWidth.getValueForWidth(component))
    }

    override fun getImplValueForHeight(component: BaseComponent): Float {
        val firstHeight = first as HeightConstraint
        val secondHeight = second as HeightConstraint
        return max(firstHeight.getValueForHeight(component), secondHeight.getValueForHeight(component))
    }

    override fun getImplValueForRadius(component: BaseComponent): Float {
        val firstRadius = first as RadiusConstraint
        val secondRadius = second as RadiusConstraint
        return max(firstRadius.getValueForRadius(component), secondRadius.getValueForRadius(component))
    }
}
