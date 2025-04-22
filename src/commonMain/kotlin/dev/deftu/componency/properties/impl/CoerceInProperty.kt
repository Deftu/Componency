@file:Suppress("UNCHECKED_CAST")

package dev.deftu.componency.properties.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.*

public class CoerceInProperty(
    private val minimum: Property<Float>,
    private val maximum: Property<Float>,
    private val value: Property<Float>
) : VectorProperty {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    override fun animationFrame(deltaTime: Float) {
        super.animationFrame(deltaTime)
        minimum.animationFrame(deltaTime)
        maximum.animationFrame(deltaTime)
        value.animationFrame(deltaTime)
    }

    override fun calculateX(component: Component<*, *>): Float {
        val minimumX = minimum as XProperty
        val maximumX = maximum as XProperty
        val valueX = value as XProperty
        return valueX.getX(component).coerceIn(minimumX.getX(component), maximumX.getX(component))
    }

    override fun calculateY(component: Component<*, *>): Float {
        val minimumY = minimum as YProperty
        val maximumY = maximum as YProperty
        val valueY = value as YProperty
        return valueY.getY(component).coerceIn(minimumY.getY(component), maximumY.getY(component))
    }

    override fun calculateWidth(component: Component<*, *>): Float {
        val minimumWidth = minimum as WidthProperty
        val maximumWidth = maximum as WidthProperty
        val valueWidth = value as WidthProperty
        return valueWidth.getWidth(component).coerceIn(minimumWidth.getWidth(component), maximumWidth.getWidth(component))
    }

    override fun calculateHeight(component: Component<*, *>): Float {
        val minimumHeight = minimum as HeightProperty
        val maximumHeight = maximum as HeightProperty
        val valueHeight = value as HeightProperty
        return valueHeight.getHeight(component).coerceIn(minimumHeight.getHeight(component), maximumHeight.getHeight(component))
    }

    override fun calculateRadius(component: Component<*, *>): Float {
        val minimumRadius = minimum as RadialProperty
        val maximumRadius = maximum as RadialProperty
        val valueRadius = value as RadialProperty
        return valueRadius.getRadius(component).coerceIn(minimumRadius.getRadius(component), maximumRadius.getRadius(component))
    }

}
