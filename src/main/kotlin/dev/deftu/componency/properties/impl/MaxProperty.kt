package dev.deftu.componency.properties.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.*
import kotlin.math.max

public class MaxProperty(
    private val first: Property<Float>,
    private val second: Property<Float>
) : VectorProperty {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    override fun frame() {
        super.frame()
        first.frame()
        second.frame()
    }

    override fun calculateX(component: Component): Float {
        val firstX = first as XProperty
        val secondX = second as XProperty
        return max(firstX.getX(component), secondX.getX(component))
    }

    override fun calculateY(component: Component): Float {
        val firstY = first as YProperty
        val secondY = second as YProperty
        return max(firstY.getY(component), secondY.getY(component))
    }

    override fun calculateWidth(component: Component): Float {
        val firstWidth = first as WidthProperty
        val secondWidth = second as WidthProperty
        return max(firstWidth.getWidth(component), secondWidth.getWidth(component))
    }

    override fun calculateHeight(component: Component): Float {
        val firstHeight = first as HeightProperty
        val secondHeight = second as HeightProperty
        return max(firstHeight.getHeight(component), secondHeight.getHeight(component))
    }

    override fun calculateRadius(component: Component): Float {
        val firstRadius = first as RadialProperty
        val secondRadius = second as RadialProperty
        return max(firstRadius.getRadius(component), secondRadius.getRadius(component))
    }

}
