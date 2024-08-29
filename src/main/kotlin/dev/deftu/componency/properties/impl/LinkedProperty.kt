package dev.deftu.componency.properties.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.VectorProperty

public class LinkedProperty(
    private val property: VectorProperty,
) : VectorProperty {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    override fun frame() {
        super.frame()
        property.frame()
    }

    override fun calculateX(component: Component): Float {
        return property.getX(component)
    }

    override fun calculateY(component: Component): Float {
        return property.getY(component)
    }

    override fun calculateWidth(component: Component): Float {
        return property.getWidth(component)
    }

    override fun calculateHeight(component: Component): Float {
        return property.getHeight(component)
    }

    override fun calculateRadius(component: Component): Float {
        return property.getRadius(component)
    }

}
