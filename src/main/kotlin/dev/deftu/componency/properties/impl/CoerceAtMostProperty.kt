package dev.deftu.componency.properties.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.*

public class CoerceAtMostProperty(
    private val maximum: Property<Float>,
    private val value: Property<Float>
) : VectorProperty {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    override fun frame() {
        super.frame()
        maximum.frame()
        value.frame()
    }

    override fun calculateX(component: Component): Float {
        val maximumX = maximum as XProperty
        val valueX = value as XProperty
        return valueX.getX(component).coerceAtMost(maximumX.getX(component))
    }

    override fun calculateY(component: Component): Float {
        val maximumY = maximum as YProperty
        val valueY = value as YProperty
        return valueY.getY(component).coerceAtMost(maximumY.getY(component))
    }

    override fun calculateWidth(component: Component): Float {
        val maximumWidth = maximum as WidthProperty
        val valueWidth = value as WidthProperty
        return valueWidth.getWidth(component).coerceAtMost(maximumWidth.getWidth(component))
    }

    override fun calculateHeight(component: Component): Float {
        val maximumHeight = maximum as HeightProperty
        val valueHeight = value as HeightProperty
        return valueHeight.getHeight(component).coerceAtMost(maximumHeight.getHeight(component))
    }

    override fun calculateRadius(component: Component): Float {
        val maximumRadius = maximum as RadialProperty
        val valueRadius = value as RadialProperty
        return valueRadius.getRadius(component).coerceAtMost(maximumRadius.getRadius(component))
    }

}
