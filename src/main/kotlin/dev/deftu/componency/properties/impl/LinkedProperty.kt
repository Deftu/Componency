package dev.deftu.componency.properties.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.*

public class LinkedProperty(
    private val property: Property<Float>,
) : VectorProperty {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    override fun animationFrame(deltaTime: Float) {
        super.animationFrame(deltaTime)
        property.animationFrame(deltaTime)
    }

    override fun calculateX(component: Component<*, *>): Float {
        val x = property as XProperty
        return x.getX(component)
    }

    override fun calculateY(component: Component<*, *>): Float {
        val y = property as YProperty
        return y.getY(component)
    }

    override fun calculateWidth(component: Component<*, *>): Float {
        val width = property as WidthProperty
        return width.getWidth(component)
    }

    override fun calculateHeight(component: Component<*, *>): Float {
        val height = property as HeightProperty
        return height.getHeight(component)
    }

    override fun calculateRadius(component: Component<*, *>): Float {
        val radius = property as RadialProperty
        return radius.getRadius(component)
    }

}
